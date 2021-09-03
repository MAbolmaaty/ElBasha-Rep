package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Report7Model {

    @Expose
    private String $id;
    @SerializedName("Client_Id")
    private int clientId;
    @SerializedName("ClientName")
    private String clientName;
    @SerializedName("EmpName")
    private String empName;
    @SerializedName("Id")
    private int id;
    @SerializedName("InvoiceId")
    private int invoiceId;
    @SerializedName("IsShifted")
    private Object isShifted;
    @SerializedName("IssuedBy")
    private int issuedBy;
    @SerializedName("IssuedDate")
    private String issuedDate;
    @SerializedName("Notes")
    private String notes;
    @SerializedName("PaidValue")
    private Double paidValue;
    @SerializedName("Reminder")
    private Double reminder;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Object getIsShifted() {
        return isShifted;
    }

    public void setIsShifted(Object isShifted) {
        this.isShifted = isShifted;
    }

    public int getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(int issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(Double paidValue) {
        this.paidValue = paidValue;
    }

    public Double getReminder() {
        return reminder;
    }

    public void setReminder(Double reminder) {
        this.reminder = reminder;
    }

}
