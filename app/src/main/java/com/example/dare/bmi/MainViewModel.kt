package com.example.dare.bmi

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.dare.bmi.model.Person
import com.example.dare.bmi.repository.MainRepository
import com.example.dare.state.DataState
import com.example.dare.bmi.util.state.StateEventCalc
import com.example.dare.bmi.model.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    // Factory to Initialize the MainViewModel
    class MainViewModelFactory(
        private val repository: MainRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }

    // Calculate LiveData
    private val _calcEvent = MutableLiveData<DataState<Result>>()
    val calcEvent: LiveData<DataState<Result>>
        get() = _calcEvent

    // Theme LiveData
    private val _themeEvent = MutableLiveData<Int>()
    val themeEvent: LiveData<Int>
        get() = _themeEvent

    fun setStateEvent(person: Person, stateEvent: StateEventCalc) {
        viewModelScope.launch {
            when (stateEvent) {
                is StateEventCalc.StateEvent -> {
                    repository.calc(person).collect { _dataState ->
                        _calcEvent.value = _dataState
                    }
                }
            }
        }
    }

    fun setStateTheme(pref: SharedPreferences, child: Int) {
        _themeEvent.value = repository.setKey(pref, child)
    }
}
