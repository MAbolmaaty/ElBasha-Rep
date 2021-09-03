package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ClientModel implements Serializable {

    private int offID;

    @SerializedName("$id")
    private String m$id;
    @SerializedName("Client_Days")
    private List<Integer> mClientDays;
    @SerializedName("ClientName")
    private String mClientName;
    @SerializedName("Created_Date")
    private String mCreatedDate;
    @SerializedName("DelgateMan")
    private String mDelgateMan;
    @SerializedName("GPSLocation")
    private String mGPSLocation;
    @SerializedName("Id")
    private int mId;
    @SerializedName("Mobile1")
    private String mMobile1;
    @SerializedName("Mobile2")
    private String mMobile2;


    public ClientModel(int offID, int mId, String mClientName) {
        this.mClientName = mClientName;
        this.mId = mId;
        this.offID = offID;
    }

    public ClientModel(int mId, String mClientName, String mDelgateMan, String mGPSLocation, String mMobile1, String mMobile2) {
        this.mClientName = mClientName;
        this.mDelgateMan = mDelgateMan;
        this.mGPSLocation = mGPSLocation;
        this.mId = mId;
        this.mMobile1 = mMobile1;
        this.mMobile2 = mMobile2;
    }

    public String get$id() {
        return m$id;
    }

    public void set$id(String $id) {
        m$id = $id;
    }

    public List<Integer> getClientDays() {
        return mClientDays;
    }

    public void setClientDays(List<Integer> clientDays) {
        mClientDays = clientDays;
    }

    public String getClientName() {
        return mClientName;
    }

    public void setClientName(String clientName) {
        mClientName = clientName;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        mCreatedDate = createdDate;
    }

    public String getDelgateMan() {
        return mDelgateMan;
    }

    public void setDelgateMan(String delgateMan) {
        mDelgateMan = delgateMan;
    }

    public String getGPSLocation() {
        return mGPSLocation;
    }

    public void setGPSLocation(String gPSLocation) {
        mGPSLocation = gPSLocation;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getMobile1() {
        return mMobile1;
    }

    public void setMobile1(String mobile1) {
        mMobile1 = mobile1;
    }

    public String getMobile2() {
        return mMobile2;
    }

    public void setMobile2(String mobile2) {
        mMobile2 = mobile2;
    }



}
