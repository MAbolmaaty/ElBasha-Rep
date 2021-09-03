package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class InvoiceModel implements Serializable {
    @SerializedName("$id")
    private String m$id;

    @SerializedName("Id")
    private int mId;

    @SerializedName("ClientName")
    private String mClientName;

    @SerializedName("Created_Date")
    private String mCreatedDate;

    @SerializedName("DelgateMan")
    private String mDelgateMan;

    @SerializedName("invoice_number")
    private String mInvoiceNumber;


    @SerializedName("total")
    private double total;


    @SerializedName("tax")
    private double tax;

    @SerializedName("totalAfter")
    private double totalAfter;

    @SerializedName("totalAfterDiscount")
    private double totalAfterDiscount;


    @SerializedName("discount")
    private double discount;

    @SerializedName("paid")
    private double paid;


    @SerializedName("un_paid")
    private double unPaid;


    @SerializedName("is_cash")
    private boolean isCash;


    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAfterDiscount() {
        return totalAfterDiscount;
    }

    public void setTotalAfterDiscount(double totalAfterDiscount) {
        this.totalAfterDiscount = totalAfterDiscount;
    }

    public String get$id() {
        return m$id;
    }

    public void set$id(String m$id) {
        this.m$id = m$id;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getClientName() {
        return mClientName;
    }

    public void setClientName(String mClientName) {
        this.mClientName = mClientName;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(String mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
    }

    public String getDelgateMan() {
        return mDelgateMan;
    }

    public void setDelgateMan(String mDelgateMan) {
        this.mDelgateMan = mDelgateMan;
    }

    public String getInvoiceNumber() {
        return mInvoiceNumber;
    }

    public void setInvoiceNumber(String mInvoiceNumber) {
        this.mInvoiceNumber = mInvoiceNumber;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalAfter() {
        return totalAfter;
    }

    public void setTotalAfter(double totalAfter) {
        this.totalAfter = totalAfter;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getUnPaid() {
        return unPaid;
    }

    public void setUnPaid(double unPaid) {
        this.unPaid = unPaid;
    }

    public boolean isCash() {
        return isCash;
    }

    public void setCash(boolean cash) {
        isCash = cash;
    }



}
