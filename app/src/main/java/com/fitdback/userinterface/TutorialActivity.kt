package com.fitdback.userinterface

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.fitdback.posedetection.CameraActivity
import com.fitdback.posedetection.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class TutorialActivity : YouTubeBaseActivity() {

    val api_key = "AIzaSyD-RJXgEjAgWZsO1LAO5sIgrpFob8k2qEk"
    val videoId = "5_SIHTd6Pqo"
    companion object{
        var cameramode : String = "back"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        // 프래그먼트로 데이터 전송
        var bundle : Bundle = Bundle()

        // 유튜브
        val youtubeView = findViewById<YouTubePlayerView>(R.id.youtubePlayer)
        youtubeView.initialize(api_key, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
               if (!wasRestored) {
                   player.cueVideo(videoId)
               }
                /*
                player.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                    override fun onAdStarted() {}
                    override fun onLoading() {}
                    override fun onVideoStarted() {}
                    override fun onVideoEnded() {}
                    override fun onError(p0: YouTubePlayer.ErrorReason) {}
                    override fun onLoaded(videoId: String) {
                        player.play()
                    }
                })*/
            }



            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                result: YouTubeInitializationResult?
            ) {
                TODO("Not yet implemented")
            }
        })

        // 레이아웃
        val btnFitnessStart = findViewById<Button>(R.id.btnFitnessStart)

        // 운동정보 넘기기
        val exr = intent.getStringExtra("exr_mod")

        // 인텐트
        val intent = Intent(this, CameraActivity::class.java)

        // btnFitnessStart 버튼을 클릭하여 CameraActivity 실행
        btnFitnessStart.setOnClickListener {
            intent.putExtra("exr_mod",exr)
            cameramode = "back"
            startActivity(intent)
        }

        val btnFitnessStartFront = findViewById<Button>(R.id.btnFitnessStartBack)

        btnFitnessStartFront.setOnClickListener {
            intent.putExtra("exr_mod",exr)
            cameramode = "front"
            startActivity(intent)
        }

    }
}