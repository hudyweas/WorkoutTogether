package com.hudyweas.workouttogether.common.static

import com.hudyweas.workouttogether.FRIENDS_SCREEN
import com.hudyweas.workouttogether.R
import com.hudyweas.workouttogether.SETTINGS_SCREEN
import com.hudyweas.workouttogether.WORKOUT_MAP
import com.hudyweas.workouttogether.model.NavigationItem

val navigationItemsList = listOf<NavigationItem>(
    NavigationItem(
        title = "Settings",
        description = "Settings Screen",
        itemId = SETTINGS_SCREEN,
        icon = R.drawable.ic_settings
    ),
    NavigationItem(
        title = "Workout map",
        description = "Check workout map",
        itemId = WORKOUT_MAP,
        icon = R.drawable.map_marker_outline
    )
)