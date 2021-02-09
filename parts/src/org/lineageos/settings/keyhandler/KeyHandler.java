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
import android.os.PowerManager;
import android.os.SystemProperties;
import android.view.KeyEvent;

import com.android.internal.os.DeviceKeyHandler;

import org.lineageos.settings.utils.DozeUtils;
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
        boolean isDozeEnabled = DozeUtils.isDozeEnabled(mContext);
        boolean isScreenOn = isScreenOn(mContext);

        if (DEBUG)
            Log.i(TAG, "handleKeyEvent=" + scanCode);

        switch (scanCode) {
            case KEYCODE_FOD:
                if (!DozeUtils.isAlwaysOnEnabled(mContext)
                    && isScreenOffFodEnabled
                    && (userId == 0)
                    && !isScreenOn) {
                    if (DEBUG)
                        Log.d(TAG, "Pulsing the screen as screen off FOD is enabled");

                    // Enable doze if it isn't enabled so the pulse can be executed
                    if (!isDozeEnabled) {
                        DozeUtils.enableDoze(mContext, true);
                    }

                    DozeUtils.launchDozePulse(mContext);

                    // Restore the doze status to the original value
                    DozeUtils.enableDoze(mContext, isDozeEnabled);
                }
                return event;
            default:
                return event;
        }
    }

    private boolean isScreenOn(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return powerManager.isInteractive();
    }
}
