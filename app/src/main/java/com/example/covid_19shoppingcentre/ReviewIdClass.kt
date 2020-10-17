package com.example.covid_19shoppingcentre

class ReviewIdClass {
    var Review_Id : String = ""
    constructor(){

    }

    constructor(Review_Id: String) {
        this.Review_Id = Review_Id
    }

    override fun toString(): String {
        return "$Review_Id"
    }
    fun getReviewId(): String {
        return Review_Id
    }

}