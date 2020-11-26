package com.example.covid_19shoppingcentre

class BodyTemperatureDetails {
    var bodyTemperature: String?=null
    var checkInTime: String?=null
    var checkOutTime:String?=null
    var customerId: String?=null
    var name:String?=null
    var phone:String?=null
    var status: String?=null

    constructor(){

    }

    constructor(
        bodyTemperature: String?,
        checkInTime: String?,
        checkOutTime: String?,
        customerId: String?,
        name: String?,
        phone: String?,
        status: String?
    ) {
        this.bodyTemperature = bodyTemperature
        this.checkInTime = checkInTime
        this.checkOutTime = checkOutTime
        this.customerId = customerId
        this.name = name
        this.phone = phone
        this.status = status
    }


}