package com.arch.search.util

import android.content.Context
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller

fun Context.installTls12() {
    try {
        ProviderInstaller.installIfNeeded(this)
    } catch (e: GooglePlayServicesRepairableException) {
        GoogleApiAvailability.getInstance()
            .showErrorNotification(this, e.connectionStatusCode)
    } catch (e: GooglePlayServicesNotAvailableException) {
    }
}