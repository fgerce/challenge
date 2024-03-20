package com.example.challenge

sealed class ActivityStates {
    object Idle: ActivityStates()
    object Loading: ActivityStates()
    object Success: ActivityStates()
    data class Error(val error: String): ActivityStates()
}
