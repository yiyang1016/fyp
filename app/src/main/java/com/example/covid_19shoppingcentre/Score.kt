package com.example.covid_19shoppingcentre

import android.icu.text.CaseMap
import java.util.*

class Score {
    var Title: String?=null
    var Score: Int?= null
    var Score_Date: String?=null
    var Member_Id: String?=null
    var Score_Id:String?=null
    constructor(){
    }

    constructor(Title: String?, Score: Int?, Score_Date: String?, Member_Id: String?, Score_Id:String?) {
        this.Title = Title
        this.Score = Score
        this.Score_Date = Score_Date
        this.Member_Id = Member_Id
        this.Score_Id = Score_Id
    }


}