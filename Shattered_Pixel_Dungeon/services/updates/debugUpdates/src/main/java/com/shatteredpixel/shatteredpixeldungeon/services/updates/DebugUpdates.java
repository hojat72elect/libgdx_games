

package com.shatteredpixel.shatteredpixeldungeon.services.updates;


import com.watabou.noosa.Game;

public class DebugUpdates extends UpdateService {

    private static AvailableUpdateData debugUpdateInfo;

    @Override
    public boolean supportsUpdatePrompts() {
        return false; //turn on to debug update prompts
    }

    @Override
    public boolean supportsBetaChannel() {
        return true;
    }

    @Override
    public void checkForUpdate(boolean useMetered, boolean includeBetas, UpdateResultCallback callback) {

        if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()) {
            callback.onConnectionFailed();
            return;
        }

        debugUpdateInfo = new AvailableUpdateData();
        debugUpdateInfo.versionCode = Game.versionCode + 1;
        debugUpdateInfo.URL = "http://www.google.com";

        callback.onUpdateAvailable(debugUpdateInfo);
    }

    @Override
    public void initializeUpdate(AvailableUpdateData update) {
        Game.platform.openURI(update.URL);
    }

    @Override
    public boolean supportsReviews() {
        return false; //turn on to debug review prompts
    }

    @Override
    public void initializeReview(ReviewResultCallback callback) {
        //does nothing
        callback.onComplete();
    }

    @Override
    public void openReviewURI() {
        Game.platform.openURI("https://www.google.com/");
    }
}
