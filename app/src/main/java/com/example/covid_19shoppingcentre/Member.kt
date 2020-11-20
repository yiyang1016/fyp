package com.example.covid_19shoppingcentre

class Member {
    var IC_Number: String?=null
    var Id: String?=null
    var Name: String?=null
    var PhoneNumber: String?=null
    var Email: String?=null
    var Password: String?=null
    var Role: String?= null

    constructor()
    constructor(IC_Number: String?, Id: String?, Name: String?, PhoneNumber: String?, Email: String?, Password: String?, Role: String?) {
        this.IC_Number = IC_Number
        this.Id = Id
        this.Name = Name
        this.PhoneNumber = PhoneNumber
        this.Email = Email
        this.Password = Password
        this.Role = Role
    }
}