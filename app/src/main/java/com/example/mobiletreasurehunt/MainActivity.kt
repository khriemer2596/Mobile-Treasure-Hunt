package com.example.mobiletreasurehunt

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.core.app.ActivityCompat
import com.example.compose.MobileTreasureHuntTheme
import com.example.mobiletreasurehunt.ui.HuntViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
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
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    val lat = location.latitude
                    val long = location.longitude
                    viewModel.updateLat(lat)
                    viewModel.updateLong(long)
                }
            }
        }
    }


    private fun getCurrentLocation() {
        // Check Location Permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            resultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            setContent {
                MobileTreasureHuntTheme {
                    AlertDialog(
                        icon = {
                            //Icon(icon, contentDescription = "Example Icon")
                        },
                        title = {
                            Text(getString(R.string.loc_not_granted))
                        },
                        text = {
                            Text(getString(R.string.alert_body))
                        },
                        onDismissRequest = {
                            finish()
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    finish()
                                }
                            ) {
                                Text(getString(R.string.confirm))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    finish()
                                }
                            ) {
                                Text(getString(R.string.dismiss))
                            }
                        }
                    )
                }
            }
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(
            this, OnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val long = location.longitude
                    viewModel.updateLat(lat)
                    viewModel.updateLong(long)
                    setContent {
                        MobileTreasureHuntTheme {
                            TreasureHuntApp()
                        }
                    }
                }
            }
        )
    }

    // Push toast notification that location permission was denied
    private val resultLauncher = registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, getString(R.string.toast_alert), Toast.LENGTH_SHORT).show()
        }
    }
}



