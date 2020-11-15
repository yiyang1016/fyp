package com.example.covid_19shoppingcentre

class ScoreIdClass {
    var Score_Id : String = ""

    constructor(){

    }

    constructor(Score_Id: String) {
        this.Score_Id = Score_Id
    }

    override fun toString(): String {
        return "$Score_Id"
    }


}