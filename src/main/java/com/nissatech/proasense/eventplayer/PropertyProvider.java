package com.nissatech.proasense.eventplayer;

import com.google.inject.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 * Provides configuration from conf.properties.
 * @author aleksandar
 */
public class PropertyProvider implements Provider<Properties>
{

    @Override
    public Properties get()
    {
        try
        {
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("conf.properties");
            Properties props = new Properties();
            props.load(resourceAsStream);
            return props;
        }
        catch (IOException ex)
        {
            LoggerFactory.getILoggerFactory().getLogger(this.getClass().getName()).error(ex.toString());
            return null;
        }
    }
    
}
