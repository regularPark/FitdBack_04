package com.fitdback.test.feedbackTest

import android.widget.TextView
import com.fitdback.algorithm.FeedbackAlgorithm

class FeedbackHandler {

    fun getExType(): String? {

        var text: String? = null

        when (FeedbackAlgorithm.exr_mode) {
            "squat" -> text = "스쿼트"
            "plank" -> text = "플랭크"
            "sidelr" -> text = "사이드 래터럴 레이즈"
        }
        
        return text

    }

    fun getExResult(): String? {

        var text: String? = null

        when (FeedbackAlgorithm.exr_mode) {
            "squat" -> text = "${FeedbackAlgorithm.squat_cnt}회 완료"
            "plank" -> text = "${FeedbackAlgorithm.plank_time_result}초 수행"
            "sidelr" -> text = "${FeedbackAlgorithm.sidelr_cnt}회 완료"
        }

        return text

    }

    fun getFeedback(): String {

        // TODO : 음성 피드백 결과에 맞춰 텍스트 변경
        val text: String = "${getExType()}를 ${getExResult()} 하였습니다. 더욱 분발하셔야겠습니다."

        return text

    }

}