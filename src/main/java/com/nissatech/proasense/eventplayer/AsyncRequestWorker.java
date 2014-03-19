package com.nissatech.proasense.eventplayer;

import com.nissatech.proasense.eventplayer.model.PlaybackRequest;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.ext.Provider;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Aleksandar
 */

@Provider
public class AsyncRequestWorker implements Runnable {

    private PlaybackRequest request;
    private String id;

    private boolean running;
    private boolean finished;

    public boolean isFinished()
    {
        return finished;
    }
    private final ScheduledExecutorService scheduler;
    
    
    public AsyncRequestWorker(PlaybackRequest request, String id) {
        this.request=request;
        this.id=id;
        running = true;
        finished=false;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        
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
    public void run() {
        try {
            scheduler.schedule(new EventSender(), 2, TimeUnit.SECONDS);
            scheduler.schedule(new EventSender(), 5, TimeUnit.SECONDS);
            scheduler.schedule(new EventSender(), 10, TimeUnit.SECONDS);
            scheduler.schedule(new EventSender(), 15, TimeUnit.SECONDS);
            scheduler.schedule(new EventSender(), 20, TimeUnit.SECONDS);
            scheduler.schedule(new EventSender(), 25, TimeUnit.SECONDS);
            scheduler.schedule(new EventSender(), 30, TimeUnit.SECONDS);
            scheduler.shutdown();
            scheduler.awaitTermination(12, TimeUnit.HOURS);  
            finished=true;
        }
        catch (InterruptedException ex) {
            LoggerFactory.getLogger(this.getClass().getName()).error(ex.toString());
        }
        finally
        {
            if(!scheduler.isTerminated())
                scheduler.shutdownNow();
            running = false;
              
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
