
package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class EmployeeLocation implements Serializable {

    @Expose
    private String $id;
    @SerializedName("ActionDate")
    private String actionDate;
    @SerializedName("EmpId")
    private int empId;
    @SerializedName("EmpName")
    private String empName;
    @SerializedName("GPSLocation")
    private String gPSLocation;
    @SerializedName("Id")
    private int id;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getGPSLocation() {
        return gPSLocation;
    }

    public void setGPSLocation(String gPSLocation) {
        this.gPSLocation = gPSLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
