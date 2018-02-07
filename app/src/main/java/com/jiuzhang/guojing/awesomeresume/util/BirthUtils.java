package com.jiuzhang.guojing.awesomeresume.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jinghao Qiao on 2018/2/6.
 */

public class BirthUtils {
    private static SimpleDateFormat sbf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public static String birthToString(Date birth){
        return sbf.format(birth);
    }

    public static Date stringToBirth(String string){
        try{
            return sbf.parse(string);
        }catch (Exception e){
            e.printStackTrace();
            return new Date(0);
        }
    }
}
