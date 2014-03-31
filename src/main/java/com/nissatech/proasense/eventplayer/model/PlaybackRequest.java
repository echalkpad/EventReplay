package com.nissatech.proasense.eventplayer.model;


import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

/**
 *
 * @author aleksandar
 */

public class PlaybackRequest
{

    @NotNull
    private DateTime startTime;

    @NotNull
    private DateTime endTime;

    @Null
    private DateTime submitted;

    @NotBlank
    private String topic;
    
    @NotBlank
    private String partner;

    public String getPartner()
    {
        return partner;
    }

    public void setPartner(String partner)
    {
        this.partner = partner;
    }
    
    @NotNull
    @NotEmpty
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

     public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
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

    public DateTime getSubmitted()
    {
        return submitted;
    }

    public void setSubmitted(DateTime submitted)
    {
        this.submitted = submitted;
    }

}
