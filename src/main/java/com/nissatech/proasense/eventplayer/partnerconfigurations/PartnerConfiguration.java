package com.nissatech.proasense.eventplayer.partnerconfigurations;

import com.datastax.driver.core.BoundStatement;
import com.nissatech.proasense.eventplayer.model.CassandraSimpleClient;
import java.util.List;
import org.joda.time.DateTime;

/**
 *
 * @author aleksandar
 */
public interface PartnerConfiguration
{
    public BoundStatement generateQuery(DateTime startTime, DateTime endTime, List<String> variables, CassandraSimpleClient client);
   
}
