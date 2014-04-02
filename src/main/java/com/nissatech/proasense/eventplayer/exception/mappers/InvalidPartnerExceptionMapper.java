package com.nissatech.proasense.eventplayer.exception.mappers;

import com.nissatech.proasense.eventplayer.partnerconfigurations.InvalidPartnerException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Mapping the {@link InvalidPartnerException to a HTTP 400 Response} 
 * @author aleksandar
 */
@Provider
public class InvalidPartnerExceptionMapper implements ExceptionMapper<InvalidPartnerException>
{

    @Override
    public Response toResponse(InvalidPartnerException exception)
    {
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).entity(exception.getMessage()).build();
        
    }
    
}
