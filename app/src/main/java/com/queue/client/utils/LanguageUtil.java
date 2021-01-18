package com.queue.client.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtil {
    /**
     * 获取手机设置的语言国家
     *
     * @param context
     * @return
     */
    public static String getCountry(Context context) {

        String country;
        Resources resources = context.getResources();
        //在7.0以上和7.0一下获取国家的方式有点不一样
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  大于等于24即为7.0及以上执行内容
            country = resources.getConfiguration().getLocales().get(0).getCountry();
        } else {
            //  低于24即为7.0以下执行内容
            country = resources.getConfiguration().locale.getCountry();
        }

        return country;
    }

    /**
     * 初始化国际化语言，繁体字和简体字
     */
    private void initI18(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (LanguageUtil.getCountry(context.getApplicationContext()).equals("TW")) {
            config.locale = Locale.TAIWAN;
        } else {
            config.locale = Locale.CHINESE;
        }
        resources.updateConfiguration(config, dm);
    }


}
