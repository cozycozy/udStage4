package biz.marketable_skill.biz.quiz

import android.content.res.AssetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import biz.marketable_skill.biz.quiz.common.*
import biz.marketable_skill.biz.quiz.model.QuestionModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    lateinit var interstitialAd : InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_sound.setOnClickListener { view ->
            changeSoundMode(view)
        }

        importQuestionFromCSV()

        setBannerAd()

        setInterstitialAd()

        btnGrade1.setOnClickListener{
            interstitialAd.show()
        }

        btnGrade2.setOnClickListener {
            interstitialAd.show()
        }

    }

    private fun setInterstitialAd() {

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.interstitial_ad_unit_id)

        loadInterstitialAd()

        interstitialAd.adListener = object : AdListener(){
            override fun onAdClosed() {
                loadInterstitialAd()
                //Todo テストアクティビティに行く処理を書く
            }
        }

    }

    private fun loadInterstitialAd() {

        //Admobへの広告リクエスト
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)

    }

    private fun setBannerAd() {

        //Admobへの広告リクエスト
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }

    private fun importQuestionFromCSV() {

        val reader = setCSVReader()
        var templist: MutableList<Array<String>>? = null

        try {
            templist = reader.readAll()
            writeCSVDateToRealm(templist!!)
        } catch (e : IOException){
            makeToast(this@MainActivity,getString(R.string.import_fail))
            isDateSetFinished = false
        } finally {
            reader.close()
        }

    }

    private fun writeCSVDateToRealm(templist : MutableList<Array<String>>) {

        val realm = Realm.getDefaultInstance()
        val iterator = templist.iterator()
        realm.executeTransactionAsync({
            while (iterator.hasNext()){
                val record = iterator.next()
                val questionDB = it.createObject(QuestionModel::class.java)
                questionDB.apply {
                    id = record[0]
                    gradeId = record[1]
                    question = record[2]
                    answer = record[3]
                    choice1 = record[4]
                    choice2 = record[5]
                    choice3 = record[6]
                    explanation = record[7]
                }
            }
        },{
            isDateSetFinished = true
            makeToast(this@MainActivity, getString(R.string.import_success))

        },{
            isDateSetFinished = false
            makeToast(this@MainActivity, getString(R.string.import_fail_notice))
        })

    }


    private fun setCSVReader(): CSVReader {

        val assetManager : AssetManager = resources.assets
        val inputStream = assetManager.open("Questions.csv")
        val parser = CSVParserBuilder().withSeparator(',').build()
        return CSVReaderBuilder(InputStreamReader(inputStream)).withCSVParser(parser).build()

    }

    override fun onResume() {
        super.onResume()

        //戻ってきたときの画面更新
        updateUI()

        adView?.resume()

        if ( !interstitialAd.isLoaded) {
            loadInterstitialAd()
        }

    }

    override fun onPause() {
        super.onPause()

        adView?.pause()

    }

    override fun onDestroy() {
        super.onDestroy()

        adView.destroy()
    }

    private fun updateUI() {

        if(isPaidGrade1 && isPaidGrade1){
            btnGrade1.setBackgroundResource(R.drawable.button_grade1)
            btnGrade1.isEnabled = true
        } else if (isPassGrade2 && !isPaidGrade1) {
            btnGrade1.setBackgroundResource(R.drawable.button_go_grade1)
            btnGrade1.isEnabled = true
        } else {
            btnGrade1.setBackgroundResource(R.drawable.button_vague_grade1)
            btnGrade1.isEnabled = false
        }

    }


}
