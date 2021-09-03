
package codeztalk.elbasha.delegate.aPIS.responses;

import com.google.gson.annotations.SerializedName;


public class AddCreditResponse {

    @SerializedName("$id")
    private String m$id;
    @SerializedName("Client")
    private Object mClient;
    @SerializedName("Client_Id")
    private int mClientId;
    @SerializedName("Employee")
    private Object mEmployee;
    @SerializedName("Id")
    private int mId;
    @SerializedName("Invoice")
    private Object mInvoice;
    @SerializedName("InvoiceId")
    private int mInvoiceId;
    @SerializedName("IsShifted")
    private Object mIsShifted;
    @SerializedName("IssuedBy")
    private int mIssuedBy;
    @SerializedName("IssuedDate")
    private String mIssuedDate;
    @SerializedName("Notes")
    private String mNotes;
    @SerializedName("PaidValue")
    private Double mPaidValue;

    public String get$id() {
        return m$id;
    }

    public void set$id(String $id) {
        m$id = $id;
    }

    public Object getClient() {
        return mClient;
    }

    public void setClient(Object client) {
        mClient = client;
    }

    public int getClientId() {
        return mClientId;
    }

    public void setClientId(int clientId) {
        mClientId = clientId;
    }

    public Object getEmployee() {
        return mEmployee;
    }

    public void setEmployee(Object employee) {
        mEmployee = employee;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Object getInvoice() {
        return mInvoice;
    }

    public void setInvoice(Object invoice) {
        mInvoice = invoice;
    }

    public int getInvoiceId() {
        return mInvoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        mInvoiceId = invoiceId;
    }

    public Object getIsShifted() {
        return mIsShifted;
    }

    public void setIsShifted(Object isShifted) {
        mIsShifted = isShifted;
    }

    public int getIssuedBy() {
        return mIssuedBy;
    }

    public void setIssuedBy(int issuedBy) {
        mIssuedBy = issuedBy;
    }

    public String getIssuedDate() {
        return mIssuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        mIssuedDate = issuedDate;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public Double getPaidValue() {
        return mPaidValue;
    }

    public void setPaidValue(Double paidValue) {
        mPaidValue = paidValue;
    }

}
