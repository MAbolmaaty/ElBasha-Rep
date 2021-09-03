package codeztalk.elbasha.delegate.aPIS.requests;

import com.google.gson.annotations.SerializedName;

public class ClientRequest
{


    @SerializedName("Id")
    private String Id;

    @SerializedName("ClientName")
    private String mClientName;
    @SerializedName("DelgateMan")
    private String mDelgateMan;
    @SerializedName("EmployeeId")
    private String mEmployeeId;
    @SerializedName("GPSLocation")
    private String mGPSLocation;
    @SerializedName("Mobile1")
    private String mMobile1;
    @SerializedName("Mobile2")
    private String mMobile2;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getClientName() {
        return mClientName;
    }

    public void setClientName(String clientName) {
        mClientName = clientName;
    }

    public String getDelgateMan() {
        return mDelgateMan;
    }

    public void setDelgateMan(String delgateMan) {
        mDelgateMan = delgateMan;
    }

    public String getEmployeeId() {
        return mEmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        mEmployeeId = employeeId;
    }

    public String getGPSLocation() {
        return mGPSLocation;
    }

    public void setGPSLocation(String gPSLocation) {
        mGPSLocation = gPSLocation;
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
