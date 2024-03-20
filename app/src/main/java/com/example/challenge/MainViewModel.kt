package com.example.challenge

import android.os.SystemClock.sleep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _activityState = MutableLiveData<ActivityStates>(ActivityStates.Idle)
    val activityState: LiveData<ActivityStates>
        get() = _activityState

    fun login(username: String, password: String) {
        _activityState.value = ActivityStates.Loading
        // Antes no hacía falta aclarar el Dispatchers.IO
        // porque el viewModelScope ya lo tenía por defecto en el lazy
        viewModelScope.launch(Dispatchers.IO) {
            sleep(2000) // Simulamos un proceso de login
            if (username == "Fede" && password == "1234")
                _activityState.postValue(ActivityStates.Success)
            else
                _activityState.postValue(ActivityStates.Error("Usuario o contraseña incorrectos"))
        }
    }

}
