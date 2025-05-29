package com.nopalsoft.sharkadventure.handlers;

public interface FacebookHandler {

    void facebookSignIn();

    void facebookSignOut();

    boolean facebookIsSignedIn();

    void facebookShareFeed(final String message);

    void showFacebook();

    void facebookInviteFriends(final String message);
}
