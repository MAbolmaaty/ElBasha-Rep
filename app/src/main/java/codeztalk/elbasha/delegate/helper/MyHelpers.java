package codeztalk.elbasha.delegate.helper;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import codeztalk.elbasha.delegate.R;

public class MyHelpers {

    public static GradientDrawable makeCustomShape(int backgroundColor, int borderColor, int borderWidth, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(radius);
        shape.setColor(backgroundColor);
        shape.setStroke(borderWidth, borderColor);

        return shape;
    }

    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().matches("");
    }

    public static boolean isEmpty(TextView etText) {
        return etText.getText().toString().matches("");
    }

    public static void showNotification(Context context, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

//        stackBuilder.addNextIntent(intent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
//                0,
//                PendingIntent.FLAG_UPDATE_CURRENT
//        );
        //mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }


    @SuppressLint("NewApi")

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static String getCurrentDate() {
        Locale locale = new Locale("ar");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", locale);
        return sdf.format(date);
    }


    public static String getCurrentDateEN() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        return sdf.format(date);
    }

    public static String getPreviousMonth(String curDate) {
        final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        Date date = null;
        try {
            date = format.parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return format.format(calendar.getTime());
    }


    public static String getPreviousMonthAR(String curDate) {
        Locale locale = new Locale("ar");

        final SimpleDateFormat format = new SimpleDateFormat("dd-MMMM-yyyy", locale);
        Date date = null;
        try {
            date = format.parse(curDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        return format.format(calendar.getTime());
    }

    public static String formatSendingDate(String date) {

        String result;
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;
        Locale locale = new Locale("ar");

        try {
            sdf = new SimpleDateFormat("dd-MMMM-yyyy", locale);
            sdf1 = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            result = sdf1.format(Objects.requireNonNull(sdf.parse(date)));
        } catch (Exception e) {
            e.printStackTrace();
            return date;

        }
        return result;
    }


    public static String formatDateFromOneToAnother(String date, String given_format, String result_format) {

           Locale locale = new Locale("ar");
        //        Date date = new Date();
        //        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", locale);
        //        return sdf.format(date);

        String result;
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;


        try {
            sdf = new SimpleDateFormat(given_format, locale);
            sdf1 = new SimpleDateFormat(result_format, locale);
            result = sdf1.format(Objects.requireNonNull(sdf.parse(date)));
        } catch (Exception e) {
            e.printStackTrace();
            return date;

        }
        return result;
    }


    public static long milliseconds(String date)
    {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss",Locale.ENGLISH);
        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return 0;
    }



}
