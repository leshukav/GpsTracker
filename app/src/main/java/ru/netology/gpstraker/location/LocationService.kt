package ru.netology.gpstraker.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import org.osmdroid.util.GeoPoint
import ru.netology.gpstraker.MainActivity
import ru.netology.gpstraker.R


class LocationService : Service() {

    companion object {
        const val CHANEL_ID = "chanel_1"
        var isRunning = false
        var timeStart = 0L
        const val LOC_MODEL_INTENT = "loc_model"
    }

    private lateinit var geoPointList: ArrayList<GeoPoint>
    private var distance = 0.0f
    private var lastLocation: Location? = null
    private lateinit var locRequest: LocationRequest
    private lateinit var locProvider: FusedLocationProviderClient
    private val locCallback = object : LocationCallback(){
        override fun onLocationResult(locResult: LocationResult) {
            super.onLocationResult(locResult)
            val currentLocation = locResult.lastLocation
            if (lastLocation != null && currentLocation != null) {
               // if (currentLocation.speed > 0.2)
                    distance += lastLocation?.distanceTo(currentLocation)
                    ?: 0.0f
                geoPointList.add(GeoPoint(currentLocation.latitude, currentLocation.longitude))
                val locModel = LocationModel(
                    currentLocation.speed,
                    distance,
                    geoPointList
                )
                sendLocModel(locModel)
            }
            lastLocation = currentLocation

            Log.d("MyTag", "Location ${distance}")
        }
    }

    private fun sendLocModel(locModel: LocationModel){
        val i = Intent(LOC_MODEL_INTENT).apply {
            putExtra(LOC_MODEL_INTENT, locModel)
        }
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(i)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        startNotification()
        startLocationUpdates()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        initLocation()
        geoPointList = ArrayList()
        Log.d("MyTag", "Service onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        locProvider.removeLocationUpdates(locCallback)
        Log.d("MyTag", "Service onDestroy")
    }

    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nChanel = NotificationChannel(
                CHANEL_ID,
                "Location Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val nManager = getSystemService(NotificationManager::class.java) as NotificationManager
            nManager.createNotificationChannel(nChanel)
        }
        val nIntent = Intent(this, MainActivity::class.java)
        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            else -> FLAG_UPDATE_CURRENT
        }
        val pIntent = PendingIntent.getActivity(this, 10, nIntent, flags)

        val notification = NotificationCompat.Builder(
            this,
            CHANEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("tracker running")
            .setContentIntent(pIntent).build()

        startForeground(99, notification)

    }

    private fun initLocation(){
        locRequest = LocationRequest.create()
        locRequest.apply {
            interval = 5000
            priority = PRIORITY_HIGH_ACCURACY
            fastestInterval = 5000
        }
      //  locRequest.interval = 5000
      //  locRequest.priority = PRIORITY_HIGH_ACCURACY
        locProvider = LocationServices.getFusedLocationProviderClient(baseContext)
    }

    private fun startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        locProvider.requestLocationUpdates(
            locRequest,
            locCallback,
            Looper.myLooper()
        )
    }

}