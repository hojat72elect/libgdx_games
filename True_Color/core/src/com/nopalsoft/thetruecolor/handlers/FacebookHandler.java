package com.nopalsoft.thetruecolor.handlers;

public interface FacebookHandler {

    void facebookSignIn();

    void facebookSignOut();

    boolean facebookIsSignedIn();

    void facebookShareFeed(final String message);

    void showFacebook();

    void facebookGetScores();

    void facebookSubmitScore(final int score);

    void facebookInviteFriends(final String message);
}
