package ru.netology.gpstraker.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import ru.netology.gpstraker.R
import ru.netology.gpstraker.databinding.DialogSaveBinding
import ru.netology.gpstraker.db.TrackItem

object DialogManeger {
    fun showLocEnebleDialog(context: Context, listener: Listener){
        val build = AlertDialog.Builder(context)
        val dialog = build.create()
        dialog.setTitle(context.getString(R.string.location_disabled))
        dialog.setMessage(context.getString(R.string.location_dialog_message))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes"){
            _,_ -> listener.onClick()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No"){
                _,_ ->
            Toast.makeText(context, "No", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showSaveDialog(context: Context, item: TrackItem?, listener: Listener){
        val build = AlertDialog.Builder(context)
        val binding = DialogSaveBinding.inflate(LayoutInflater.from(context), null, false)
        build.setView(binding.root)
        val dialog = build.create()
        binding.apply {
            val time  = "${item?.time}"
            tvtime.text = time
           val distance = "${item?.distance} km"
            tvDinstance.text = distance
            val velocity = "${item?.velocity} km/h"
            tvSpeed.text = velocity
            btSave.setOnClickListener {
                listener.onClick()
                dialog.dismiss()
            }
            btCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    interface Listener{
        fun onClick()
    }
}