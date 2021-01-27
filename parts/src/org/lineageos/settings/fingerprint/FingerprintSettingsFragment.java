/*
 * Copyright (C) 2020 ArrowOS
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

package org.lineageos.settings.fingerprint;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import org.lineageos.settings.R;
import org.lineageos.settings.Constants;

public class FingerprintSettingsFragment extends PreferenceFragment implements
        OnPreferenceChangeListener {

    private final String SCREEN_OFF_FOD_SWITCH_KEY = "screen_off_fod_switch";

    private boolean isScreenOffFodEnabled;
    private SwitchPreference mScreenOffFodSwitch;
    private SharedPreferences mSharedPrefs;
    private int mUserId;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fingerprint_settings);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        mUserId = ActivityManager.getCurrentUser();
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mScreenOffFodSwitch = findPreference(SCREEN_OFF_FOD_SWITCH_KEY);

        if (mUserId != 0) {
            mScreenOffFodSwitch.setEnabled(false);
        } else {
            mScreenOffFodSwitch.setOnPreferenceChangeListener(this);
            mScreenOffFodSwitch.setChecked(SystemProperties.getBoolean("parts.screen_off_fod_enabled", false));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        switch(preference.getKey()) {
            case SCREEN_OFF_FOD_SWITCH_KEY:
                SystemProperties.set("parts.screen_off_fod_enabled", ((boolean) value) ? "true" : "false");
                mSharedPrefs.edit().putBoolean(Constants.SCREEN_OFF_FOD_ENABLE_KEY, (boolean) value).commit();
                return true;
            default:
                return true;
        }
    }
}
