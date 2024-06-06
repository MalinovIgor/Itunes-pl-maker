package ru.startandroid.develop.sprint8v3

open class Observable {
    private val observers = mutableListOf<Observer>()

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun notifyObservers() {
        for (observer in observers) {
            observer.update()
        }
    }
}