package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EmployeeModel implements Serializable {

    private int offID;


    @Expose
    private String $id;
    @SerializedName("Email")
    private String email;
    @SerializedName("EmpAddress")
    private String empAddress;
    @SerializedName("EmpName")
    private String empName;
    @Expose
    private List<EmployeeLocation> employeeLocations;
    @SerializedName("Id")
    private int id;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("UserName")
    private String userName;



    @SerializedName("Region_Id")
    private int regionId;
    @SerializedName("Region_Name_AR")
    private String regionNameAR;
    @SerializedName("Region_Name_EN")
    private Object regionNameEN;
    @SerializedName("ResidenceExpireDate")
    private String residenceExpireDate;
    @SerializedName("ResidenceImagePath")
    private String residenceImagePath;
    @SerializedName("ResidenceNumber")
    private String residenceNumber;
    @SerializedName("EmpImagePath")
    private String empImagePath;

    @SerializedName("PermessionImagePath")
    private String permessionImagePath;


    public EmployeeModel(int offID, int id, String empName) {

        this.offID = offID;
        this.id = id;
        this.empName = empName;
    }

    public String getPermessionImagePath() {
        return permessionImagePath;
    }

    public void setPermessionImagePath(String permessionImagePath) {
        this.permessionImagePath = permessionImagePath;
    }

    public String getEmpImagePath() {
        return empImagePath;
    }

    public void setEmpImagePath(String empImagePath) {
        this.empImagePath = empImagePath;
    }

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public List<EmployeeLocation> getEmployeeLocations() {
        return employeeLocations;
    }

    public void setEmployeeLocations(List<EmployeeLocation> employeeLocations) {
        this.employeeLocations = employeeLocations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getOffID() {
        return offID;
    }

    public void setOffID(int offID) {
        this.offID = offID;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionNameAR() {
        return regionNameAR;
    }

    public void setRegionNameAR(String regionNameAR) {
        this.regionNameAR = regionNameAR;
    }

    public Object getRegionNameEN() {
        return regionNameEN;
    }

    public void setRegionNameEN(Object regionNameEN) {
        this.regionNameEN = regionNameEN;
    }

    public String getResidenceExpireDate() {
        return residenceExpireDate;
    }

    public void setResidenceExpireDate(String residenceExpireDate) {
        this.residenceExpireDate = residenceExpireDate;
    }

    public String getResidenceImagePath() {
        return residenceImagePath;
    }

    public void setResidenceImagePath(String residenceImagePath) {
        this.residenceImagePath = residenceImagePath;
    }

    public String getResidenceNumber() {
        return residenceNumber;
    }

    public void setResidenceNumber(String residenceNumber) {
        this.residenceNumber = residenceNumber;
    }
}
