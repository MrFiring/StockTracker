package ru.mrfiring.stocktracker

import androidx.lifecycle.LiveData

typealias EmptyLiveData = LiveData<Unit>

class LiveEvent : SingleLiveEvent<Unit>() {

    operator fun invoke() {
        value = Unit
    }
}