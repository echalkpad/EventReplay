package com.nissatech.proasense.eventplayer.model;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.inject.Inject;

/**
 * An implementation of the Cassandra Client
 * @author aleksandar
 */
public class CassandraClient
{

    private Cluster cluster;
    private Session session;

    @Inject
    public CassandraClient()
    {
    }
    public CassandraClient connect(String address)
    {
        cluster = Cluster.builder().addContactPoint(address).build(); 
        session = cluster.connect("proasense");
        return this;
        
    }
    /**
     * Don't forget to call this when you're finished :) 
     */
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
