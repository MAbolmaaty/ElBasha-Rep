package codeztalk.elbasha.delegate.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import codeztalk.elbasha.delegate.models.ConnectedDevice;

public class PreferenceHelper {

    private final String isLogged = "isUserLogged";
    private final String LanguageID = "lang_id";
    private final String userId = "userId";
    private final String empName = "empName";
    private final String userName = "userName";
    private final String userPhone = "userPhone";
    private final String userAddress = "userAddress";
    private final String userEmail = "userEmail";
    private final String userPhoto = "userPhoto";

    private final String activated = "activated";

    private final String totalPrice = "totalPrice";
    private final String userToken = "userToken";
    private final String userLocation = "userLocation";
    private final String userTime = "userTime";
    private final String userDate = "userDate";
    private final String updateDate = "updateDate";

    private final String paperHeight = "paperHeight";


    private SharedPreferences app_prefs;

    public PreferenceHelper(Context context) {
        try {
            String app_ref = "force_sms";
            app_prefs = context.getSharedPreferences(app_ref,
                    Context.MODE_PRIVATE);
        } catch (NullPointerException e) {
            Log.e("exception", "01232");

        }

    }


    public int getPaperHeight() {
        return app_prefs.getInt(paperHeight, 250);
    }

    public void setPaperHeight(int cityID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putInt(paperHeight, cityID);
        edit.apply();
    }


    public long getUserDate() {

        return app_prefs.getLong(userDate, 0);
    }


    public void setUserDate(long cityID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putLong(userDate, cityID);
        edit.apply();
    }

    public long getUpdateDate() {

        return app_prefs.getLong(updateDate, 0);
    }


    public void setUpdateDate(long cityID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putLong(updateDate, cityID);
        edit.apply();
    }

    public boolean getActivated() {
        return app_prefs.getBoolean(activated, false);
    }

    public void setActivated(boolean userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(activated, userID);
        edit.apply();
    }

    public String getUserLocation() {
        return app_prefs.getString(userLocation, "");
    }

    public void setUserLocation(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userLocation, userID);
        edit.apply();
    }

    public String getUserTime() {
        return app_prefs.getString(userTime, "");
    }

    public void setUserTime(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userTime, userID);
        edit.apply();
    }

    public String getUserId() {
        return app_prefs.getString(userId, "");
    }

    public void setUserId(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userId, userID);
        edit.apply();
    }

    public String getTotalPrice() {
        return app_prefs.getString(totalPrice, "");
    }

    public void setTotalPrice(String total_Price) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(totalPrice, total_Price);
        edit.apply();
    }


    public String getUserToken() {
        return app_prefs.getString(userToken, "");
    }

    public void setUserToken(String token) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userToken, token);
        edit.apply();
    }

    public String getEmpName() {
        return app_prefs.getString(empName, "");
    }

    public void setEmpName(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(empName, userID);
        edit.apply();
    }



    public String getUserName() {
        return app_prefs.getString(userName, "");
    }

    public void setUserName(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userName, userID);
        edit.apply();
    }


    public String getUserEmail() {
        return app_prefs.getString(userEmail, "");
    }

    public void setUserEmail(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userEmail, userID);
        edit.apply();
    }
    public String getUserPhoto() {
        return app_prefs.getString(userPhoto, "");
    }

    public void setUserPhoto(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userPhoto, userID);
        edit.apply();
    }

    public String getUserPhone() {
        return app_prefs.getString(userPhone, "");
    }

    public void setUserPhone(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userPhone, userID);
        edit.apply();
    }


    public String getUserAddress() {
        return app_prefs.getString(userAddress, "");
    }

    public void setUserAddress(String userID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userAddress, userID);
        edit.apply();
    }





    public int getLanguageID() {
        return app_prefs.getInt(LanguageID, 1);
    }

    public void setLanguageID(int cityID) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putInt(LanguageID, cityID);
        edit.apply();
    }

    public String getAppLanguage() {
        if (getLanguageID() == 0) {
            return "en";
        } else return "ar";

    }



    public boolean isUserLogged() {
        return app_prefs.getBoolean(isLogged, false);
    }

    public void setUserLogged(boolean is_logged) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(isLogged, is_logged);
        edit.apply();
    }

    public void logOut() {

        setUserLogged(false);
        setUserName("");
        setUserId("");
        setUserToken("");
        setUserAddress("");
        setUserEmail("");
        setUserPhone("");

    }

    public void setPrinter(ConnectedDevice printer){
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(constants.PRINTER_NAME, printer.getName());
        edit.putString(constants.PRINTER_MAC_ADDRESS, printer.getMacAddress());
        edit.apply();
    }

    public ConnectedDevice getPrinter(){
        ConnectedDevice connectedDevice = null;
        String name = app_prefs.getString(constants.PRINTER_NAME, null);
        String macAddress = app_prefs.getString(constants.PRINTER_MAC_ADDRESS, null);
        if (name != null && macAddress != null){
            connectedDevice = new ConnectedDevice(name, macAddress);
        }
        return connectedDevice;
    }
}
