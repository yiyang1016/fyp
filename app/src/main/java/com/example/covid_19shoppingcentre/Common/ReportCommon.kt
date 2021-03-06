@file:Suppress("DEPRECATION")

package com.example.covid_19shoppingcentre.Common

import java.io.File
import android.content.Context
import com.example.covid_19shoppingcentre.R

object ReportCommon {
        fun getAppPath(context: Context):String{
            val dir = File(android.os.Environment.getExternalStorageDirectory().toString()
                    +File.separator
                    +context.resources.getString(R.string.app_name)
                    +File.separator)
            if(!dir.exists())
                dir.mkdir()
            return dir.path+File.separator
        }
}