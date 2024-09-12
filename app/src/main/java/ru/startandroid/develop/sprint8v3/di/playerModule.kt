package ru.startandroid.develop.sprint8v3.di

import android.media.MediaPlayer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.startandroid.develop.sprint8v3.player.domain.api.PlayerInteractor
import ru.startandroid.develop.sprint8v3.player.domain.impl.PlayerInteractorImpl
import ru.startandroid.develop.sprint8v3.player.ui.PlayerActivityViewModel

val playerModule =module{

    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }

    factory <MediaPlayer>{MediaPlayer()}

    viewModel {
        PlayerActivityViewModel(get())
    }
}