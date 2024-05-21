

package com.hudyweas.workouttogether.screens.edit_workout

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.hudyweas.workouttogether.R.drawable as AppIcon
import com.hudyweas.workouttogether.R.string as AppText
import com.hudyweas.workouttogether.common.composable.*
import com.hudyweas.workouttogether.common.ext.card
import com.hudyweas.workouttogether.common.ext.fieldModifier
import com.hudyweas.workouttogether.common.ext.spacer
import com.hudyweas.workouttogether.common.ext.toolbarActions
import com.hudyweas.workouttogether.model.Workout
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

@Composable
fun EditWorkoutScreen(
  popUpScreen: () -> Unit,
  viewModel: EditWorkoutViewModel = hiltViewModel()
) {
  val workout by viewModel.workout
  val activity = LocalContext.current as AppCompatActivity

  EditWorkoutsScreenContent(
    workout = workout,
    onDoneClick = { viewModel.onDoneClick(popUpScreen) },
    onTitleChange = viewModel::onTitleChange,
    onDescriptionChange = viewModel::onDescriptionChange,
    onDateChange = viewModel::onDateChange,
    onIconChange = viewModel::onIconChange,
    onTimeChange = viewModel::onTimeChange,
    onCountryChange = viewModel::onAddressCountryChange,
    onCityChange = viewModel::onAddressCityChange,
    onStreetChange = viewModel::onStreetChange,
    onBuildingNumberChange = viewModel::onBuildingNumberChange,
//    getMarkerAddress = viewModel::getMarkerAddressDetails,
    activity = activity
  )
}

@Composable
fun EditWorkoutsScreenContent(
  modifier: Modifier = Modifier,
  workout: Workout,
  onDoneClick: () -> Unit,
  onTitleChange: (String) -> Unit,
  onDescriptionChange: (String) -> Unit,
  onDateChange: (Long) -> Unit,
  onTimeChange: (Int, Int) -> Unit,
  onIconChange: (Int) -> Unit,
  onCountryChange: (String) -> Unit,
  onCityChange: (String) -> Unit,
  onStreetChange: (String) -> Unit,
  onBuildingNumberChange: (String) -> Unit,
  activity: AppCompatActivity?,
//  getMarkerAddress: KFunction3<Double, Double, Context, Unit>?
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    ActionToolbar(
      title = AppText.edit_workout,
      modifier = Modifier.toolbarActions(),
      action = { onDoneClick() },
      actionIcon = AppIcon.ic_check,
      )

    Spacer(modifier = Modifier.spacer())

    val fieldModifier = Modifier.fieldModifier()
    BasicField(AppText.title, workout.title, onTitleChange, fieldModifier)
    BasicField(AppText.description, workout.description, onDescriptionChange, fieldModifier)

    //ADDRESS
    BasicField(AppText.country, workout.country, onCountryChange, fieldModifier)
    BasicField(AppText.city, workout.city, onCityChange, fieldModifier)
    BasicField(AppText.street, workout.street, onStreetChange, fieldModifier)
    BasicField(AppText.building_number, workout.buildingNumber, onBuildingNumberChange, fieldModifier)

    Spacer(modifier = Modifier.spacer())

    CardEditors(workout, onDateChange, onTimeChange, activity)

    DropdownIconSelector(
      listOf(AppIcon.ic_check, AppIcon.ic_calendar),
      workout.icon,
      Modifier.card(),
      onNewValue = onIconChange
    )

//    val cameraPositionState = rememberCameraPositionState()//Nothing different(using rememberSaveable internally)
//
//    val context = LocalContext.current
//
//    LaunchedEffect(cameraPositionState.position.target) {
//      if (getMarkerAddress != null) {
//        getMarkerAddress(
//          cameraPositionState.position.target.latitude,//Use the object we created
//          cameraPositionState.position.target.longitude,
//          context
//        )
//      }
//    }

//    Box(
//      modifier = Modifier
//        .fillMaxSize()
//    ) {
//      GoogleMap(
//        cameraPositionState = cameraPositionState
//      ) {
//        Marker(
//          state = MarkerState(
//            position = cameraPositionState.position.target
//          )
//        )
//      }
//    }


  }
}

@Composable
private fun CardEditors(
  workout: Workout,
  onDateChange: (Long) -> Unit,
  onTimeChange: (Int, Int) -> Unit,
  activity: AppCompatActivity?
) {
    RegularCardEditor(AppText.date, AppIcon.ic_calendar, workout.date, Modifier.card()) {
      showDatePicker(activity, onDateChange)
    }

    RegularCardEditor(AppText.time, AppIcon.ic_clock, workout.time, Modifier.card()) {
      showTimePicker(activity, onTimeChange)

  }
}


private fun showDatePicker(activity: AppCompatActivity?, onDateChange: (Long) -> Unit) {
  val picker = MaterialDatePicker.Builder.datePicker().build()

  activity?.let {
    picker.show(it.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener { timeInMillis -> onDateChange(timeInMillis) }
  }
}

private fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
  val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()

  activity?.let {
    picker.show(it.supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
  }
}

@Preview(showBackground = true)
@Composable
fun EditWorkoutScreenPreview() {
  val workout = Workout(
    title = "Workout title",
    description = "Workout description",
    date = "Mon, 1 Jan 2022",
    time = "12:00",
  )

  AppTheme {
    EditWorkoutsScreenContent(
      workout = workout,
      onDoneClick = { },
      onTitleChange = { },
      onDescriptionChange = { },
      onDateChange = { },
      onTimeChange = { _, _ -> },
      onIconChange = { },
      activity = null,
      onCountryChange = { },
      onCityChange = { },
      onStreetChange = { },
      onBuildingNumberChange = { },
//      getMarkerAddress = null
    )
  }
}
