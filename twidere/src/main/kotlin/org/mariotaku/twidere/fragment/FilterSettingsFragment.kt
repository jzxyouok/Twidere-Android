package org.mariotaku.twidere.fragment

import android.os.Bundle
import org.mariotaku.twidere.Constants.SHARED_PREFERENCES_NAME
import org.mariotaku.twidere.R

class FilterSettingsFragment : BasePreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SHARED_PREFERENCES_NAME
        addPreferencesFromResource(R.xml.preferences_filters)
    }

}