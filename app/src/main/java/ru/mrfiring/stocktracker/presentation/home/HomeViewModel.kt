package ru.mrfiring.stocktracker.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mrfiring.stocktracker.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _navigateToSearchFragment = SingleLiveEvent<Boolean>()
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    fun navigateToSearch() {
        _navigateToSearchFragment.value = true
    }

}