
package codeztalk.elbasha.delegate.aPIS.requests;

 import com.google.gson.annotations.SerializedName;


public class AddCreditRequest {

    @SerializedName("Client_Id")
    private String mClientId;
    @SerializedName("InvoiceId")
    private String mInvoiceId;
    @SerializedName("IssuedBy")
    private String mIssuedBy;
    @SerializedName("IssuedDate")
    private String mIssuedDate;
    @SerializedName("Notes")
    private String mNotes;
    @SerializedName("PaidValue")
    private String mPaidValue;

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    public String getInvoiceId() {
        return mInvoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        mInvoiceId = invoiceId;
    }

    public String getIssuedBy() {
        return mIssuedBy;
    }

    public void setIssuedBy(String issuedBy) {
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

    public String getPaidValue() {
        return mPaidValue;
    }

    public void setPaidValue(String paidValue) {
        mPaidValue = paidValue;
    }

}
