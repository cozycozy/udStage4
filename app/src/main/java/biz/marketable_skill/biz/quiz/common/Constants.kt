package biz.marketable_skill.biz.quiz.common

import android.media.SoundPool


var soundPool : SoundPool? = null

var soundIdCorrect = 0
var soundIdInCorrect = 0
var soundIdApplause = 0
var soundIdTin = 0

val PREF_FILE_NAME = "biz.marketable_skill.biz.quiz.status"

var isPassGrade1 = false
var isPassGrade2 = false

var isPaidGrade1 = false

enum class PrefKey{
    PASS_GRADE1,
    PASS_GRADE2
}

var isSoundOn = false

var isDateSetFinished = false

class Constants {

}

