package com.shall_we.admin.login.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class IdentificationUploadReq(
    val identification: String,
    val businessRegistration: String,
    val bankbook: String
)

@Parcelize
data class IdentificationUploadUri(
    val identificationUri: Uri,
    val businessRegistrationUri: Uri,
    val bankbookUri: Uri
) : Parcelable