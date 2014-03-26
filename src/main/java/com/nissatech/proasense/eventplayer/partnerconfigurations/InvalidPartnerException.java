package com.nissatech.proasense.eventplayer.partnerconfigurations;

import javax.xml.ws.WebServiceException;

/**
 *
 * @author aleksandar
 */
public class InvalidPartnerException extends Exception
{

    public InvalidPartnerException()
    {
        super("Invalid partner");
    }
    
}
