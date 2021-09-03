package codeztalk.elbasha.delegate.aPIS.requests;

import com.google.gson.annotations.SerializedName;

public class ReportRequest
{

    @SerializedName("EmpId")
    private String empId;
    @SerializedName("EndDate")
    private String endDate;
    @SerializedName("StartDate")
    private String startDate;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
