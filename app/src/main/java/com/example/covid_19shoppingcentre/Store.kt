package com.example.covid_19shoppingcentre

class Store {
    var Store_Floor: String?=null
    var Store_Limitation: Int? = null
    var Store_Name: String? = null
    var Store_Slot : String? = null
    var Store_Image : String? = null

    constructor(){
    }

    constructor(
        Store_Floor: String?,
        Store_Limitation: Int?,
        Store_Name: String?,
        Store_Slot: String?,
        Store_Image: String?
    ) {
        this.Store_Floor = Store_Floor
        this.Store_Limitation = Store_Limitation
        this.Store_Name = Store_Name
        this.Store_Slot = Store_Slot
        this.Store_Image = Store_Image
    }
}