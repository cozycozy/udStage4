package biz.marketable_skill.biz.quiz

import android.content.res.AssetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import biz.marketable_skill.biz.quiz.common.changeSoundMode
import biz.marketable_skill.biz.quiz.common.isDateSetFinished
import biz.marketable_skill.biz.quiz.common.makeToast
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_sound.setOnClickListener { view ->
            changeSoundMode(view)
        }

        importQuestionFromCSV()

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

    private fun writeCSVDateToRealm(templist : MutableList<Array<String>>) {}


    private fun setCSVReader(): CSVReader {

        val assetManager : AssetManager = resources.assets
        val inputStream = assetManager.open("Questions.csv")
        val parser = CSVParserBuilder().withSeparator(',').build()
        return CSVReaderBuilder(InputStreamReader(inputStream)).withCSVParser(parser).build()

    }


}
