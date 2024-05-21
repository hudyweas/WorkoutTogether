package com.hudyweas.workouttogether.model

import android.location.Address
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.hudyweas.workouttogether.R
import java.util.Date
import java.util.Locale

data class Workout(
  @DocumentId val id: String = "",
  @ServerTimestamp val createdAt: Date = Date(),
  val title: String = "",
  val description: String = "",
  val userId: String = "",
  val icon: Int = R.drawable.ic_workout,
  val date: String = "",
  val time: String = "",
  val country: String = "",
  val city: String = "",
  val street: String = "",
  val buildingNumber: String = "",
  val address: Address? =  null
)
