package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PocketModel
{
    @Expose
    private String $id;
    @SerializedName("EmpId")
    private int empId;
    @SerializedName("Id")
    private int id;
    @SerializedName("Notes")
    private String notes;
    @SerializedName("TransactionAmount")
    private Double transactionAmount;
    @SerializedName("TransactionDate")
    private String transactionDate;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
