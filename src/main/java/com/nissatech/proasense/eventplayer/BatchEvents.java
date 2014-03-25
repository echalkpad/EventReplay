package com.nissatech.proasense.eventplayer;

import com.datastax.driver.core.BoundStatement;
import com.nissatech.proasense.eventplayer.model.CassandraSimpleClient;
import com.nissatech.proasense.eventplayer.partnerconfigurations.AkerConfiguration;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfiguration;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import javax.ejb.BeforeCompletion;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.joda.time.DateTime;

/**
 *
 * @author aleksandar
 */
@Provider
@Path("/batch")
public class BatchEvents
{

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{partner}/variables/{variables}/between/{startTime}/and/{endTime}")
    public Response batchEvents(@PathParam("partner") String partner, @PathParam("variables") String variables, @PathParam("startTime") DateTime start, @PathParam("endTime") DateTime end) throws IOException
    {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("conf.properties");
        Properties props = new Properties();
        props.load(resourceAsStream);
        PartnerConfiguration configuration = new AkerConfiguration();
        String[] variableList = variables.split(",");
        BoundStatement generatedQuery = configuration.generateQuery(start, end, Arrays.asList(variableList), new CassandraSimpleClient());
        return Response.ok().build();
    }
}
