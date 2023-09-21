package ru.netology.gpstraker.utils

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.netology.gpstraker.R

fun Fragment.openFragment(f: Fragment) {
    if ((activity as AppCompatActivity).supportFragmentManager.fragments.isNotEmpty()) {
        if ((activity as AppCompatActivity).supportFragmentManager.fragments[0].javaClass == f.javaClass) return
    }
    (activity as AppCompatActivity).supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out
        )
        .replace(R.id.placeHolder, f).commit()
}

fun AppCompatActivity.openFragment(f: Fragment) {
    if (supportFragmentManager.fragments.isNotEmpty()) {
        if (supportFragmentManager.fragments[0].javaClass == f.javaClass) return
    }
    supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
            androidx.appcompat.R.anim.abc_fade_in,
            androidx.appcompat.R.anim.abc_fade_out
        )
        .replace(R.id.placeHolder, f).commit()
}

fun Fragment.makeToast(s: String) {
    Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
}

fun AppCompatActivity.makeToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}

fun Fragment.checkPermission(p: String): Boolean {
    return when(PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(activity as AppCompatActivity, p) -> true
        else -> false
    }
}