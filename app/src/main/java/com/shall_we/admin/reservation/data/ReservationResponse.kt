package com.shall_we.admin.reservation.data

data class ReservationResponse(
    val data: List<ReservationListData>,
    val transaction_time: String,
    val status: String,
    val description: String?,
    val statusCode: Int
)
