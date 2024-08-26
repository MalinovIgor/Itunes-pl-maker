package ru.startandroid.develop.sprint8v3.player.data.repository

import android.content.Intent
import ru.startandroid.develop.sprint8v3.domain.models.Track
import ru.startandroid.develop.sprint8v3.player.domain.repository.PlayerRepository
import ru.startandroid.develop.sprint8v3.ui.player.selectedTrack

class PlayerRepositoryImpl:PlayerRepository {
    override fun getTrackFromIntent(intent: Intent): Track? {
        return intent.getSerializableExtra(selectedTrack) as? Track
    }
}