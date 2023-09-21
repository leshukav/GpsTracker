package ru.netology.gpstraker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.netology.gpstraker.databinding.ActivityMainBinding
import ru.netology.gpstraker.fragment.MainFragment
import ru.netology.gpstraker.fragment.SettingsFragment
import ru.netology.gpstraker.fragment.TracksFragment
import ru.netology.gpstraker.utils.makeToast
import ru.netology.gpstraker.utils.openFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomMenuClicks()
        openFragment(MainFragment.newInstance())
    }

    private fun bottomMenuClicks(){
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    openFragment(MainFragment.newInstance())
                    makeToast("Main Fragment")
                }
                R.id.tracks -> {
                    openFragment(TracksFragment.newInstance())
                    makeToast("Track Fragment")
                }
                R.id.settings -> {
                    openFragment(SettingsFragment())
                    makeToast("Settings Fragment")
                }
            }
            true
        }
    }
}