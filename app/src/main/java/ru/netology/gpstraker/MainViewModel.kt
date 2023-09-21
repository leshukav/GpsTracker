package ru.netology.gpstraker

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.gpstraker.adapter.TrackAdapter
import ru.netology.gpstraker.db.MainDb
import ru.netology.gpstraker.db.TrackItem
import ru.netology.gpstraker.location.LocationModel

@Suppress("UNCHECKED_CAST")
class MainViewModel(db: MainDb): ViewModel() {
    val dao = db.getDao()
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
    val track = MutableLiveData<TrackItem>()
    val tracks = dao.getAllTracks().asLiveData()

    fun insertTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.insertTrack(trackItem)
    }

    fun  deleteTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.deleteTrack(trackItem)
    }

    class ViewModelFactory(private val db: MainDb): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(db) as T
            }
            throw java.lang.IllegalArgumentException("Unknown ViewModel class ")
        }
    }
}