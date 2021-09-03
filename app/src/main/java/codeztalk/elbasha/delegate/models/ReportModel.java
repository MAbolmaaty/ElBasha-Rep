package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.SerializedName;

public class ReportModel
{
    @SerializedName("$id")
    private String m$id;
    @SerializedName("ActionDate")
    private String mActionDate;
    @SerializedName("CreditAmount")
    private Double mCreditAmount;
    @SerializedName("InvoicesAmount")
    private Double mInvoicesAmount;
    @SerializedName("InvoicesCashedAmount")
    private Double mInvoicesCashedAmount;
    @SerializedName("InvoicesCreditAmount")
    private Double mInvoicesCreditAmount;
    @SerializedName("InvoicesQty")
    private int mInvoicesQty;
    @SerializedName("TotalCollectedAmount")
    private Double mTotalCollectedAmount;

    public String get$id() {
        return m$id;
    }

    public void set$id(String $id) {
        m$id = $id;
    }

    public String getActionDate() {
        return mActionDate;
    }

    public void setActionDate(String actionDate) {
        mActionDate = actionDate;
    }

    public Double getCreditAmount() {
        return mCreditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        mCreditAmount = creditAmount;
    }

    public Double getInvoicesAmount() {
        return mInvoicesAmount;
    }

    public void setInvoicesAmount(Double invoicesAmount) {
        mInvoicesAmount = invoicesAmount;
    }

    public Double getInvoicesCashedAmount() {
        return mInvoicesCashedAmount;
    }

    public void setInvoicesCashedAmount(Double invoicesCashedAmount) {
        mInvoicesCashedAmount = invoicesCashedAmount;
    }

    public Double getInvoicesCreditAmount() {
        return mInvoicesCreditAmount;
    }

    public void setInvoicesCreditAmount(Double invoicesCreditAmount) {
        mInvoicesCreditAmount = invoicesCreditAmount;
    }

    public int getInvoicesQty() {
        return mInvoicesQty;
    }

    public void setInvoicesQty(int invoicesQty) {
        mInvoicesQty = invoicesQty;
    }

    public Double getTotalCollectedAmount() {
        return mTotalCollectedAmount;
    }

    public void setTotalCollectedAmount(Double totalCollectedAmount) {
        mTotalCollectedAmount = totalCollectedAmount;
    }

}
