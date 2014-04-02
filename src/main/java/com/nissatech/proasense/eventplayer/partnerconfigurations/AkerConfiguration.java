package com.nissatech.proasense.eventplayer.partnerconfigurations;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.nissatech.proasense.eventplayer.model.CassandraClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Hours;

/**
 * {@inheritDoc}
 * Implements the configuration for the Aker use case
 */
public class AkerConfiguration implements PartnerConfiguration
{

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundStatement generateQuery(DateTime startTime, DateTime endTime, List<String> variables, CassandraClient client)
    {

        DateTime roundedStartTime = startTime.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0); //to be sure we have the complete hour

        List<String> inKeys = new ArrayList<String>();
        int hoursBetween = Hours.hoursBetween(roundedStartTime, endTime).getHours();
        for (int i = 0; i < hoursBetween; i++)
        {
            long milliKeyPart = roundedStartTime.plusHours(i).getMillis();
            for (String variable : variables)
            {

                inKeys.add(variable + "|" + milliKeyPart);
            }

        }

        //the ? in the prepared query is out of parenthesis for a reason. This is a deviation from the typical CQL standard when using the IN keyword
        String query = "select * from aker_variables where type_hour_stamp in ? order by variable_timestamp";
        BoundStatement statement = client.prepareStatement(query);
        statement.bind(inKeys);

        return statement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateMessage(Row row) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setDateFormat(new ISO8601DateFormat());
        HashMap message = new HashMap();
        DateTime date = new DateTime(row.getDate("variable_timestamp"));
        message.put("variable_timestamp", date);
        message.put("value", row.getDouble("value"));
        message.put("variable_type", row.getString("variable_type"));
        return mapper.writeValueAsString(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateBatch(ResultSet rows) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setDateFormat(new ISO8601DateFormat());
        List<HashMap> batch = new ArrayList<HashMap>();
        for (Row row : rows)
        {
            HashMap message = new HashMap();
            DateTime date = new DateTime(row.getDate("variable_timestamp"));
            message.put("variable_timestamp", date);
            message.put("value", row.getDouble("value"));
            message.put("variable_type", row.getString("variable_type"));
            batch.add(message);
        }
        return mapper.writeValueAsString(batch);
    }

}
