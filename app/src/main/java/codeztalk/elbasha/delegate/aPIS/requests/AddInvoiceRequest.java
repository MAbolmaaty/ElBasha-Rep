package codeztalk.elbasha.delegate.aPIS.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AddInvoiceRequest {
    @SerializedName("ClientId")
    private String mClientId;
    @SerializedName("Discount")
    private String mDiscount;
    @SerializedName("FinalValue")
    private String mFinalValue;
    @SerializedName("GPSLocation")
    private String mGPSLocation;
    @SerializedName("InvoiceProducts")
    private List<InvoiceProduct> mInvoiceProducts;
    @SerializedName("InvoiceTaxNumber")
    private String mInvoiceTaxNumber;
    @SerializedName("IssuedBy")
    private String mIssuedBy;
    @SerializedName("Notes")
    private String mNotes;
    @SerializedName("PaidValue")
    private String mPaidValue;
    @SerializedName("RemainValue")
    private String mRemainValue;
    @SerializedName("TaxValue")
    private String mTaxValue;
    @SerializedName("TotalAfterDiscount")
    private String mTotalAfterDiscount;
    @SerializedName("TotalValue")
    private String mTotalValue;

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    public String getDiscount() {
        return mDiscount;
    }

    public void setDiscount(String discount) {
        mDiscount = discount;
    }

    public String getFinalValue() {
        return mFinalValue;
    }

    public void setFinalValue(String finalValue) {
        mFinalValue = finalValue;
    }

    public String getGPSLocation() {
        return mGPSLocation;
    }

    public void setGPSLocation(String gPSLocation) {
        mGPSLocation = gPSLocation;
    }

    public List<InvoiceProduct> getInvoiceProducts() {
        return mInvoiceProducts;
    }

    public void setInvoiceProducts(List<InvoiceProduct> invoiceProducts) {
        mInvoiceProducts = invoiceProducts;
    }

    public String getInvoiceTaxNumber() {
        return mInvoiceTaxNumber;
    }

    public void setInvoiceTaxNumber(String invoiceTaxNumber) {
        mInvoiceTaxNumber = invoiceTaxNumber;
    }

    public String getIssuedBy() {
        return mIssuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        mIssuedBy = issuedBy;
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

    public String getRemainValue() {
        return mRemainValue;
    }

    public void setRemainValue(String remainValue) {
        mRemainValue = remainValue;
    }

    public String getTaxValue() {
        return mTaxValue;
    }

    public void setTaxValue(String taxValue) {
        mTaxValue = taxValue;
    }

    public String getTotalAfterDiscount() {
        return mTotalAfterDiscount;
    }

    public void setTotalAfterDiscount(String totalAfterDiscount) {
        mTotalAfterDiscount = totalAfterDiscount;
    }

    public String getTotalValue() {
        return mTotalValue;
    }

    public void setTotalValue(String totalValue) {
        mTotalValue = totalValue;
    }




    public static class InvoiceProduct {

        @SerializedName("BoxPrice")
        private String mBoxPrice;
        @SerializedName("IsByBox")
        private String mIsByBox;
        @SerializedName("ProductId")
        private String mProductId;
        @SerializedName("QTY")
        private int mQTY;
        @SerializedName("TotalPrice")
        private String mTotalPrice;
        @SerializedName("UnitPrice")
        private String mUnitPrice;
        @SerializedName("OfficialUnitPrice")
        private String OfficialUnitPrice;

        public InvoiceProduct(String mProductId,
                              String mBoxPrice,
                              String mIsByBox,
                              int mQTY,
                              String mTotalPrice,
                              String mUnitPrice,
                              String OfficialUnitPrice) {
            this.mBoxPrice = mBoxPrice;
            this.mIsByBox = mIsByBox;
            this.mProductId = mProductId;
            this.mQTY = mQTY;
            this.mTotalPrice = mTotalPrice;
            this.mUnitPrice = mUnitPrice;
            this.OfficialUnitPrice = OfficialUnitPrice;
        }


        public String getOfficialUnitPrice() {
            return OfficialUnitPrice;
        }

        public void setOfficialUnitPrice(String officialUnitPrice) {
            OfficialUnitPrice = officialUnitPrice;
        }

        public String getBoxPrice() {
            return mBoxPrice;
        }

        public void setBoxPrice(String boxPrice) {
            mBoxPrice = boxPrice;
        }

        public String getIsByBox() {
            return mIsByBox;
        }

        public void setIsByBox(String isByBox) {
            mIsByBox = isByBox;
        }

        public String getProductId() {
            return mProductId;
        }

        public void setProductId(String productId) {
            mProductId = productId;
        }

        public int getQTY() {
            return mQTY;
        }

        public void setQTY(int qTY) {
            mQTY = qTY;
        }

        public String getTotalPrice() {
            return mTotalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            mTotalPrice = totalPrice;
        }

        public String getUnitPrice() {
            return mUnitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            mUnitPrice = unitPrice;
        }

    }


}
