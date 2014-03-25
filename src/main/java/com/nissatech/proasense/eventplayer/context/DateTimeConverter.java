package com.nissatech.proasense.eventplayer.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author aleksandar
 */
@Provider
public class DateTimeConverter implements ParamConverterProvider
{

    @Override
    public ParamConverter getConverter(Class type, Type genericType, Annotation[] annotations)
    {
        if (type.equals(DateTime.class))
        {
            return new DateTimeParamConverter();
        }
        else
        {
            return null;
        }

    }
    private static class DateTimeParamConverter implements ParamConverter<DateTime>
    {
        @Override
        public DateTime fromString(String value)
        {
            try
            {
               return DateTime.parse(value);
               
            }
            catch (IllegalArgumentException e)
            {
                return ISODateTimeFormat.dateTime().parseDateTime(value);
            }
        }

        @Override
        public String toString(DateTime value)
        {
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            return fmt.print(value);
        }

    }

}
