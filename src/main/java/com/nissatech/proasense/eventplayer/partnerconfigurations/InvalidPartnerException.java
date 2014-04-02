package com.nissatech.proasense.eventplayer.partnerconfigurations;


/**
 * Thrown when an non-existing partner is presented to the system. 
 * @author aleksandar
 */
public class InvalidPartnerException extends Exception
{

    public InvalidPartnerException()
    {
        super("Invalid partner");
    }
    
}
