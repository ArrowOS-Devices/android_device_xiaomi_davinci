/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2019 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.PreferenceManager;

import static android.provider.Settings.Secure.DOZE_ALWAYS_ON;
import static android.provider.Settings.Secure.DOZE_ENABLED;

public final class DozeUtils {
    private static final String TAG = "DozeUtils";
    private static final boolean DEBUG = false;

    private static final String DOZE_INTENT = "com.android.systemui.doze.pulse";

    public static final String ALWAYS_ON_DISPLAY = "always_on_display";

    public static final String CATEG_PICKUP_SENSOR = "pickup_sensor";
    public static final String CATEG_PROX_SENSOR = "proximity_sensor";

    public static final String GESTURE_PICK_UP_KEY = "gesture_pick_up";
    public static final String GESTURE_RAISE_TO_WAKE_KEY = "gesture_raise_to_wake";
    public static final String GESTURE_HAND_WAVE_KEY = "gesture_hand_wave";
    public static final String GESTURE_POCKET_KEY = "gesture_pocket";
    public static final String GESTURE_RAISE_TO_WAKE = "gesture_raise_to_wake";

    public static boolean getProxCheckBeforePulse(Context context) {
        try {
            Context con = context.createPackageContext("com.android.systemui", 0);
            int id = con.getResources().getIdentifier(
                    "doze_proximity_check_before_pulse", "bool", "com.android.systemui");
            return con.getResources().getBoolean(id);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean enableDoze(Context context, boolean enable) {
        return Settings.Secure.putInt(context.getContentResolver(), DOZE_ENABLED, enable ? 1 : 0);
    }

    public static boolean isDozeEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), DOZE_ENABLED, 1) != 0;
    }

    public static void launchDozePulse(Context context) {
        if (DEBUG)
            Log.d(TAG, "Launch doze pulse");
        context.sendBroadcastAsUser(
                new Intent(DOZE_INTENT), new UserHandle(UserHandle.USER_CURRENT));
    }

    public static boolean enableAlwaysOn(Context context, boolean enable) {
        return Settings.Secure.putIntForUser(context.getContentResolver(), DOZE_ALWAYS_ON,
                enable ? 1 : 0, UserHandle.USER_CURRENT);
    }

    public static boolean isAlwaysOnEnabled(Context context) {
        final boolean enabledByDefault = context.getResources().getBoolean(
                com.android.internal.R.bool.config_dozeAlwaysOnEnabled);

        return Settings.Secure.getIntForUser(context.getContentResolver(), DOZE_ALWAYS_ON,
                       alwaysOnDisplayAvailable(context) && enabledByDefault ? 1 : 0,
                       UserHandle.USER_CURRENT)
                != 0;
    }

    public static boolean alwaysOnDisplayAvailable(Context context) {
        return new AmbientDisplayConfiguration(context).alwaysOnAvailable();
    }

    public static boolean isGestureEnabled(Context context, String gesture) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(gesture, false);
    }

    public static boolean isPickUpEnabled(Context context) {
        return isGestureEnabled(context, GESTURE_PICK_UP_KEY);
    }

    public static boolean isRaiseToWakeEnabled(Context context) {
        return isGestureEnabled(context, GESTURE_RAISE_TO_WAKE_KEY);
    }

    public static boolean isHandwaveGestureEnabled(Context context) {
        return isGestureEnabled(context, GESTURE_HAND_WAVE_KEY);
    }

    public static boolean isPocketGestureEnabled(Context context) {
        return isGestureEnabled(context, GESTURE_POCKET_KEY);
    }

    public static boolean sensorsEnabled(Context context) {
        return isPickUpEnabled(context) || isHandwaveGestureEnabled(context)
                || isPocketGestureEnabled(context);
    }

    public static Sensor getSensor(SensorManager sm, String type) {
        for (Sensor sensor : sm.getSensorList(Sensor.TYPE_ALL)) {
            if (type.equals(sensor.getStringType())) {
                return sensor;
            }
        }
        return null;
    }
}
