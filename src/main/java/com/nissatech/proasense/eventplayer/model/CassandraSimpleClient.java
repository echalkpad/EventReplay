package com.nissatech.proasense.eventplayer.model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 *
 * @author aleksandar
 */
public class CassandraSimpleClient
{

    private Cluster cluster;
    private Session session;

    public CassandraSimpleClient()
    {
        
    }
    public void connect(String address)
    {
        cluster = Cluster.builder().addContactPoint(address).build(); 
        session = cluster.connect("proasense");
        
    }
    public void close()
    {
        if(session!=null) session.close();
        if(cluster!=null) cluster.close();
    }
    public BoundStatement prepareStatement(String statement)
    {
        return new BoundStatement(session.prepare(statement));
    }
    public ResultSet execute(BoundStatement statement)
    {
        return session.execute(statement);
    }

}
