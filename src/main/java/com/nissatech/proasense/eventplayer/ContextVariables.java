/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nissatech.proasense.eventplayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author aleksandar
 */
public class ContextVariables
{
    private static final Map jobs = new ConcurrentHashMap<String,AsyncRequestWorker>();
    
    public static Map getJobs()
    {
        return jobs;
    }
}
