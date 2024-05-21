package com.hudyweas.workouttogether.screens.workoutdetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hudyweas.workouttogether.TASK_ID
import com.hudyweas.workouttogether.api.ForecastResponse
import com.hudyweas.workouttogether.common.ext.idFromParameter
import com.hudyweas.workouttogether.model.Workout
import com.hudyweas.workouttogether.model.service.LogService
import com.hudyweas.workouttogether.model.service.StorageService
import com.hudyweas.workouttogether.screens.WorkoutTogetherViewModel
import com.hudyweas.workouttogether.model.service.WeatherService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val weatherService: WeatherService
): WorkoutTogetherViewModel(logService){
    val workout = mutableStateOf(Workout())
    val weatherResponse = mutableStateOf(ForecastResponse(null, null, null, null, null, null, null, null, null))
    init {
        val workoutId = savedStateHandle.get<String>(TASK_ID)
        viewModelScope.launch {
            try {
                if (workoutId != null) {
                    workout.value = storageService.getWorkout(workoutId.idFromParameter()) ?: Workout()
                }
            } catch (e: Exception) {
                print("Error getting workout")
            }
            try {
                weatherResponse.value = weatherService.sendGet(workout.value.city, workout.value.date)

            } catch (e: Exception) {
                print("Error getting weather data")
                print(e.message)
            }
        }
    }
}