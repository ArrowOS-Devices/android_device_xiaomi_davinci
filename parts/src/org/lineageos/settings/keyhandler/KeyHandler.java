/*
 * Copyright (C) 2021 The LineageOS Project
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.keyhandler;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.SystemProperties;
import android.view.KeyEvent;

import com.android.internal.os.DeviceKeyHandler;

import org.lineageos.settings.doze.DozeUtils;
import org.lineageos.settings.Constants;

public class KeyHandler implements DeviceKeyHandler {

    private static final boolean DEBUG = true;
    private static final String TAG = "KeyHandler";
    private static final int KEYCODE_FOD = 338;

    private Context mContext;

    public KeyHandler(Context context) {
        if (DEBUG)
            Log.i(TAG, "KeyHandler constructor called");

        mContext = context;
    }

    public KeyEvent handleKeyEvent(KeyEvent event) {
        int scanCode = event.getScanCode();
        int userId = ActivityManager.getCurrentUser();
        boolean isScreenOffFodEnabled = SystemProperties.getBoolean(Constants.SCREEN_OFF_FOD_ENABLE_PROP, false);

        if (DEBUG)
            Log.i(TAG, "handleKeyEvent=" + scanCode);

        switch (scanCode) {
            case KEYCODE_FOD:
                if (!DozeUtils.isAlwaysOnEnabled(mContext)
                    && isScreenOffFodEnabled
                    && (userId == 0)) {
                    if (DEBUG)
                        Log.d(TAG, "Pulsing the screen as screen off FOD is enabled");
                    DozeUtils.launchDozePulse(mContext);
                }
                return event;
            default:
                return event;
        }
    }
}
