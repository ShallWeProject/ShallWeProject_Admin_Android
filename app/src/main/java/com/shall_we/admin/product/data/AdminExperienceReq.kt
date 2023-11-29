package com.shall_we.admin.product.data

data class AdminExperienceReq(
    val subtitle: String, // 지역
    val expCategory: String,
    //val sttCategory: String,
    val title: String, // 상품명
    val giftImgKey: List<String>?,// 대표사진
    val description: String,
    val explanation: List<ExplanationRes>,
    val location: String,
    val price: Int? = 0,
    val note: String
)

data class ExplanationRes(
    val stage: String? = null,
    val description: String? = null,
    val explanationUrl: String? = null
)