package biz.marketable_skill.biz.quiz

import android.app.Application
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import biz.marketable_skill.biz.quiz.common.*
import com.google.android.gms.ads.MobileAds
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {



    override fun onCreate() {
        super.onCreate()

        setRealm()

        setSoundPool()

        loadStatus()

        //Admobの初期化
        MobileAds.initialize(this,getString(R.string.application_id_admob))

    }

    //SharedPrefereceからのデータ取得
    private fun loadStatus() {
        val pref = this.getSharedPreferences(PREF_FILE_NAME, android.content.Context.MODE_PRIVATE)
        isPassGrade1 = pref.getBoolean(PrefKey.PASS_GRADE1.name, false)
        isPassGrade2 = pref.getBoolean(PrefKey.PASS_GRADE2.name, false)
    }

    //Applicationクラスでの最後に呼ばれるメソッド
    override fun onTerminate() {

        //SoundPoolの解放
        soundPool?.release()

        super.onTerminate()

    }

    private fun setSoundPool() {

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            SoundPool.Builder().setAudioAttributes(
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build())
                    .setMaxStreams(1)
                    .build()
        } else {
            SoundPool(1,AudioManager.STREAM_MUSIC,0)
        }

        //音源のLoad
        soundIdCorrect = soundPool!!.load(this, R.raw.sound_correct,1)
        soundIdInCorrect = soundPool!!.load(this,R.raw.sound_incorrect, 1)
        soundIdApplause = soundPool!!.load(this, R.raw.sound_applause, 1)
        soundIdTin = soundPool!!.load(this, R.raw.sound_tin, 1)

    }

    private fun setRealm() {

        Realm.init(this)

        //Realm内のデータ初期化
        val config = RealmConfiguration.Builder().build()
        Realm.deleteRealm(config)

    }


}