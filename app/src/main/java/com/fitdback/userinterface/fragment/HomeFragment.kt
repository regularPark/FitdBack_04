package com.fitdback.userinterface.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.fitdback.posedetection.CameraActivity
import com.fitdback.posedetection.R
import com.fitdback.test.DevModeActivity
import com.fitdback.userinterface.LoginActivity
import com.fitdback.userinterface.TutorialActivity
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun signOut(intent: Intent) {

        firebaseAuth.signOut() // 로그아웃 처리
        //Toast.makeText(this, "로그아웃 완료", Toast.LENGTH_SHORT).show()

        startActivity(intent)
        activity?.finish()
    }


    // TODO:버튼 관련 함수 여기에
    override fun onCreateView(

            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 스쿼트 버튼
        val squatBtn : Button = view.findViewById(R.id.squatBtn)
        squatBtn.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?){
                val toTutorialActivity = Intent(context, TutorialActivity::class.java)
                toTutorialActivity.putExtra("exr_mod","squat") // 모드 설정
                startActivity(toTutorialActivity)
            }
        })

        // 플랭크 버튼
        val plankBtn : Button = view.findViewById(R.id.plkBtn)
        plankBtn.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?){
                val toTutorialActivity = Intent(context, TutorialActivity::class.java)
                toTutorialActivity.putExtra("exr_mod","plank") // 모드 설정
                startActivity(toTutorialActivity)
            }
        })

        // 푸시업 버튼
        val puBtn : Button = view.findViewById(R.id.puBtn)
        puBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?){
                val toTutorialActivity = Intent(context, TutorialActivity::class.java)
                toTutorialActivity.putExtra("exr_mod", "pushup")
                startActivity(toTutorialActivity)
            }
        })


        // 로그아웃
        val btnSignOut : Button = view.findViewById(R.id.btnSignOut)
        btnSignOut.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val toLoginActivity = Intent(context, LoginActivity::class.java)
                signOut(toLoginActivity)

            }

        })

        // 개발자 모드
        val btnDevMode : Button = view.findViewById(R.id.btnDevMode)
        btnDevMode.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v:View?){
                activity?.finish()
                val toDevActivity = Intent(context, DevModeActivity::class.java)
                startActivity(toDevActivity)
            }
        })





        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =

                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }

    }
}