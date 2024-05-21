package com.hudyweas.workouttogether.screens.workout_map


import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.type.LatLng
import com.hudyweas.workouttogether.model.Workout
import kotlinx.coroutines.flow.Flow
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import java.io.IOException

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val workouts = viewModel.workouts
    Text(text ="No workouts available")
    OsmdroidMapView(workouts)
}


@Preview
@Composable
fun WeatherScreenPreview() {
    MapScreen()
}

@Composable
fun OsmdroidMapView(workouts: Flow<List<Workout>>) {
    val context = LocalContext.current
    val items = remember { mutableStateListOf<OverlayItem>() }
    val mapView = MapView(context)
    mapView.isClickable = true
    val itemsLoaded = remember { mutableStateOf(false) }

    LaunchedEffect(workouts) {
        workouts.collect { workouts ->
            workouts.forEach { workout ->
                val location = getLocationFromWorkout(context, "${workout.country}, ${workout.city}, ${workout.street}, ${workout.buildingNumber}")
                if (location != null) {
                    items.add(OverlayItem(workout.title, workout.description, GeoPoint(location.latitude, location.longitude)))
                }
            }
            itemsLoaded.value = true
        }
    }
    if (itemsLoaded.value) {
        var overlay = remember {
            ItemizedOverlayWithFocus<OverlayItem>(items, object :
                ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    return false
                }
            }, context)
        }
        overlay.setFocusItemsOnTap(true)

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                mapView.setTileSource(TileSourceFactory.MAPNIK)
                mapView.setBuiltInZoomControls(true)
                mapView.setMultiTouchControls(true)
                mapView.overlays.add(overlay)
                org.osmdroid.config.Configuration.getInstance().userAgentValue = "WorkoutTogether"

                val mapController = mapView.controller
                mapController.setZoom(13.0)
                var startPoint = GeoPoint(52.3961918, 16.921311)
                if(items.size > 0) {
                    startPoint =  GeoPoint(items.get(0).point.latitude, items.get(0).point.longitude)
                }
                mapController.setCenter(startPoint)
                mapView
            }
        )
    }
}


fun getLocationFromWorkout(context: Context, strAddress: String?): LatLng? {
    val coder = Geocoder(context)
    val address: List<Address>?
    var p1: LatLng? = null
    try {
        // May throw an IOException
        address = coder.getFromLocationName(strAddress!!, 5)
        if (address == null) {
            return null
        }
        if(address.isEmpty()){
            return null
        }
        val location: Address = address[0]
        p1 = LatLng.newBuilder()
            .setLatitude(location.latitude)
            .setLongitude(location.longitude)
            .build()
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    return p1
}


