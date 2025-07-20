package com.nopalsoft.donttap;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nopalsoft.donttap.handlers.FloatFormatter;

public class AndroidLauncher extends AndroidApplication implements FloatFormatter {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new DoNotTapGame(this), config);
    }

    @Override
    public String format(String format, float number) {
        return String.format(format, number);
    }

}
