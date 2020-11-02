package com.example.covid_19shoppingcentre

class Reserve {
    var status: String?=null
    var memberId: String?=null
    var date: String?=null
    var time: String?=null
    var storeName: String?=null

    constructor(){
    }

    constructor(
        status: String?,
        memberId: String?,
        date: String?,
        time: String?,
        storeName: String?

    ) {
        this.status = status
        this.memberId = memberId
        this.date = date
        this.time = time
        this.storeName = storeName
    }
}