package com.decorator1889.instruments.View

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.appodeal.ads.Appodeal
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.fragment_result.*
import java.util.*

const val MINUTES_IN_AN_HOUR = 60
const val SECONDS_IN_A_MINUTE = 60
class ResultFragment : Fragment() {
    private val fragment = CategoryFragment()
    private val fragmentTest = TestFragment()
    private var testType: String? = null
    private var testName: String? = null
    private var score = 0
    private var size = 0
    private var timer = 0
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        testType = arguments!!.getString("type")
        testName = arguments!!.getString("name")
        score = arguments!!.getInt("score")
        size = arguments!!.getInt("size")
        timer = arguments!!.getInt("timer")

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPreferences = activity!!.getSharedPreferences("SP_TEST",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString("type", testType)
        editor.putString("name", testName)
        editor.apply()
        btn_to_main.setOnClickListener {
            val rand = Random()
            val chance: Int = rand.nextInt(10)
            if (chance > 5)
            {
                val activity = activity as MainActivity
                Appodeal.show(activity, Appodeal.INTERSTITIAL);
            }
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.main_container, fragment)?.addToBackStack(null)
            transaction?.commit()
        }
        btn_again.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.main_container, fragmentTest)?.addToBackStack(null)
            transaction?.commit()
        }
        scoreResult.text = "$score из $size"
        val seconds = timer % SECONDS_IN_A_MINUTE
        val totalMinutes = timer / SECONDS_IN_A_MINUTE
        val minutes = totalMinutes % MINUTES_IN_AN_HOUR
        if (score < size) {
            textTitleResult.text = "Тест не пройден!"
            imageLogo.setImageResource(R.drawable.ic_frown)
        } else {
            textTitleResult.text = "Тест пройден успешно!"
            imageLogo.setImageResource(R.drawable.ic_smile)
        }
        time.text = String.format("%02d:%02d", minutes, seconds);

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = activity as MainActivity
        activity.hideUpButton()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.main_container, fragment)?.addToBackStack(null)
                    transaction?.commit()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }
}