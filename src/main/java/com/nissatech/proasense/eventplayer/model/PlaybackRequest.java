package com.nissatech.proasense.eventplayer.model;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.joda.time.DateTime;

/**
 *
 * @author aleksandar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaybackRequest
{
    @NotNull
    private DateTime startTime;
    
    @NotNull
    private DateTime endTime;
    
    @NotNull
    private List<String> variables;

    public PlaybackRequest()
    {
        this.variables = new ArrayList<String>();
    }

    public List<String> getVariables()
    {
        return variables;
    }

    public void setVariables(List<String> variables)
    {
        this.variables = variables;
    }

    public DateTime getStartTime()
    {
        return startTime;
    }

    public void setStartTime(DateTime startTime)
    {
        this.startTime = startTime;
    }

    public DateTime getEndTime()
    {
        return endTime;
    }

    public void setEndTime(DateTime endTime)
    {
        this.endTime = endTime;
    }
    
    
}
