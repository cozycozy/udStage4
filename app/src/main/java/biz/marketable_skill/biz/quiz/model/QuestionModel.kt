package biz.marketable_skill.biz.quiz.model

import io.realm.RealmObject

open class QuestionModel : RealmObject() {

    //id
    var id : String = ""

    // 級
    var gradeId: String = ""

    // 質問
    var question : String = ""

    // 答え
    var answer : String = ""

    // 選択肢1
    var choice1 : String = ""

    // 選択肢2
    var choice2 : String = ""

    // 選択肢3
    var choice3 : String = ""

    // 解説テキスト
    var explanation : String = ""

}