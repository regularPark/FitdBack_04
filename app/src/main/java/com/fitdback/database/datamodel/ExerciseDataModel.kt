package com.fitdback.database.datamodel

data class ExerciseDataModel(

    val ex_date: String? = null,
    val ex_type: String? = null,
    val ex_time: Int = 0,
    val ex_count: Int = 0,
    val ex_success_count: Int = 0,
    val ex_calorie: Int = 0

)