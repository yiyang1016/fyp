package com.example.covid_19shoppingcentre

class CheckInScCustomer {
    var name: String?=null
    var phone: String? = null
    var status: String? = null

    constructor(){
    }

    constructor(
        name: String?,
        phone: String?,
        status: String?

    ) {
        this.name = name
        this.phone = phone
        this.status = status
    }
}