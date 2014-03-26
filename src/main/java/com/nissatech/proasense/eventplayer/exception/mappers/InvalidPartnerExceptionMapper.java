/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nissatech.proasense.eventplayer.exception.mappers;

/**
 *
 * @author aleksandar
 */
import com.nissatech.proasense.eventplayer.partnerconfigurations.InvalidPartnerException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidPartnerExceptionMapper implements ExceptionMapper<InvalidPartnerException>
{

    @Override
    public Response toResponse(InvalidPartnerException exception)
    {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception).build();
        
    }
    
}
