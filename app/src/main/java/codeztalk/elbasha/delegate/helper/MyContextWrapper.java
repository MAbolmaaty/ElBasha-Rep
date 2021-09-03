package codeztalk.elbasha.delegate.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;

import java.util.Locale;

public class MyContextWrapper extends ContextWrapper {

    public MyContextWrapper(Context base) {
        super(base);
    }


   public static void changeLanguage(Context context,String language)
    {

        Locale locale = new Locale(language);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        context.createConfigurationContext(configuration);
    }


    public static ContextWrapper wrap(Context context, String language)
    {


        /*String language = SharedPrefUtil.getInstance(this).read(LOCALE, Locale.getDefault().getLanguage());
        Locale locale = new Locale(language);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        createConfigurationContext(configuration);*/

        Resources resources = context.getResources();
        Configuration config =resources.getConfiguration();

        if (!language.equals(""))
        {
            Log.e("language",">>>"+language);

            Locale locale = new Locale(language);
            Locale.setDefault(locale);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
                Log.e("sdk is ",">>>"+language);

            } else {
                setSystemLocaleLegacy(config, locale);

                Log.e("sdk not is ",">>>"+language);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context = context.createConfigurationContext(config);
                Log.e("sdk 22 is ",">>>"+language);

            } else {
                 Log.e("sdk is 22 not ",">>>"+language);

                resources.updateConfiguration(config, resources.getDisplayMetrics());

            }

         }
        return new MyContextWrapper(context);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void forceRTLIfSupported(Activity context, boolean replace)
    {
        if (new PreferenceHelper(context).getLanguageID() == 1) {
            context.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            context.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void forceRTLIfSupported(Activity context, Dialog dialog)
    {
        if (new PreferenceHelper(context).getLanguageID() == 1) {
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void forceRTLIfSupported(Context context, Dialog dialog)
    {
        if (new PreferenceHelper(context).getLanguageID() == 1) {
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale){
        config.locale = locale;
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }




    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale){
        config.setLocale(locale);
    }







}