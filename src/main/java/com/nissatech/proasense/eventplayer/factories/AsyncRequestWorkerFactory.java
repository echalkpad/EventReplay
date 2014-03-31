package com.nissatech.proasense.eventplayer.factories;

import com.nissatech.proasense.eventplayer.AsyncRequestWorker;
import com.nissatech.proasense.eventplayer.model.PlaybackRequest;
import com.nissatech.proasense.eventplayer.partnerconfigurations.PartnerConfiguration;

/**
 *
 * @author aleksandar
 */
public interface AsyncRequestWorkerFactory
{
   AsyncRequestWorker create(PlaybackRequest request, String id, PartnerConfiguration conf) ;
}
