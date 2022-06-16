package com.fitdback.database.datamodel

data class ExerciseDataModel(

    val ex_date: String? = null,
    var ex_type: String? = null,
    var ex_time: Int = 0,
    var ex_count: Int = 0,
    val ex_success_count: Int = 0,
    val ex_calorie: Int = 0

)