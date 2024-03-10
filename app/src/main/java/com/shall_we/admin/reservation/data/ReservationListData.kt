package com.shall_we.admin.reservation.data

data class ReservationListData(
    val reservationId: Long,
    val status: String,
    val name: String,
    val time: String,
    val payment:String,
    val phoneNum: String,
    val person: Int
)



