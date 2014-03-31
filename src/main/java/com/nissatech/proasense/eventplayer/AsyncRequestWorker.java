package com.nissatech.proasense.eventplayer;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.nissatech.proasense.eventplayer.context.KafkaProducerFactory;
import com.nissatech.proasense.eventplayer.model.CassandraClient;
import com.nissatech.proasense.eventplayer.model.PlaybackRequest;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfiguration;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import kafka.javaapi.producer.Producer;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Aleksandar
 */
public class AsyncRequestWorker implements Runnable
{

    private PlaybackRequest request;
    private String id;

    private boolean running;
    private boolean finished;

    private final Semaphore sendersAvailable = new Semaphore(50,true);

    private final PartnerConfiguration partnerConfiguration;
    
    @Inject
    private KafkaProducerFactory kpFactory; 
    
    @Inject
    private Properties properties;
    
    @Inject
    private CassandraClient cassandraClient;

    public boolean isFinished()
    {
        return finished;
    }
    private final ScheduledExecutorService scheduler;

    @Inject
    public AsyncRequestWorker(@Assisted PlaybackRequest request, @Assisted String id, @Assisted PartnerConfiguration conf)
    {
        this.request = request;
        this.id = id;
        running = true;
        finished = false;
        scheduler = Executors.newScheduledThreadPool(1);
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
        Producer producer = null;
        
        try
        {
            cassandraClient.connect(properties.getProperty("cassandra.host"));
         
            BoundStatement generatedQuery = partnerConfiguration.generateQuery(request.getStartTime(), request.getEndTime(), request.getVariables(), cassandraClient);
            ResultSet results = cassandraClient.execute(generatedQuery);
            long accumulatedDelay = 0;
            long startOfSending = System.currentTimeMillis();
            long previousEventTime=Long.MAX_VALUE;
            for (Row row : results)
            {

                sendersAvailable.acquire();               
                DateTime eventTime = new DateTime(row.getDate("variable_timestamp"));
                long deltaEvent = eventTime.getMillis() - previousEventTime;
                if(deltaEvent < 0) deltaEvent =0;
                accumulatedDelay += deltaEvent;
                
                long deltaAbsolute = accumulatedDelay - (System.currentTimeMillis() - startOfSending);
                producer = kpFactory.createProducer();

                scheduler.schedule(new EventSender(sendersAvailable, producer, partnerConfiguration.generateMessage(row), request), deltaAbsolute, TimeUnit.MILLISECONDS);
                previousEventTime = eventTime.getMillis();
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
            if (cassandraClient != null)
            {
                cassandraClient.close();
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
