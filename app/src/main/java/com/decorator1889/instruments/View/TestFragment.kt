package com.decorator1889.instruments.View
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import com.appodeal.ads.Appodeal
import com.decorator1889.instruments.LocalDb.RealmDbTest
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.Models.TestModel
import com.decorator1889.instruments.R
import com.decorator1889.instruments.util.DisposableManager
import com.decorator1889.instruments.util.DisposingObserver
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_test.*
import java.util.*

class TestFragment : Fragment() {
    var quesList: List<TestModel?>? = null
    private var score = 0
    private var qid = 0
    private var numberOfQ = 0
    private var timer = 0
    private var testType: String? = null
    private var testName: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesTest: SharedPreferences
    private var currentQ: TestModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test, container, false)
        sharedPreferencesTest = activity!!.getSharedPreferences("SP_TEST", MODE_PRIVATE)
        if (arguments != null ) {
            testType = arguments!!.getString("testType")
            testName = arguments!!.getString("testName")
        } else {
            testType = sharedPreferencesTest.getString("type", "")
            testName = sharedPreferencesTest.getString("name", "")

        }


        val title = view.findViewById<TextView>(R.id.textTitle)
        title.text = "$testName"
        showCountryNameAndCodeFromDatabase(testType)
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPreferences = activity!!.getSharedPreferences("SP_HIGHEST_SCORE", MODE_PRIVATE)
        val myHandler = Handler(Looper.getMainLooper())
        val delay: Long  = 1000


        myHandler.postDelayed(object : Runnable {
            override fun run() {
                timer++
                myHandler.postDelayed(this, delay)
            }
        }, delay)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = activity as MainActivity
        activity.showUpButton()

    }

    override fun onDestroy() {
        DisposableManager.dispose()
        super.onDestroy()
    }
    private fun showCountryNameAndCodeFromDatabase(testType: String?) {
        RealmDbTest.countryCodeAndNameAsObservable()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : DisposingObserver<RealmResults<TestModel>>() {
                override fun onNext(value: RealmResults<TestModel>) {
                  val db =  RealmDbTest.realm.where(TestModel::class.java).equalTo( "type", testType).findAll()
                    quesList = RealmDbTest.realm.copyFromRealm(db)
                    takeAction()

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    fun takeAction() {
    Collections.shuffle(quesList)
    currentQ = quesList!![qid]
        numberOfQ = quesList!!.size
        setQuestionView()
        radio0.setOnClickListener {
            if (currentQ?.true_answer == radio0.text) {
                score++
                radio0.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            else {
                radio0.setBackgroundResource(R.drawable.round_btn_corner_false)
            }
            if (currentQ?.true_answer == radio1.text) {
                radio1.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            if (currentQ?.true_answer == radio2.text) {
                radio2.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            radio0.isEnabled = false
            radio1.isEnabled = false
            radio2.isEnabled = false
            if (qid < quesList!!.size) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    currentQ = quesList!![qid]
                    setQuestionView()
                }, 1000)
            }
            else {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    backToMain()
                }, 1000)            }
        }
        radio1.setOnClickListener {
            if (currentQ?.true_answer == radio1.text) {
                score++
                radio1.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            else {
                radio1.setBackgroundResource(R.drawable.round_btn_corner_false)
            }
            if (currentQ?.true_answer == radio0.text) {
                radio0.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            if (currentQ?.true_answer == radio2.text) {
                radio2.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            radio1.isEnabled = false
            radio2.isEnabled = false
            radio0.isEnabled = false
            if (qid < quesList!!.size) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    currentQ = quesList!![qid]
                    setQuestionView()
                }, 1000)
            }
            else {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    backToMain()
                }, 1000)
            }
        }
        radio2.setOnClickListener {
            if (currentQ?.true_answer == radio2.text) {
                score++
                radio2.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            else {
                radio2.setBackgroundResource(R.drawable.round_btn_corner_false)
            }
            if (currentQ?.true_answer == radio0.text) {
                radio0.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            if (currentQ?.true_answer == radio1.text) {
                radio1.setBackgroundResource(R.drawable.round_btn_corner_true)
            }
            radio2.isEnabled = false
            radio1.isEnabled = false
            radio0.isEnabled = false
            if (qid < quesList!!.size) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    currentQ = quesList!![qid]
                    setQuestionView()
                }, 1000)
            }
            else {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                backToMain()
                }, 1000)

            }
        }
    }
    private fun setQuestionView() {
        if(imageLogo != null) {
            val mPicasso: Picasso? = Picasso.get()
            mPicasso?.setIndicatorsEnabled(false)
            mPicasso
                ?.load("http://ovz2.a-decorator1889.noklm.vps.myjino.ru/instruments/admin-company/upload/" + currentQ?.question)
                ?.into(imageLogo)
            }

        radio0?.text = currentQ?.answer_one
        radio1?.text = currentQ?.answer_two
        radio2?.text = currentQ?.answer_three
        radio0?.setBackgroundResource(R.drawable.round_btn_corner)
        radio1?.setBackgroundResource(R.drawable.round_btn_corner)
        radio2?.setBackgroundResource(R.drawable.round_btn_corner)
        if  (radio0 != null||radio1 != null||radio2 != null) {
            enableBtns()
        }
        qid++
        if (buttonScore != null) {
            buttonScore.text = "$qid / ${quesList!!.size}"
            buttonScore.isEnabled = false
        }

    }
    private fun backToMain () {
        val highestScore = sharedPreferences.getInt(testType, 0)
        if (score>highestScore) {
            val editor = sharedPreferences.edit()
            editor.putInt(testType, score)
            editor.apply()
        }
        val fragment: Fragment
        val bundle = Bundle()
        bundle.putString("type", testType)
        bundle.putString("name", testName)
        bundle.putInt("score", score)
        bundle.putInt("size", numberOfQ)
        bundle.putInt("timer", timer)
        fragment = ResultFragment()
        fragment.arguments = bundle
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment).addToBackStack(null)
        transaction.commit()
    }
    private fun enableBtns () {
        radio0.isEnabled = true
        radio1.isEnabled = true
        radio2.isEnabled = true
    }
}