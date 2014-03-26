package com.nissatech.proasense.eventplayer;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nissatech.proasense.eventplayer.context.ContextListener;
import com.nissatech.proasense.eventplayer.context.DateTimeConverter;
import com.nissatech.proasense.eventplayer.context.JacksonConfigurator;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfigurationResolver;

/**
 *
 * @author aleksandar
 */
public class GuiceModule implements Module
{

    @Override
    public void configure(Binder binder)
    {
        binder.bind(EventPlayer.class);
        binder.bind(BatchEvents.class);
        binder.bind(ContextListener.class);
        binder.bind(JacksonConfigurator.class);
        binder.bind(DateTimeConverter.class);
        binder.bind(PartnerConfigurationResolver.class);
    }
    
}
