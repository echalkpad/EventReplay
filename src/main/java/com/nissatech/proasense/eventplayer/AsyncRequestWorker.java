package com.nissatech.proasense.eventplayer;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.nissatech.proasense.eventplayer.context.KafkaProducerFactory;
import com.nissatech.proasense.eventplayer.model.CassandraSimpleClient;
import com.nissatech.proasense.eventplayer.model.PlaybackRequest;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfiguration;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.ext.Provider;
import kafka.javaapi.producer.Producer;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Aleksandar
 */
@Provider
public class AsyncRequestWorker implements Runnable
{

    private PlaybackRequest request;
    private String id;

    private boolean running;
    private boolean finished;

    private final Semaphore sendersAvailable = new Semaphore(10, true);

    private final PartnerConfiguration partnerConfiguration;

    public boolean isFinished()
    {
        return finished;
    }
    private final ScheduledExecutorService scheduler;

    public AsyncRequestWorker(PlaybackRequest request, String id, PartnerConfiguration conf)
    {
        this.request = request;
        this.id = id;
        running = true;
        finished = false;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        this.partnerConfiguration = conf;

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public PlaybackRequest getRequest()
    {
        return request;
    }

    public void setRequest(PlaybackRequest request)
    {
        this.request = request;
    }

    @Override
    public void run()
    {
        CassandraSimpleClient client = null;
        Producer producer = null;
        try
        {
            client = new CassandraSimpleClient();
            KafkaProducerFactory<String, String> kpFactory = new KafkaProducerFactory<String, String>();
            client.connect("127.0.0.1");
            BoundStatement generatedQuery = partnerConfiguration.generateQuery(request.getStartTime(), request.getEndTime(), request.getVariables(), client);
            ResultSet results = client.execute(generatedQuery);
            long previous = Long.MAX_VALUE;
            long lastSendSubmitted = 0L;
            for (Row row : results)
            {

                sendersAvailable.acquire();
                long deltaRealTime = System.currentTimeMillis() - lastSendSubmitted; //we have to count in the CPU overhead (waiting for the thread pool and such). That time will be deducted from the pause between events. Real (current) time span at which the last event was scheduled
                DateTime eventTime = new DateTime(row.getDate("variable_timestamp"));

                long deltaEvent = eventTime.getMillis() - previous; 
                long deltaAbsolute = deltaEvent - deltaRealTime; //the time in which the event should be scheduled, based on the difference between the last recorded event and time when the last one is scheduled
                if (deltaAbsolute < 0)
                {
                    deltaAbsolute = 0;
                }
                
                scheduler.schedule(new EventSender(sendersAvailable, kpFactory.createProducer(), partnerConfiguration.generateMessage(row), request), deltaAbsolute, TimeUnit.MILLISECONDS);

                lastSendSubmitted = System.currentTimeMillis();
                previous = eventTime.getMillis();
            }

            scheduler.shutdown();
            scheduler.awaitTermination(12, TimeUnit.HOURS);
            finished = true;
        }
        catch (InterruptedException ex)
        {
            LoggerFactory.getLogger(this.getClass().getName()).error(ex.toString());
        }
        catch (IOException ex)
        {
            LoggerFactory.getLogger(this.getClass().getName()).error(ex.toString());
        }
        finally
        {
            if (!scheduler.isTerminated())
            {
                scheduler.shutdownNow();
            }
            running = false;
            if (client != null)
            {
                client.close();
            }
            if (producer != null)
            {
                producer.close();
            }

        }

    }

    public void stop()
    {
        running = false;
        this.scheduler.shutdownNow();

    }

    public boolean isRunning()
    {
        return running;
    }

}
