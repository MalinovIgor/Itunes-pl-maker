package ru.startandroid.develop.sprint8v3.player.domain.repository

import android.content.Intent
import ru.startandroid.develop.sprint8v3.domain.models.Track

interface PlayerRepository {
    fun getTrackFromIntent (intent: Intent) : Track?
}