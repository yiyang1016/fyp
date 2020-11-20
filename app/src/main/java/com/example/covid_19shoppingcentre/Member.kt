package com.example.covid_19shoppingcentre

class Member {
    var ic: String?=null
    var id: String?=null
    var name: String?=null
    var phoneNumber: String?=null

    constructor()
    constructor(ic: String?, id: String?, name: String?, phoneNumber: String?) {
        this.ic = ic
        this.id = id
        this.name = name
        this.phoneNumber = phoneNumber
    }


}