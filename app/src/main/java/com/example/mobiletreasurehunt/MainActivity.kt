package com.example.mobiletreasurehunt

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.compose.MobileTreasureHuntTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    setContent {
                        MobileTreasureHuntTheme {
                            TreasureHuntApp()
                        }
                    }
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.


                }
            }

        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                setContent {
                    MobileTreasureHuntTheme {
                        TreasureHuntApp()
                    }
                }
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                //showInContextUI(...)
                /*
                setContent {
                    MobileTreasureHuntTheme {
                        AlertDialog(
                            icon = {

                            },
                            title = {
                                Text(text = "Location Denial Notification")
                            },
                            text = {
                                Text(text = "Are you sure you want to deny location settings? You will not be able to" +
                                        "use this app if you do not grant this permission.")
                            },
                            onDismissRequest = {

                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        //onConfirmation()
                                    }
                                ) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        finishAffinity()
                                    }
                                ) {
                                    Text("Dismiss")
                                }
                            }
                        )
                    }
                }

                 */

            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }

    }
}
