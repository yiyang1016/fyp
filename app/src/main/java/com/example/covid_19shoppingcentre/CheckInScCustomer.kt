package com.example.covid_19shoppingcentre

class CheckInScCustomer {
    var name: String?=null
    var phone: String? = null
    var status: String? = null
    var checkInTime: String? = null
    var bodyTemperature: String? = null
    var customerId: String? = null

    constructor(){
    }

    constructor(
        name: String?,
        phone: String?,
        status: String?,
        checkInTime: String?,
        bodyTemperature: String?,
        customerId: String?

    ) {
        this.name = name
        this.phone = phone
        this.status = status
        this.checkInTime = checkInTime
        this.bodyTemperature = bodyTemperature
        this.customerId = customerId
    }
}