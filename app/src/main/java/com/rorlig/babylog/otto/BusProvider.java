package com.rorlig.babylog.otto;

import com.squareup.otto.Bus;

/**
 * Created by gaurav
 */
public class BusProvider {
    private static final Bus sBus = new Bus();

    private BusProvider()
    {
        throw new UnsupportedOperationException();
    }

    public static Bus getInstance()
    {
        return sBus;
    }
}
