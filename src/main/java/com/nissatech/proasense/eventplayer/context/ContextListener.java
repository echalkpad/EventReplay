package com.nissatech.proasense.eventplayer.context;

import com.nissatech.proasense.eventplayer.AsyncRequestWorker;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.ext.Provider;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleksandar
 */
@Provider
public class ContextListener implements ServletContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        LoggerFactory.getLogger(this.getClass().getName()).info("Context initialized");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 200, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new ArrayBlockingQueue<Runnable>(100));
        //Map jobs = new ConcurrentHashMap<String,AsyncRequestWorker>();
        sce.getServletContext().setAttribute("executor", executor);
        final ConcurrentHashMap<String, AsyncRequestWorker> jobs = new ConcurrentHashMap<String,AsyncRequestWorker>();
        sce.getServletContext().setAttribute("jobs", jobs);
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(new Runnable()
        {

            @Override
            public void run()
            {
                for(Map.Entry<String, AsyncRequestWorker> s : jobs.entrySet())
                {
                    if(!s.getValue().isRunning())
                        jobs.remove(s.getKey());
                }
            }

        }, 30, 30, TimeUnit.MINUTES
        );
        sce.getServletContext().setAttribute("pruning", scheduledExecutor);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        LoggerFactory.getLogger(this.getClass().getName()).info("Context destroyed");
        ThreadPoolExecutor executor = (ThreadPoolExecutor) sce.getServletContext().getAttribute("executor");
        if (executor != null) {
            executor.shutdownNow();
        }
        ScheduledExecutorService pruningJob = (ScheduledExecutorService) sce.getServletContext().getAttribute("pruning");
        pruningJob.shutdownNow();
    }

}
