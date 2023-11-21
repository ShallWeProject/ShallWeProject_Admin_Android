package com.shall_we.admin.product

data class ProductListData(
    val title: String,
    val expCategory: CharSequence,
    val sttCategory: String,
    val subtitle: String,
    val thumbnail: String,
    val description: String,
    val explanation: List<ExplanationRes>,
    val contextImg: String,
    //val location: String,
    val price: Int,
    //val caution: String
    )



data class ExplanationRes(
    val stage: String? = null,
    val description: String? = null,
    val explanationUrl: List<String>? = null
)
