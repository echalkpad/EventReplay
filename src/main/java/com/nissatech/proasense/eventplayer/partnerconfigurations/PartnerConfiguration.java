package com.nissatech.proasense.eventplayer.partnerconfigurations;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.nissatech.proasense.eventplayer.model.CassandraClient;
import java.io.IOException;
import java.util.List;
import org.joda.time.DateTime;

/**
 *
 * @author aleksandar
 * Describes a typical partner configuration. Each configuration is a helper for
 * creating queries, real time and batch messages for the respective client.
 */
public interface PartnerConfiguration
{
    /**
     * Generates a Cassandra statement which should return all events happening in a defined time span.
     * @param startTime The start time of the interest period.
     * @param endTime End time of the interest period
     * @param variables Variables that should be fetched
     * @param client {@link CassandraClient} with an already opened connection
     * @return Statement ready for execution
     */
    
    public BoundStatement generateQuery(DateTime startTime, DateTime endTime, List<String> variables, CassandraClient client);
    
    /**
     * Generates a JSON message converted into a String suitable for sending using a broker
     * from the row data, typically return with 
     * {@link #generateQuery(org.joda.time.DateTime, org.joda.time.DateTime, java.util.List, com.nissatech.proasense.eventplayer.model.CassandraClient)}
     * @param row One row fetched from the Cassandra database
     * @return Event transformed into a JSON message.
     * @throws IOException 
     */
    public String generateMessage(Row row) throws IOException;
    
    /**
     * Generates a batch of events, represented with a JSON array as a String, suitable for using as a
     * batched response. 
     * @param rows All the events fetched from the database, typically with a query generated with 
     * {@link #generateQuery(org.joda.time.DateTime, org.joda.time.DateTime, java.util.List, com.nissatech.proasense.eventplayer.model.CassandraClient)} ;
     * @return
     * @throws IOException 
     */
    public String generateBatch(ResultSet rows) throws IOException;
   
}
