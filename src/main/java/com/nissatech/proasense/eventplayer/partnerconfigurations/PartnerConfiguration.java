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
 */
public interface PartnerConfiguration
{
    public BoundStatement generateQuery(DateTime startTime, DateTime endTime, List<String> variables, CassandraClient client);
    public String generateMessage(Row row) throws IOException;
    public String generateBatch(ResultSet rows) throws IOException;
   
}
