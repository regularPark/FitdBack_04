package com.fitdback.algorithm

import android.content.Context
import android.graphics.PointF
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import com.fitdback.posedetection.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.acos
import kotlin.math.roundToInt
import kotlin.math.sqrt

class FeedbackAlgorithm {


    // 스쿼트 먼저
    companion object {
        var exr_mode: String = "Empty"

        var hka_l_angle: Double = 0.0 //왼쪽 엉덩이, 무릎, 발 각도
        var hka_r_angle: Double = 0.0 //오른쪽 엉덩이, 무릎, 발 각도

        var total_exr_time: Long = 0
        var start_time: Long = 0
        var exr_time_result: Int = 0 //최종 시간 값. (초 단위)

        var isSquat: Boolean = false //스쿼트 동작 완료
        var isWrong: Boolean = false //올바르지 않은 자세
        var isStand: Boolean = false //서있는지 판단
        var isPlaying: Boolean = false
        var time_tf: Boolean = true //시간 저장
        var cnt_s_tf: Boolean = false //성공 횟수 추가 판단
        var cnt_f_tf: Boolean = false //성공 횟수 추가 판단
        var exr_cnt: Int = 0 //동작 완료 횟수
        var exr_cnt_s: Int = 0 //동작 성공 횟수
        var exr_cnt_f: Int = 0 //동작 실패 횟수
        var exr_cal: Double = 0.0 // 운동 후 칼로리 소모량
        val pi: Double = 3.141592

        val squat_cal : Double = 0.50 // 스쿼트 1회당 칼로리

        var isExrFinished:Boolean = false
        /*val soundPool = SoundPool.Builder().build()
        var soundId: Int = 0*/


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
            if (exr_cnt == 0 && time_tf) {
                start_time = System.currentTimeMillis()
                time_tf = false
            }

            hka_l_angle = cal_angle(mDrawPoint[8], mDrawPoint[9], mDrawPoint[10])
            hka_r_angle = cal_angle(mDrawPoint[11], mDrawPoint[12], mDrawPoint[13])
            //soundId = MediaPlayer.load(context, R.raw.sound1, 1)


            //if ((170.toDouble() <= hka_l_angle && hka_l_angle <= 180.toDouble()) && (170.toDouble() <= hka_r_angle && hka_r_angle <= 180.toDouble()))
            if (170.toDouble() <= hka_l_angle && hka_l_angle <= 180.toDouble()){
                isStand = true
                isSquat = false
                //Log.d("zxcv", "stand complete")

                //운동 횟수 추가 판단되면 스쿼트 동작 완료 후 기본자세(stand)로 돌아가면 횟수 추가
                if (cnt_s_tf || cnt_f_tf) {
                    if (cnt_s_tf) {
                        cnt_s_tf = false
                        exr_cnt_s++
                        Toast.makeText(context, "운동 성공~!", Toast.LENGTH_SHORT).show()
                        Log.d("exr_S", exr_cnt_s.toString())
                    }
                    //else if(cnt_f_tf && (isSquat==false)&&isWrong)
                    if (cnt_f_tf) {
                        cnt_f_tf = false
                        if ((isSquat == false) && isWrong) {
                            exr_cnt_f++
                            Toast.makeText(context, "------FAIL------", Toast.LENGTH_SHORT).show()
                            Log.d("exr_F", exr_cnt_f.toString())
                        }
                    }
                    exr_cnt = exr_cnt_s + exr_cnt_f
                    exr_cal = exr_cnt.toDouble() * squat_cal

                    if (exr_cnt == 5) {
                        total_exr_time = System.currentTimeMillis() - start_time
                        exr_time_result = ((total_exr_time / 1000.toDouble())).roundToInt()
                    }

                    println("S_cnt = " + exr_cnt_s + " F_cnt = " + exr_cnt_f + " T_cnt = " + exr_cnt + " cal = " + String.format("%.1f", exr_cal))
                    if (exr_cnt == 5) {
                        Log.d("exr_T", "Total = " + exr_cnt + " S = " + exr_cnt_s + " F = " + exr_cnt_f + " Time = " + exr_time_result)
                    }
                    //--------------------------------------------------------------------------------------------------------
                    //exr_cnt_s(int) = 운동 성공 횟수, exr_cnt_f(int) = 운동 실패 횟수, exr_cnt(int) = 총 운동 횟수
                    //exr_cal(double) = 칼로리 소모량, exr_time_result(int) = 운동 시간(초), exr_mode(string) = 운동 종류(스쿼트)
                    //--------------------------------------------------------------------------------------------------------

                    //soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)

                    /*val mediaPlayer = MediaPlayer.create(this, R.raw.sound1)
                    mediaPlayer.start()
                    mediaPlayer.setOnCompletionListener { mediaPlayer.release()}*/

                    //Toast.makeText(context, "운동 성공~!", Toast.LENGTH_SHORT).show()
                    //Log.d("asdf", "squat complete")
                }
            } else if (140.toDouble() >= hka_l_angle) {
                //스쿼트 자세로 판단되면 Stand가 아님
                isStand = false
                if (100.toDouble() >= hka_l_angle) {
                    cnt_s_tf = true
                    isSquat = true
                    isWrong = false
                } else {
                    cnt_f_tf = true
                    if (isSquat == false) {
                        isWrong = true
                    }

                }


            }
        }


    }
}