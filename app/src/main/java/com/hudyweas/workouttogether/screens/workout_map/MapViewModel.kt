package com.hudyweas.workouttogether.screens.workout_map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.hudyweas.workouttogether.api.ForecastResponse
import com.hudyweas.workouttogether.model.service.LogService
import com.hudyweas.workouttogether.model.service.StorageService
import com.hudyweas.workouttogether.model.service.WeatherService
import com.hudyweas.workouttogether.screens.WorkoutTogetherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
) : WorkoutTogetherViewModel(logService) {
    val workouts = storageService.workouts
}