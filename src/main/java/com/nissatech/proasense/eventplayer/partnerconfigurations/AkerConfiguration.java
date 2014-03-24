/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nissatech.proasense.eventplayer.partnerconfigurations;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.nissatech.proasense.eventplayer.model.CassandraSimpleClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Hours;

/**
 *
 * @author aleksandar
 */
public class AkerConfiguration implements PartnerConfiguration
{

    @Override
    public BoundStatement generateQuery(DateTime startTime, DateTime endTime, List<String> variables, CassandraSimpleClient client)
    {
     
        DateTime roundedStartTime = startTime.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0); //to be sure we have the complete hour
        
        List<String> inKeys = new ArrayList<String>();
        int hoursBetween = Hours.hoursBetween(roundedStartTime, endTime).getHours();
        for (int i = 0; i < hoursBetween; i++) {
            long milliKeyPart = roundedStartTime.plusHours(i).getMillis();
            for (String variable : variables) {

                inKeys.add(variable+"|"+milliKeyPart);
            }

        }
     
        //the ? in the prepared query is out of parenthesis for a reason. This is a deviation from the standard when using the IN keyword
        String query = "select * from aker_variables where type_hour_stamp in ? order by variable_timestamp";
        BoundStatement statement = client.prepareStatement(query);
        statement.bind(inKeys);
                
        return statement;
    }

}
