package com.nissatech.proasense.eventplayer.partnerconfigurations;

import com.google.inject.Inject;

/**
 *
 * @author aleksandar
 */
public class PartnerConfigurationResolver 
{

    @Inject
    public PartnerConfigurationResolver()
    {
    }

    public PartnerConfiguration getConfiguration(String partner) throws InvalidPartnerException
    {
        if(partner.equalsIgnoreCase("aker"))
            return new AkerConfiguration();
        if(partner.equalsIgnoreCase("hella"))
            return new HellaConfiguration();
        throw new InvalidPartnerException();
    }
    
}
