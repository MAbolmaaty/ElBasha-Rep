package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Report8Model
{
    @Expose
    private String $id;
    @SerializedName("ActionDate")
    private String actionDate;
    @SerializedName("ClientName")
    private String clientName;
    @SerializedName("EmpName")
    private String empName;
    @SerializedName("FinalValue")
    private Double finalValue;
    @SerializedName("Id")
    private Long id;
    @SerializedName("InvoiceTaxNumber")
    private String invoiceTaxNumber;
    @SerializedName("PaidValue")
    private Double paidValue;
    @SerializedName("RemainValue")
    private Double remainValue;

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Double getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(Double finalValue) {
        this.finalValue = finalValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceTaxNumber() {
        return invoiceTaxNumber;
    }

    public void setInvoiceTaxNumber(String invoiceTaxNumber) {
        this.invoiceTaxNumber = invoiceTaxNumber;
    }

    public Double getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(Double paidValue) {
        this.paidValue = paidValue;
    }

    public Double getRemainValue() {
        return remainValue;
    }

    public void setRemainValue(Double remainValue) {
        this.remainValue = remainValue;
    }

}
