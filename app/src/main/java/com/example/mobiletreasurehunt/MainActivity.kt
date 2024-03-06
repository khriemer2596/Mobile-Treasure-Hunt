package com.example.mobiletreasurehunt

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.renderscript.RenderScript
import android.renderscript.RenderScript.Priority
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import com.example.compose.MobileTreasureHuntTheme
import com.example.mobiletreasurehunt.ui.HuntViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnSuccessListener
import dagger.hilt.android.AndroidEntryPoint

/* Assignment 6: Mobile Treasure Hunt
Kevin Riemer / riemerk@oregonstate.edu
CS 492 / Oregon State University
3/8/2024
*/

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val viewModel: HuntViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

        setContent {
            var currentLocation by remember { mutableStateOf(LatLng(0.toDouble(), 0.toDouble())) }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations){
                        // Update UI with location data
                        currentLocation = LatLng(location.latitude, location.longitude)
                        viewModel.updateLat(currentLocation.latitude)
                        viewModel.updateLong(currentLocation.longitude)
                    }
                }
            }
            startLocationUpdates()
            MobileTreasureHuntTheme {
                TreasureHuntApp()
            }
        }
    }


    private fun getCurrentLocation() {
        // Check Location Permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            resultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    viewModel.updateLat(currentLocation.latitude)
                    viewModel.updateLong(currentLocation.longitude)
                }
            }
        }
    }

    // Push toast notification and alert dialog when location permission was denied
    private val resultLauncher = registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, getString(R.string.toast_alert), Toast.LENGTH_SHORT).show()
            setContent {
                MobileTreasureHuntTheme {
                    AlertDialog(
                        title = {
                            Text(getString(R.string.loc_not_granted))
                        },
                        text = {
                            Text(getString(R.string.alert_body))
                        },
                        onDismissRequest = {
                            this.finishAndRemoveTask()
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    this.finishAndRemoveTask()
                                }
                            ) {
                                Text(getString(R.string.confirm))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    this.finishAndRemoveTask()
                                }
                            ) {
                                Text(getString(R.string.dismiss))
                            }
                        }
                    )
                }
            }
        }
    }

    @SuppressLint("VisibleForTests", "MissingPermission")
    private fun startLocationUpdates() {
        locationCallback.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 100
                fastestInterval = 50
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                maxWaitTime = 100
            }
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

}



