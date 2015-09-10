package com.rorlig.babyapp.otto.auth;

import com.parse.ParseUser;

/**
 * @author gaurav gupta
 */
public class LoginSuccessEvent {
    private final ParseUser user;

    public LoginSuccessEvent(ParseUser user) {
        this.user  = user;
    }

    public ParseUser getUser() {
        return user;
    }
}
