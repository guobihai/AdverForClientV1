package com.smtlibrary;

import android.app.Application;
import android.os.Build;
import android.webkit.WebSettings;

import com.google.code.microlog4android.config.PropertyConfigurator;
import com.smtlibrary.utils.CrashHandler;

/**
 * Created by gbh on 16/9/14.
 */
public class SmtApplication extends Application {
    private static SmtApplication smtApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        smtApplication = this;
        PropertyConfigurator.getConfigurator(this).configure();
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
    }

    public static SmtApplication getSmtApplication() {
        return smtApplication;
    }


    private String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(smtApplication.getApplicationContext());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
