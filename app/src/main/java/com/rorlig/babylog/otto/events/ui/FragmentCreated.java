package com.rorlig.babylog.otto.events.ui;

/**
 * Created by gaurav
 */
public class FragmentCreated {

    private String fragmentTitle;

    public FragmentCreated(String fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
    }


    public String getFragmentTitle() {
        return fragmentTitle;
    }
}
