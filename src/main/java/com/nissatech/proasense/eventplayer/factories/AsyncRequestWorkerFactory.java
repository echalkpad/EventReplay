package com.nissatech.proasense.eventplayer.factories;

import com.nissatech.proasense.eventplayer.AsyncRequestWorker;
import com.nissatech.proasense.eventplayer.model.PlaybackRequest;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfiguration;

/**
 * Guice-style factory interface
 * @author aleksandar
 */
public interface AsyncRequestWorkerFactory
{
    /**
     * Creates a playback worker/thread based on the inputs from the client.
     * @param request Client's request
     * @param id Unique id of the worker (typically a UUID)
     * @param conf Client configuration based on he client's request
     * @return The configured worker 
     */
   AsyncRequestWorker create(PlaybackRequest request, String id, PartnerConfiguration conf) ;
}
