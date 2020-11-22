package com.example.covid_19shoppingcentre

class MemberVisitDetails {
    var checkInDate:String?=null
    var checkInTime: String? = null
    var checkOutTime: String? = null
    var customerId: String? = null
    var status:String?= null
    var storeId:String?=null

    constructor(){

    }


    constructor(
        checkInDate: String?,
        checkInTime: String?,
        checkOutTime: String?,
        customerId: String?,
        status: String?,
        storeId: String?
    ) {
        this.checkInDate = checkInDate
        this.checkInTime = checkInTime
        this.checkOutTime= checkOutTime
        this.customerId = customerId
        this.status = status
        this.storeId = storeId
    }


}