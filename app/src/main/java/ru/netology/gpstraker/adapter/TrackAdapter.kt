package ru.netology.gpstraker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.gpstraker.R
import ru.netology.gpstraker.databinding.TrackItemBinding
import ru.netology.gpstraker.db.TrackItem

class TrackAdapter(private val listener: Listener) : ListAdapter<TrackItem, TrackAdapter.TrackHolder>(Comparator()) {

    class TrackHolder(view: View, val listener: Listener) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val binding = TrackItemBinding.bind(view)
        private var trackTemp: TrackItem? = null
        init {
            binding.ibDelete.setOnClickListener(this)
            binding.itemCardView.setOnClickListener(this)
        }
        fun bind(track: TrackItem) = with(binding) {
            val speed = "${track.velocity} km/h"
            val distance = "${track.distance} km"
            tvDistance.text = distance
            tvSpeed.text = speed
            tvTime.text = track.time
            tvData.text = track.date
            trackTemp = track
        }

        override fun onClick(v: View) {
            val type = when(v.id) {
                R.id.ibDelete -> ClickType.DELETE
                R.id.itemCardView -> ClickType.OPEN
                else -> ClickType.OPEN
            }
            trackTemp?.let { listener.onClick(it, type) }
        }
    }

    class Comparator : DiffUtil.ItemCallback<TrackItem>() {
        override fun areItemsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrackItem, newItem: TrackItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackHolder(view, listener)
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Listener{
        fun onClick(track: TrackItem, type: ClickType)
    }

    enum class ClickType{
        DELETE,
        OPEN,
    }
}