package ru.netology.gpstraker.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.netology.gpstraker.MainViewModel
import ru.netology.gpstraker.R
import ru.netology.gpstraker.application.MainApp
import ru.netology.gpstraker.databinding.FragmentTracksBinding
import ru.netology.gpstraker.databinding.FragmentViewTrackBinding

class ViewTrackFragment : Fragment() {
    private var startPoint: GeoPoint? =null
    lateinit var binding: FragmentViewTrackBinding
    private val model: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).dataBase)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewTrackBinding.inflate(inflater, container, false)
        return binding.root
        settingsOsm()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOSM()
        getTrack()
        binding.fCenter.setOnClickListener {
            startPoint?.let { binding.mapTrack.controller.animateTo(it) }
        }
    }

    private fun getTrack(){
        model.track.observe(viewLifecycleOwner){track ->
            with(binding){
                val distance = "Distance: ${track.distance} Km"
                val averageVelocity = "Average Speed: ${track.velocity} Km/h"
                tvData.text = track.date
                tvDistance.text = distance
                tvTime.text = track.time
                tvAverage.text = averageVelocity
                val polyline = getPolyline(track.geoPoint)
                mapTrack.overlays.add(polyline)
                setMarkers(polyline.actualPoints)
                goToStartPosition(polyline.actualPoints[0])
                startPoint = polyline.actualPoints[0]
            }
        }
    }

    private fun goToStartPosition(startPosition: GeoPoint){
        binding.mapTrack.controller.setZoom(15.0)
        binding.mapTrack.controller.animateTo(startPosition)
    }

    private fun setMarkers(list: List<GeoPoint>) = with(binding){
        val startMarker = Marker(mapTrack)
        val finishMarker = Marker(mapTrack)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        finishMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.icon = getDrawable(requireContext(),R.drawable.ic_start)
        finishMarker.icon = getDrawable(requireContext(),R.drawable.ic_finish)
        startMarker.position = list[0]
        finishMarker.position = list[list.size - 1]
        mapTrack.overlays.add(startMarker)
        mapTrack.overlays.add(finishMarker)
    }

    private fun getPolyline(geoPoint: String): Polyline{
        val polyline = Polyline()
        polyline.outlinePaint.color = Color.parseColor(
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getString("key_color", "#FF0D5FEC")
        )
        val list = geoPoint.split("/")
        list.forEach {
            if (it.isEmpty()) return@forEach
            val point = it.split(",")
            polyline.addPoint(GeoPoint(point[0].toDouble(), point[1].toDouble()))
        }
        return polyline
    }

    private fun initOSM() = with(binding) {
      /*  val mLocProvider = GpsMyLocationProvider(activity)
        val mLocOverlay = MyLocationNewOverlay(mLocProvider, mapTrack)
        mLocOverlay.enableMyLocation()
        mLocOverlay.enableFollowLocation()
        mLocOverlay.runOnFirstFix {
            mapTrack.overlays.clear()
            mapTrack.overlays.add(mLocOverlay)
            mapTrack.overlays.add(pl)
        }

       */
    }


    private fun settingsOsm() {
        Configuration.getInstance().load(
            (activity as AppCompatActivity),
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

    }

    companion object {

        @JvmStatic
        fun newInstance() = ViewTrackFragment()

    }
}