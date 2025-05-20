package com.nopalsoft.ninjarunner.handlers;

public interface FacebookHandler {

    public void facebookSignIn();

    public void facebookSignOut();

    public boolean facebookIsSignedIn();

    public void facebookGetScores();

    public void facebookSubmitScore(final long score);

//	public void showFacebook();

    public void facebookInviteFriends(final String message);

}
