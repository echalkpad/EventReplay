package com.nissatech.proasense.eventplayer;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.google.inject.Inject;
import com.nissatech.proasense.eventplayer.model.CassandraClient;
import com.nissatech.proasense.eventplayer.partnerconfigurations.InvalidPartnerException;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfiguration;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfigurationResolver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.joda.time.DateTime;

/**
 *
 * @author aleksandar
 */
@Path("/batch")
public class BatchEvents
{ 
    @Inject
    private PartnerConfigurationResolver resolver;
    
    @Inject 
    private CassandraClient cassandraClient;

    @Inject
    private Properties properties;
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{partner}/variables/{variables}/between/{startTime}/and/{endTime}")
    public Response batchEvents(@PathParam("partner") String partner, @PathParam("variables") String variables, @PathParam("startTime") DateTime start, @PathParam("endTime") DateTime end) throws IOException, InvalidPartnerException
    {
        
        PartnerConfiguration configuration = resolver.getConfiguration(partner);
        String[] variableList = variables.split(",");
        
        cassandraClient.connect(properties.getProperty("cassandra.host"));
        BoundStatement generatedQuery = configuration.generateQuery(start, end, Arrays.asList(variableList), cassandraClient);
        ResultSet results = cassandraClient.execute(generatedQuery);
        String generateBatch = configuration.generateBatch(results);
        
        return Response.ok().entity(generateBatch).build();
    }
}
