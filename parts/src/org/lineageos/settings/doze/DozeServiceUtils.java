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

package org.lineageos.settings.doze;

import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;

import org.lineageos.settings.utils.DozeUtils;

public final class DozeServiceUtils {
    private static final String TAG = "DozeServiceUtils";
    private static final boolean DEBUG = false;

    public static void startService(Context context) {
        if (DEBUG)
            Log.d(TAG, "Starting service");
        context.startServiceAsUser(new Intent(context, DozeService.class), UserHandle.CURRENT);
    }

    protected static void stopService(Context context) {
        if (DEBUG)
            Log.d(TAG, "Stopping service");
        context.stopServiceAsUser(new Intent(context, DozeService.class), UserHandle.CURRENT);
    }

    public static void checkDozeService(Context context) {
        if (DozeUtils.isDozeEnabled(context) && !DozeUtils.isAlwaysOnEnabled(context) && DozeUtils.sensorsEnabled(context)) {
            startService(context);
        } else {
            stopService(context);
        }
    }
}
