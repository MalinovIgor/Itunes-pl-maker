package ru.startandroid.develop.sprint8v3.library.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.startandroid.develop.sprint8v3.library.domain.api.PlaylistInteractor
import ru.startandroid.develop.sprint8v3.library.domain.model.Playlist
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistsViewModel(
    private val interactor: PlaylistInteractor,
    private val application: Application
) : ViewModel() {

    private val playlists = MutableLiveData<List<Playlist>>()
    fun observePlaylists(): LiveData<List<Playlist>> = playlists

    fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri,
        bitmap: Bitmap,
    ) {
        var playlistImage: String? = null
        if (imageUri != Uri.EMPTY)
            playlistImage = "${UUID.randomUUID()}.png"
        viewModelScope.launch(Dispatchers.IO) {
            interactor.createPlaylist(playlistName, playlistDescription, playlistImage)

            playlistImage?.let {
                saveImageToPrivateStorage(bitmap, it)
            }
        }
    }

    fun returnPlaylists() {
        viewModelScope.launch (Dispatchers.IO){
            interactor.getPlaylists().collect { trackList ->
                playlists.postValue(trackList)
            }
        }
    }

    private fun saveImageToPrivateStorage(bitmap: Bitmap, fileName: String): Boolean {
        if (fileName.isEmpty()) return false
        val filePath =
            File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "cache")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, fileName)
        val outputStream = FileOutputStream(file)
        return bitmap
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    init {
        returnPlaylists()
    }
}