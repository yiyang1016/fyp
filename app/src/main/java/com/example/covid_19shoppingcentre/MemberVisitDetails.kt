package com.example.covid_19shoppingcentre

class MemberVisitDetails {
    var CheckInDate:String?=null
    var CheckInTime: String? = null
    var CheckOutTime: String? = null
    var CustomerId: String? = null
    var Status:String?= null
    var StoreId:String?=null

    constructor(){

    }


    constructor(
        CheckInDate: String?,
        CheckInTime: String?,
        CheckOutTime: String?,
        CustomerId: String?,
        Status: String?,
        StoreId: String?
    ) {
        this.CheckInDate = CheckInDate
        this.CheckInTime = CheckInTime
        this.CheckOutTime = CheckOutTime
        this.CustomerId = CustomerId
        this.Status = Status
        this.StoreId = StoreId
    }


}