package ru.startandroid.develop.sprint8v3

import ru.startandroid.develop.sprint8v3.data.TracksRepositoryImpl
import ru.startandroid.develop.sprint8v3.data.network.RetrofitNetworkClient
import ru.startandroid.develop.sprint8v3.domain.api.TracksInteractor
import ru.startandroid.develop.sprint8v3.domain.repository.TracksRepository
import ru.startandroid.develop.sprint8v3.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}