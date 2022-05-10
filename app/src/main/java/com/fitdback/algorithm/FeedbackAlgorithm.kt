package com.fitdback.algorithm

import android.content.Context
import android.graphics.PointF
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast

import com.fitdback.posedetection.R
import com.google.firebase.database.Transaction
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.acos
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.sqrt

class FeedbackAlgorithm {


    // 스쿼트 먼저
    companion object {
        var exr_mode: String = "Empty"

        var hka_l_angle: Double = 0.0 //왼쪽 엉덩이, 무릎, 발 각도
        var hka_r_angle: Double = 0.0 //오른쪽 엉덩이, 무릎, 발 각도
        var nhk_l_angle: Double = 0.0 //목, 왼골반, 왼무릎 각도

        var total_exr_time: Long = 0
        var start_time: Long = 0
        var exr_time_result: Int = 0 //최종 시간 값. (초 단위)
        var head_y: Float = 0.0f
        var ank_l_y: Float = 0.0f

        var no_exr: Boolean = false // 운동 판별 할건지(머리가 가장 위에, 왼발목이 가장 아래에 있어야 운동 판별)
        var isSquat: Boolean = false //스쿼트 동작 완료
        var isSound: Boolean = false
        var wrong_mode: Int = 0 //올바르지 않은 자세
        var isWrong: Boolean = false
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

        val squat_cal: Double = 0.50 // 스쿼트 1회당 칼로리

        var isExrFinished: Boolean = false
        //val soundPool = SoundPool.Builder().build()

        private fun cal_dist(p1: PointF, p2: PointF): Double {
            return sqrt((((p1.x - p2.x) * (p1.x - p2.x)) + ((p1.y - p2.y) * (p1.y - p2.y))).toDouble())
        }

        private fun cal_angle(p1: PointF, p2: PointF, p3: PointF): Double {

            val dist1 = cal_dist(p1, p2)
            val dist2 = cal_dist(p2, p3)
            val dist3 = cal_dist(p3, p1)

            return acos((((dist1 * dist1) + (dist2 * dist2) - (dist3 * dist3)) / (2 * dist1 * dist2))) * 180 / pi
        }


        fun sound_play(context: Context, sound: Int) {
            var sound: MediaPlayer = MediaPlayer.create(context, sound)
            if (!isSound) {
                sound.start()
                isSound = true

                Handler().postDelayed({
                    sound.release()
                    isSound = false
                }, sound.duration.toLong())

            }

        }

        //DrawView 164줄에서 squat 함수 호출
        fun squat(context: Context, mDrawPoint: ArrayList<PointF>) {
            no_exr = false

            if (exr_cnt == 0 && time_tf) {
                start_time = System.currentTimeMillis()
                time_tf = false
            }

            hka_l_angle = cal_angle(mDrawPoint[8], mDrawPoint[9], mDrawPoint[10])
            hka_r_angle = cal_angle(mDrawPoint[11], mDrawPoint[12], mDrawPoint[13])
            nhk_l_angle = cal_angle(mDrawPoint[1], mDrawPoint[8], mDrawPoint[9])

            head_y = mDrawPoint[0].y
            ank_l_y = mDrawPoint[10].y

            //------------------------------
            //머리가 가장 위, 왼발목이 가장 아래 있는지 체크 -> 아니면 스쿼트 판별x
            for (i in 1..13) {
                if (head_y >= mDrawPoint[i].y) {
                    no_exr = true
                    break
                }
            }
            for (i in 0..9) {
                if (mDrawPoint[i].y >= ank_l_y) {
                    no_exr = true
                    break
                }
            }
            //------------------------------

            //스쿼트 판별 시작
            if (150.toDouble() <= hka_l_angle && hka_l_angle <= 190.toDouble() && !no_exr) {
                isStand = true
                //Log.d("zxcv", "stand complete")

                //운동 횟수 추가 판단되면 스쿼트 동작 완료 후 기본자세(stand)로 돌아가면 횟수 추가
                if (cnt_s_tf || cnt_f_tf) {
                    if (cnt_s_tf && isSquat && wrong_mode < 5) {
                        cnt_s_tf = false
                        isSquat = false
                        cnt_f_tf = false
                        isWrong = false
                        wrong_mode = 0
                        exr_cnt_s++
                        sound_play(context, R.raw.sound1) // "띠링" (성공사운드)
                        Toast.makeText(context, "운동 성공~!", Toast.LENGTH_SHORT).show()
                    } else if (cnt_f_tf && isWrong) {
                        cnt_f_tf = false
                        if (isWrong && wrong_mode >= 1) {
                            exr_cnt_f++

                            if (wrong_mode == 2) {
                                sound_play(context, R.raw.leg_feedback) // "다리 더 굽히세요"
                                Toast.makeText(context, "------FAIL2------", Toast.LENGTH_SHORT).show()
                            } // 2 -> 다리가 덜 굽혀져 실패

                            /*else if(wrong_mode==1) {
                                sound_play(context, R.raw.back_feedback)
                                Toast.makeText(context, "------FAIL1------", Toast.LENGTH_SHORT).show()
                            } // 1 -> 허리 굽혀져 실패*/
                            else if (wrong_mode == 5) {
                                sound_play(context, R.raw.leg_feedback_2) // "다리 너무 굽혔어요"
                                Toast.makeText(context, "------FAIL5------", Toast.LENGTH_SHORT).show()
                                cnt_s_tf = false
                                isSquat = false
                            } // 5 -> 다리가 너무 굽혀져 실패

                            /*else if(wrong_mode==3) {
                                Toast.makeText(context, "------FAIL3------", Toast.LENGTH_SHORT).show()
                            } // 1+2 -> 다리가 덜 굽혀지고 허리도 덜 굽혀져 실패*/
                        }
                        Log.d("exr_F", "wrong_mode = " + wrong_mode + " exr_cnt_f = " + exr_cnt_f)
                        isWrong = false
                        wrong_mode = 0
                    }
                    exr_cnt = exr_cnt_s + exr_cnt_f
                    Log.d("exr_cnt", "S = " + exr_cnt_s + " F = " + exr_cnt_f + " T = " + exr_cnt)
                    exr_cal = ceil((exr_cnt.toDouble() * squat_cal))

                    if (exr_cnt == 10) {
                        total_exr_time = System.currentTimeMillis() - start_time
                        exr_time_result = (ceil((total_exr_time / 1000.toDouble()))).toInt()
                    }
                    //--------------------------------------------------------------------------------------------------------
                    //exr_cnt_s(int) = 운동 성공 횟수, exr_cnt_f(int) = 운동 실패 횟수, exr_cnt(int) = 총 운동 횟수
                    //exr_cal(double) = 칼로리 소모량, exr_time_result(int) = 운동 시간(초), exr_mode(string) = 운동 종류(스쿼트)
                    //--------------------------------------------------------------------------------------------------------

                }
            } else if (140.toDouble() >= hka_l_angle && !no_exr) {
                //스쿼트 자세로 판단되면 Stand가 아님
                isStand = false

                //스쿼트 성공
                if (105.toDouble() >= hka_l_angle && 60.toDouble() <= hka_l_angle && 90.toDouble() <= nhk_l_angle && wrong_mode != 5) {
                    cnt_s_tf = true
                    isSquat = true
                    Log.d("exr_S", "Success 각도 = 무릎 " + hka_l_angle)
                }

                //다리가 70도 이하로 내려가면 너무 굽혀져 실패
                else if (60.toDouble() > hka_l_angle) {
                    cnt_f_tf = true
                    wrong_mode = 5
                    isWrong = true
                    Log.d("exr_F5", "F5 각도 = 무릎 " + hka_l_angle)
                } else if (!isSquat) {
                    cnt_f_tf = true
                    if (isSquat == false && wrong_mode < 5) {
                        wrong_mode = 0

                        /*//허리가 70도 이상 굽혀져 실패
                        if(nhk_l_angle<90.toDouble()) {
                            wrong_mode += 1
                            isWrong = true
                        }*/

                        //다리가 105도 이하로 굽혀지지 않아 실패
                        if (hka_l_angle > 105.toDouble()) {
                            wrong_mode += 2
                            isWrong = true
                        }
                    }
                }
            }
        }
    }
}