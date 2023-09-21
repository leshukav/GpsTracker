package ru.netology.gpstraker.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.netology.gpstraker.R


class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var timePref: Preference
    private lateinit var colorPref: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)

        init()

    }

    private fun init() {
        timePref = findPreference<ListPreference>("key_preference")!!
        colorPref = findPreference<ListPreference>("key_color")!!
        val changeListener = onChangeListener()
        timePref.onPreferenceChangeListener = changeListener
        colorPref.onPreferenceChangeListener = changeListener

        val pref = timePref.preferenceManager.sharedPreferences
        //   val prefColor = colorPref.preferenceManager.sharedPreferences

        val trackColor = pref?.getString("key_color", "#FF0D5FEC")
        colorPref.icon?.setTint(Color.parseColor(trackColor))

        val nameArray = resources.getStringArray(R.array.local_time_update)
        val valueArray = resources.getStringArray(R.array.local_time_update_value)
        val title = timePref.title
        timePref.title =
            "$title: ${nameArray[valueArray.indexOf(pref?.getString("key_preference", "3000"))]}"


    }

    private fun onChangeListener(): Preference.OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener { pref, value ->
            when (pref.key) {
                "key_preference" -> onTimeChange(value.toString())
                "key_color" -> colorPref.icon?.setTint(Color.parseColor(value.toString()))
            }
            true
        }
    }

    private fun onTimeChange(value: String) {
        val nameArray = resources.getStringArray(R.array.local_time_update)
        val valueArray = resources.getStringArray(R.array.local_time_update_value)
        val title = timePref.title.toString().substringBefore(":")
        timePref.title = "$title: ${nameArray[valueArray.indexOf(value)]}"
    }

}