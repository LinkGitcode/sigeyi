package com.sigeyi.csc.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.sigeyi.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
	public static final String SETTINGS_WHEEL_SIZE = "settings_wheel_size";
	public static final int SETTINGS_WHEEL_SIZE_DEFAULT = 2340;
	public static final String SETTINGS_UNIT = "settings_csc_unit";
	public static final int SETTINGS_UNIT_M_S = 0; // [m/s]
	public static final int SETTINGS_UNIT_KM_H = 1; // [m/s]
	public static final int SETTINGS_UNIT_MPH = 2; // [m/s]
	public static final int SETTINGS_UNIT_DEFAULT = SETTINGS_UNIT_KM_H;


	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings_csc);

		// set initial values
		updateWheelSizeSummary();
	}

	@Override
	public void onResume() {
		super.onResume();

		// attach the preference change listener. It will update the summary below interval preference
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();

		// unregister listener
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		if (SETTINGS_WHEEL_SIZE.equals(key)) {
			updateWheelSizeSummary();
		}
	}

	private void updateWheelSizeSummary() {
		final PreferenceScreen screen = getPreferenceScreen();
		final SharedPreferences preferences = getPreferenceManager().getSharedPreferences();

		final String value = preferences.getString(SETTINGS_WHEEL_SIZE, String.valueOf(SETTINGS_WHEEL_SIZE_DEFAULT));
		screen.findPreference(SETTINGS_WHEEL_SIZE).setSummary(getString(R.string.csc_settings_wheel_diameter_summary, value));
	}
}
