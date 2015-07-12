package com.hackatoncivico.rankingpolitico.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.hackatoncivico.rankingpolitico.R;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

/**
 * Created by franzueto on 6/5/15.
 */
public class Utils {

    public static final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
    public static final long MILLSECS_PER_HOUR = 60 * 60 * 1000; //Milisegundos por hora
    public static final long MILLSECS_PER_MINUTE = 60 * 1000; //Milisegundos por minuto

    public static final String PARSE_MAIN_BANNER_TYPE = "MAIN_BANNER";
    public static final String PARSE_THIN_BANNER_TYPE = "THIN_BANNER";
    public static final int BANNER_TIME_DELAY = 3000;

    public static final String APP_PACKAGE = "com.muniguate.carrerasmuni.";
    public static final String TABS_STATE = APP_PACKAGE + "TABS_STATE";
    public static final String COMMUNITY_TAB_SPINNER_STATE = APP_PACKAGE + "COMMUNITY_TAB_SPINNER_STATE";
    public static final String TAB4_REFRESH_CALENDAR = APP_PACKAGE + "TAB4_REFRESH_CALENDAR";

    public static final String FACEBOOK_LARGE_PICTURE = "https://graph.facebook.com/%s/picture?type=large";
    public static final String FACEBOOK_MEDIUM_PICTURE = "https://graph.facebook.com/%s/picture?type=normal";
    public static final String FACEBOOK_SMALL_PICTURE = "https://graph.facebook.com/%s/picture?type=small";
    public static final String FACEBOOK_SQUARE_PICTURE = "https://graph.facebook.com/%s/picture?type=square";

    public static String zeroFormatDate(long value){
        String zeroFormat = String.valueOf(value);

        if(value < 10){
            zeroFormat = "0" + zeroFormat;
        }

        return zeroFormat;
    }

    public static String zeroFormatDate(int value){
        String zeroFormat = String.valueOf(value);

        if(value < 10){
            zeroFormat = "0" + zeroFormat;
        }

        return zeroFormat;
    }

    public static long getDiasFaltantes(long timeStart, long timeEnd){
        return (timeStart - timeEnd) / MILLSECS_PER_DAY;
    }

    public static long getHorasFaltantes(long timeStart, long timeEnd){
        return ((timeStart - timeEnd) % MILLSECS_PER_DAY) / MILLSECS_PER_HOUR;
    }

    public static long getMinutosFaltantes(long timeStart, long timeEnd){
        return (((timeStart - timeEnd) % MILLSECS_PER_DAY) % MILLSECS_PER_HOUR) / MILLSECS_PER_MINUTE;
    }

    public static View.OnClickListener setNextScreenListener(final Context context, final Class nextScreen, final String objectKey, final String objectId){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Create an Intent that will start the login activity.
                Intent intent = new Intent(context, nextScreen);
                intent.putExtra(objectKey, objectId);
                context.startActivity(intent);
            }
        };
    }

    public static View.OnClickListener setNextScreenListener(final Context context, final Class nextScreen, final List<Pair<String, Object>> parameters){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Create an Intent that will start the login activity.
                Intent intent = new Intent(context, nextScreen);

                Iterator<Pair<String, Object>> paramItr = parameters.iterator();

                while (paramItr.hasNext()){
                    Pair<String, Object> parameter = paramItr.next();
                    if(parameter.second instanceof String){
                        intent.putExtra(parameter.first, parameter.second.toString());
                    } else if(parameter.second instanceof Integer){
                        intent.putExtra(parameter.first, (Integer) parameter.second);
                    } else if(parameter.second instanceof Boolean){
                        intent.putExtra(parameter.first, ((Boolean) parameter.second).booleanValue());
                    }
                }
                context.startActivity(intent);
            }
        };
    }

    public static View.OnClickListener setNextScreenListenerAndClear(final Context context, final Class nextScreen, final String objectKey, final String objectId){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Create an Intent that will start the login activity.
                Intent intent = new Intent(context, nextScreen);
                intent.putExtra(objectKey, objectId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        };
    }

    public static void savePreference(Context context, String key, Object value){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(value instanceof Integer){
            editor.putInt(key, (Integer)value);
        } else if(value instanceof String){
            editor.putString(key, value.toString());
        } else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean)value);
        } else{
            return;
        }

        editor.commit();
    }

    public static void removePreference(Context context, String preference){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(preference);
        editor.commit();
    }

    public static void clearPreferences(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    public static String getTWPictureBigger(String pictureUrl){
        return pictureUrl.replace("_normal", "_bigger");
    }

    public static String getTWPictureMini(String pictureUrl){
        return pictureUrl.replace("_normal", "_mini");
    }

    public static String getTWPictureOriginal(String pictureUrl){
        return pictureUrl.replace("_normal", "");
    }

    public static void openUrl(Context context, String url){
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void shareText(Context context, String text){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.send_to)));
    }

}
