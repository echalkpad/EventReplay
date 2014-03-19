package com.nissatech.proasense.eventplayer;


import com.nissatech.proasense.eventplayer.model.PlaybackRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import javax.validation.executable.ValidateOnExecution;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.joda.time.DateTime;

/**
 *
 * @author aleksandar
 */
@Path("/playback")

public class EventPlayer
{

    @Context
    private ServletContext context;
    @Context
    UriInfo uri;

    @POST
    @Produces(value = MediaType.TEXT_PLAIN)
    @ValidateOnExecution
    public Response startWorker(@Valid PlaybackRequest request)
    {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) context.getAttribute("executor");
        Map<String, AsyncRequestWorker> jobMap = (Map<String, AsyncRequestWorker>) context.getAttribute("jobs");
        
        request.setSubmitted(new DateTime());
        AsyncRequestWorker worker = new AsyncRequestWorker(request, UUID.randomUUID().toString());
        executor.execute(worker);
        jobMap.put(worker.getId(), worker);
        return Response.status(Response.Status.CREATED).header("Location",uri.getRequestUri()+"/"+worker.getId()).build();
    }

    @DELETE
    @Path("/{workerId}")
    public Response stopWorker(@PathParam("workerId") String workerid)
    {
        Map<String, AsyncRequestWorker> jobMap = (Map<String, AsyncRequestWorker>) context.getAttribute("jobs");
        AsyncRequestWorker worker = jobMap.get(workerid);
        if (worker != null) {
            worker.stop();
            return Response.status(Response.Status.OK).build();
        }
        else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response fetchRunningTasks() throws IOException
    {
        Map<String, AsyncRequestWorker> jobMap = (Map<String, AsyncRequestWorker>) context.getAttribute("jobs");

        return Response.status(Response.Status.OK).entity(jobMap.values()).build();
    }

    @GET
    @Path("/{workerId}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response fetchTaskDetails(@PathParam("workerId") String workerid
    )
    {
        Map<String, AsyncRequestWorker> jobMap = (Map<String, AsyncRequestWorker>) context.getAttribute("jobs");
        AsyncRequestWorker worker = jobMap.get(workerid);
        if (worker != null) {
            return Response.status(Response.Status.OK).entity(worker).build();
        }
        else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }
}
