package com.fitdback.database.datamodel

data class UserInfoDataModel(

    val user_email: String? = null,
    val user_password: String? = null,
    val user_nickname: String? = null,
    val user_height: Float = 0f,
    val user_weight: Float = 0f,
    val trainer_uid: String = ""

)