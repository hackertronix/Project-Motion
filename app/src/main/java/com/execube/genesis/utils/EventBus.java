package com.execube.genesis.utils;

import com.squareup.otto.Bus;

/**
 * Created by Prateek Phoenix on 6/9/2016.
 */
public class EventBus {
    private static Bus bus = new Bus();

    private EventBus()
    {

    }

    public static Bus getBus()
    {
        return bus;
    }
}
