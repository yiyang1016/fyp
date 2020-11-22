package com.example.covid_19shoppingcentre.Common;

import android.content.Context;

import com.example.covid_19shoppingcentre.R;

import java.io.File;

public class ReportCommonJava {
    public static String getAppPath(Context context){
        File dir = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator
                + context.getResources().getString(R.string.app_name)
                +File.separator
        );
        if(!dir.exists())
            dir.mkdir();
        return dir.getPath()+File.separator;
    }
}
