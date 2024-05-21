

package com.hudyweas.workouttogether.screens.edit_workout

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.hudyweas.workouttogether.TASK_ID
import com.hudyweas.workouttogether.common.ext.idFromParameter
import com.hudyweas.workouttogether.model.Workout
import com.hudyweas.workouttogether.model.service.LogService
import com.hudyweas.workouttogether.model.service.StorageService
import com.hudyweas.workouttogether.screens.WorkoutTogetherViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditWorkoutViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  logService: LogService,
  private val storageService: StorageService,
) : WorkoutTogetherViewModel(logService) {
  val workout = mutableStateOf(Workout())

  init {
    val workoutId = savedStateHandle.get<String>(TASK_ID)
    if (workoutId != null) {
      launchCatching {
        workout.value = storageService.getWorkout(workoutId.idFromParameter()) ?: Workout()
      }
    }
  }

  fun onTitleChange(newValue: String) {
    workout.value = workout.value.copy(title = newValue)
  }

  fun onDescriptionChange(newValue: String) {
    workout.value = workout.value.copy(description = newValue)
  }

  fun onAddressCountryChange(newValue: String) {
    workout.value = workout.value.copy(country = newValue)
  }
  fun onAddressCityChange(newValue: String) {
    workout.value = workout.value.copy(city = newValue)
  }

  fun onStreetChange(newValue: String) {
    workout.value = workout.value.copy(street = newValue)
  }

  fun onBuildingNumberChange(newValue: String) {
    workout.value = workout.value.copy(buildingNumber = newValue)
  }

  fun onIconChange(newValue: Int) {
    workout.value = workout.value.copy(icon = newValue)
  }

  fun onDateChange(newValue: Long) {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
    calendar.timeInMillis = newValue
    val newDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
    workout.value = workout.value.copy(date = newDate)
  }

  fun onTimeChange(hour: Int, minute: Int) {
    val newTime = "${hour.toClockPattern()}:${minute.toClockPattern()}"
    workout.value = workout.value.copy(time = newTime)
  }

  fun onDoneClick(popUpScreen: () -> Unit) {
    launchCatching {
      val editedTask = workout.value
      if (editedTask.id.isBlank()) {
        storageService.save(editedTask)
      } else {
        storageService.update(editedTask)
      }
      popUpScreen()
    }
  }

  private fun Int.toClockPattern(): String {
    return if (this < 10) "0$this" else "$this"
  }

  companion object {
    private const val UTC = "UTC"
    private const val DATE_FORMAT = "EEE, d MMM yyyy"
  }

  ////////////////

//  sealed class ResponseState<out T> {
//    object Idle : ResponseState<Nothing>()
//    object Loading : ResponseState<Nothing>()
//    data class Error(val error: Throwable) : ResponseState<Nothing>()
//    data class Success<R>(val data: R) : ResponseState<R>()
//  }
//
//  private val _markerAddressDetail = MutableStateFlow<ResponseState<Address>>(ResponseState.Idle)
//  val markerAddressDetail = _markerAddressDetail.asStateFlow()
//
//  fun getMarkerAddressDetails(lat: Double, long: Double, context: Context) {
//    _markerAddressDetail.value = ResponseState.Loading
//    try {
//      val geocoder = Geocoder(context, Locale.getDefault())
//      if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        geocoder.getFromLocation(
//          lat,
//          long,
//          1,
//        ) { p0 ->
//          _markerAddressDetail.value = ResponseState.Success(p0[0])
//        }
//      } else {
//        val addresses = geocoder.getFromLocation(
//          lat,
//          long,
//          1,
//        )
//
//        if(!addresses.isNullOrEmpty())
//          workout.value = workout.value.copy(address = addresses[0])
//
//        _markerAddressDetail.value =
//          if(!addresses.isNullOrEmpty()){
//            ResponseState.Success(addresses[0])
//
//
//          }else{
//            ResponseState.Error(Exception("Address is null"))
//          }
//      }
//    } catch (e: Exception) {
//      _markerAddressDetail.value = ResponseState.Error(e)
//    }
//  }
}
