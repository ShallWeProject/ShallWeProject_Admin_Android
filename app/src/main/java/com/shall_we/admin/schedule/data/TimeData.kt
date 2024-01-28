package com.shall_we.admin.schedule.data
data class TimeData(val time: String) {
    fun toLocalTimeFormat(): String {
        // "시" 문자를 제거하고, 시간을 "HH:mm" 형식으로 변환합니다.
        val hour = time.replace("시", "").trim()
        return String.format("%02d:00", hour.toInt())
    }
}

