package com.example.covid_19shoppingcentre

class MemberCheckInStore {
    var checkInDate: String?=null
    var checkInTime: String?=null
    var customerId: String?=null
    var status: String?=null
    var storeId: String?=null
    var bodyTemp: String?=null

    constructor(){

    }

    constructor(
        checkInDate: String?,
        checkInTime: String?,
        customerId: String?,
        status: String?,
        storeId: String,
        bodyTemp: String?
    ) {
        this.checkInDate = checkInDate
        this.checkInTime = checkInTime
        this.customerId = customerId
        this.status = status
        this.storeId = storeId
        this.bodyTemp=bodyTemp
    }
}