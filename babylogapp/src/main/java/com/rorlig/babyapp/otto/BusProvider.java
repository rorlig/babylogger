package com.rorlig.babyapp.otto;

import com.squareup.otto.Bus;

/**
 * Created by gaurav
 */
public class BusProvider {
    private static final MainThreadBus sBus = new MainThreadBus();

    private BusProvider()
    {
        throw new UnsupportedOperationException();
    }

    public static Bus getInstance()
    {
        return sBus;
    }
}
