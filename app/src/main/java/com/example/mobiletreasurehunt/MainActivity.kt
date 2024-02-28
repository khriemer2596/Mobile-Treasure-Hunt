package com.example.mobiletreasurehunt

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.compose.MobileTreasureHuntTheme


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                setContent {
                    MobileTreasureHuntTheme {
                        TreasureHuntApp()
                    }
                }
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
                setContent {
                    MobileTreasureHuntTheme {
                        AlertDialog(
                            icon = {
                                //Icon(icon, contentDescription = "Example Icon")
                            },
                            title = {
                                Text("Dialogue Title")
                            },
                            text = {
                                Text("Dialogue Text Body")
                            },
                            onDismissRequest = {

                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        requestPermissions(
                                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                android.Manifest.permission.ACCESS_COARSE_LOCATION),
                                            MY_PERMISSIONS_REQUEST_LOCATION)
                                    }
                                ) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {

                                    }
                                ) {
                                    Text("Dismiss")
                                }
                            }
                        )
                    }
                }


            }
            else -> {
                // Ask for both the ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions.
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If the request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // ACCESS_FINE_LOCATION is granted
                        setContent {
                            MobileTreasureHuntTheme {
                                TreasureHuntApp()
                            }
                        }
                    } else if (grantResults[1] ==
                        PackageManager.PERMISSION_GRANTED) {
                        // ACCESS_COARSE_LOCATION is granted

                    }
                    else if (grantResults[0] == -1 && grantResults[1] == -1) {
                        setContent {
                            MobileTreasureHuntTheme {
                                AlertDialog(
                                    icon = {
                                        //Icon(icon, contentDescription = "Example Icon")
                                    },
                                    title = {
                                        Text("Dialogue Title")
                                    },
                                    text = {
                                        Text("Dialogue Text Body")
                                    },
                                    onDismissRequest = {

                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                requestPermissions(
                                                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                        android.Manifest.permission.ACCESS_COARSE_LOCATION),
                                                    MY_PERMISSIONS_REQUEST_LOCATION)
                                            }
                                        ) {
                                            Text("Confirm")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                finish()
                                            }
                                        ) {
                                            Text("Dismiss")
                                        }
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    setContent {
                        MobileTreasureHuntTheme {
                            AlertDialog(
                                icon = {
                                    //Icon(icon, contentDescription = "Example Icon")
                                },
                                title = {
                                    Text("Dialogue Title")
                                },
                                text = {
                                    Text("Dialogue Text Body")
                                },
                                onDismissRequest = {

                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            requestPermissions(
                                                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                    android.Manifest.permission.ACCESS_COARSE_LOCATION),
                                                MY_PERMISSIONS_REQUEST_LOCATION)
                                        }
                                    ) {
                                        Text("Confirm")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {

                                        }
                                    ) {
                                        Text("Dismiss")
                                    }
                                }
                            )
                        }
                    }

                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }

}



