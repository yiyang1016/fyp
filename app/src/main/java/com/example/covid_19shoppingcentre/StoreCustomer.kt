package com.example.covid_19shoppingcentre

class StoreCustomer {
    var Name : String? = null
    var PhoneNumber : String? = null
    var status : String? = null

    constructor(){
    }

    constructor(
        Name: String?,
        PhoneNumber: String?,
        status: String?
    ) {
        this.Name = Name
        this.PhoneNumber = PhoneNumber
        this.status = status
    }
}