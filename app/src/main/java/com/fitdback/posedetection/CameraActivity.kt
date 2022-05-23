/*
 * Copyright 2018 Zihua Zeng (edvard_hua@live.com), Lang Feng (tearjeaker@hotmail.com)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fitdback.posedetection

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.fitdback.algorithm.FeedbackAlgorithm
import com.fitdback.userinterface.TutorialActivity
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader

/**
 * Main `Activity` class for the Camera app.
 */
class CameraActivity : Activity() {

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> isOpenCVInit = true
                LoaderCallbackInterface.INCOMPATIBLE_MANAGER_VERSION -> {
                }
                LoaderCallbackInterface.INIT_FAILED -> {
                }
                LoaderCallbackInterface.INSTALL_CANCELED -> {
                }
                LoaderCallbackInterface.MARKET_ERROR -> {
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // FeedBack 알고리즘
        val exr = intent.getStringExtra("exr_mod") // 운동 모드 결정

        FeedbackAlgorithm.exr_mode = exr

        if (null == savedInstanceState) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit()

            when (FeedbackAlgorithm.exr_mode) {

                "squat" -> {
                    FeedbackAlgorithm.exr_cnt = 0
                    FeedbackAlgorithm.exr_cnt_s = 0
                    FeedbackAlgorithm.exr_cnt_f = 0
                    FeedbackAlgorithm.wrong_mode = 0
                    FeedbackAlgorithm.isExrFinished = false
                    Handler().postDelayed(
                            { FeedbackAlgorithm.time_tf = true },
                            5000
                    ) //5초 후 운동 시작 시간 설정
                    FeedbackAlgorithm.isPlaying = true
                }

                "plank" -> {
                    Handler().postDelayed(
                            { FeedbackAlgorithm.time_tf = true },
                            5000
                    ) //5초 후 운동 시작 시간 설정
                    FeedbackAlgorithm.exr_time_result = 0
                    FeedbackAlgorithm.start_time = 0
                    FeedbackAlgorithm.exr_cnt = 0
                    FeedbackAlgorithm.isPlaying = true
                    FeedbackAlgorithm.isDone = false
                    FeedbackAlgorithm.isFirst = true

                }

                "sidelr" -> {
                    FeedbackAlgorithm.exr_cnt = 0
                    FeedbackAlgorithm.exr_cnt_s = 0
                    FeedbackAlgorithm.exr_cnt_f = 0
                    FeedbackAlgorithm.wrong_mode = 0
                    FeedbackAlgorithm.isExrFinished = false
                    Handler().postDelayed(
                            { FeedbackAlgorithm.time_tf = true },
                            5000
                    ) //5초 후 운동 시작 시간 설정
                    FeedbackAlgorithm.isPlaying = true
                    FeedbackAlgorithm.prev_time = 0
                    FeedbackAlgorithm.total_exr_time = 0
                }

                "pushup" -> {
                    FeedbackAlgorithm.exr_cnt = 0
                    FeedbackAlgorithm.exr_cnt_s = 0
                    FeedbackAlgorithm.exr_cnt_f = 0
                    FeedbackAlgorithm.wrong_mode = 0
                    FeedbackAlgorithm.isExrFinished = false
                    Handler().postDelayed(
                            { FeedbackAlgorithm.time_tf = true },
                            5000
                    ) //5초 후 운동 시작 시간 설정
                    FeedbackAlgorithm.isPlaying = true
                    FeedbackAlgorithm.prev_time = 0
                    FeedbackAlgorithm.total_exr_time = 0
                }

            }

        }

        //val circleProgressBar = findViewById<CircleProgressBar>(R.id.cpb_circlebar)
        //circleProgressBar.progress
    }

    /*override fun onBackPressed(){
        startActivity(Intent(this, TutorialActivity::class.java))

        FeedbackAlgorithm.sound_stop(this, R.raw.start_exr_5s)
        finish()
    }*/

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onBackPressed() {
        val exr = intent.getStringExtra("exr_mod")
        val intent = Intent(this, TutorialActivity::class.java)
        intent.putExtra("exr_mod", exr)

        startActivity(intent)
        finish()
    }

    companion object {

        init {
            //        System.loadLibrary("opencv_java");
            System.loadLibrary("opencv_java3")
        }

        @JvmStatic
        var isOpenCVInit = false
    }


}