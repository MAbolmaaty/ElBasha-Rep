
package codeztalk.elbasha.delegate.aPIS.requests;

 import com.google.gson.annotations.SerializedName;


public class AddLocationRequest {

    @SerializedName("EmpId")
    private String empId;
    @SerializedName("GPSLocation")
    private String gPSLocation;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getGPSLocation() {
        return gPSLocation;
    }

    public void setGPSLocation(String gPSLocation) {
        this.gPSLocation = gPSLocation;
    }

}
