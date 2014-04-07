/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nissatech.proasense.eventplayer.filters;

import com.google.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author aleksandar
 */

@Provider
public class SecurityFilter implements ContainerRequestFilter
{

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    
    @Inject
    Properties properties;
    
    
    @Override
    public void filter(ContainerRequestContext crc) throws IOException
    {
        boolean authorize =  Boolean.valueOf(properties.get("client.authorize").toString());
        if(!authorize) return;
        final MultivaluedMap<String, String> headers = crc.getHeaders();
        if(!headers.containsKey(AUTHORIZATION_PROPERTY))
        {
            crc.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        String authorization = headers.getFirst(AUTHORIZATION_PROPERTY);
        String password = properties.get("client.password").toString();
        if(!authorization.equals(password)) 
            crc.abortWith(Response.status(Response.Status.FORBIDDEN).build());
    
    }
    
}
