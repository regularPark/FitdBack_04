package com.fitdback.algorithm

import android.content.Context
import android.graphics.PointF
import android.media.SoundPool
import android.widget.Toast
import com.fitdback.posedetection.R
import kotlin.math.acos
import kotlin.math.sqrt

class FeedbackAlgorithm {

    // 스쿼트 먼저
    companion object {
        var exr_mode: String = "Empty" //운동 모드 1 = 스쿼트

        var hka_l_angle: Double = 0.0 //왼쪽 엉덩이, 무릎, 발 각도
        var hka_r_angle: Double = 0.0 //오른쪽 엉덩이, 무릎, 발 각도

        var com_squat: Boolean = false //스쿼트 동작 완료
        var isPlaying: Boolean = false
        var exr_cnt: Int = 0 //동작 완료 횟수
        val pi: Double = 3.141592
        val soundPool = SoundPool.Builder().build()
        var soundId: Int = 0


        private fun cal_dist(p1: PointF, p2: PointF): Double {
            return sqrt((((p1.x - p2.x) * (p1.x - p2.x)) + ((p1.y - p2.y) * (p1.y - p2.y))).toDouble())
        }

        private fun cal_angle(p1: PointF, p2: PointF, p3: PointF): Double {

            val dist1 = cal_dist(p1, p2)
            val dist2 = cal_dist(p2, p3)
            val dist3 = cal_dist(p3, p1)

            return acos((((dist1 * dist1) + (dist2 * dist2) - (dist3 * dist3)) / (2 * dist1 * dist2))) * 180 / pi
        }

        //DrawView 164줄에서 squat 함수 호출
        fun squat(context: Context, mDrawPoint: ArrayList<PointF>) {
            hka_l_angle = cal_angle(mDrawPoint[8], mDrawPoint[9], mDrawPoint[10])
            hka_r_angle = cal_angle(mDrawPoint[11], mDrawPoint[12], mDrawPoint[13])
//            soundId = soundPool.load(context, R.raw.sound1, 1)

            if ((100.toDouble() >= hka_l_angle) && (100.toDouble() >= hka_r_angle) && isPlaying) {
                com_squat = true

                if (com_squat) {
                    com_squat = false
                    exr_cnt++
                    println("Exercising : hkal = " + hka_l_angle + ", hkar = " + hka_r_angle)
                    //soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
                    Toast.makeText(context, "운동 성공~!", Toast.LENGTH_SHORT).show()
                }

            }
        }


    }

}