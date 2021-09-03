
package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Report9Model {

    @Expose
    private String $id;
    @SerializedName("ActionDate")
    private String actionDate;
    @SerializedName("Actiontype")
    private String actiontype;
    @SerializedName("Amount")
    private Double amount;
    @SerializedName("CurrentBalance")
    private Double currentBalance;
    @SerializedName("Notes")
    private String notes;
    @SerializedName("PreviousBalance")
    private Double previousBalance;

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

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(Double previousBalance) {
        this.previousBalance = previousBalance;
    }

}
