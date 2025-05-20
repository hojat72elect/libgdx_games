package com.nopalsoft.superjumper.handlers;

public interface FacebookHandler {

	public void facebookSignIn();

	public void facebookSignOut();

	public boolean facebookIsSignedIn();

	public void facebookShareFeed(final String message);

	public void showFacebook();

	public void facebookInviteFriends(final String message);

}
