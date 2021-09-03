package codeztalk.elbasha.delegate.aPIS.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddInvoiceResponse
{
    @SerializedName("$id")
    private String m$id;
    @SerializedName("ActionDate")
    private String mActionDate;
    @SerializedName("Client")
    private Object mClient;
    @SerializedName("ClientId")
    private Long mClientId;
    @SerializedName("ClientRecipts")
    private List<Object> mClientRecipts;
    @SerializedName("Discount")
    private Double mDiscount;
    @SerializedName("Employee")
    private Object mEmployee;
    @SerializedName("FinalValue")
    private Double mFinalValue;
    @SerializedName("GPSLocation")
    private String mGPSLocation;
    @SerializedName("Id")
    private Long mId;
    @SerializedName("InvoiceProducts")
    private List<InvoiceProduct> mInvoiceProducts;
    @SerializedName("InvoiceTaxNumber")
    private String mInvoiceTaxNumber;
    @SerializedName("IssuedBy")
    private Long mIssuedBy;
    @SerializedName("Notes")
    private String mNotes;
    @SerializedName("PaidValue")
    private Double mPaidValue;
    @SerializedName("RemainValue")
    private Double mRemainValue;
    @SerializedName("TaxValue")
    private Double mTaxValue;
    @SerializedName("TotalAfterDiscount")
    private Double mTotalAfterDiscount;
    @SerializedName("TotalValue")
    private Double mTotalValue;

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

    public Object getClient() {
        return mClient;
    }

    public void setClient(Object client) {
        mClient = client;
    }

    public Long getClientId() {
        return mClientId;
    }

    public void setClientId(Long clientId) {
        mClientId = clientId;
    }

    public List<Object> getClientRecipts() {
        return mClientRecipts;
    }

    public void setClientRecipts(List<Object> clientRecipts) {
        mClientRecipts = clientRecipts;
    }

    public Double getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Double discount) {
        mDiscount = discount;
    }

    public Object getEmployee() {
        return mEmployee;
    }

    public void setEmployee(Object employee) {
        mEmployee = employee;
    }

    public Double getFinalValue() {
        return mFinalValue;
    }

    public void setFinalValue(Double finalValue) {
        mFinalValue = finalValue;
    }

    public String getGPSLocation() {
        return mGPSLocation;
    }

    public void setGPSLocation(String gPSLocation) {
        mGPSLocation = gPSLocation;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
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

    public Long getIssuedBy() {
        return mIssuedBy;
    }

    public void setIssuedBy(Long issuedBy) {
        mIssuedBy = issuedBy;
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

    public Double getRemainValue() {
        return mRemainValue;
    }

    public void setRemainValue(Double remainValue) {
        mRemainValue = remainValue;
    }

    public Double getTaxValue() {
        return mTaxValue;
    }

    public void setTaxValue(Double taxValue) {
        mTaxValue = taxValue;
    }

    public Double getTotalAfterDiscount() {
        return mTotalAfterDiscount;
    }

    public void setTotalAfterDiscount(Double totalAfterDiscount) {
        mTotalAfterDiscount = totalAfterDiscount;
    }

    public Double getTotalValue() {
        return mTotalValue;
    }

    public void setTotalValue(Double totalValue) {
        mTotalValue = totalValue;
    }


    public static class InvoiceProduct {

        @SerializedName("$id")
        private String m$id;
        @SerializedName("BoxPrice")
        private Double mBoxPrice;
        @SerializedName("Id")
        private Long mId;
        @SerializedName("Invoice")
        private Invoice mInvoice;
        @SerializedName("IsByBox")
        private Boolean mIsByBox;
        @SerializedName("Product")
        private Object mProduct;
        @SerializedName("ProductId")
        private Long mProductId;
        @SerializedName("QTY")
        private Long mQTY;
        @SerializedName("TotalPrice")
        private Double mTotalPrice;
        @SerializedName("Transaction_Id")
        private Long mTransactionId;
        @SerializedName("UnitPrice")
        private Double mUnitPrice;

        public String get$id() {
            return m$id;
        }

        public void set$id(String $id) {
            m$id = $id;
        }

        public Double getBoxPrice() {
            return mBoxPrice;
        }

        public void setBoxPrice(Double boxPrice) {
            mBoxPrice = boxPrice;
        }

        public Long getId() {
            return mId;
        }

        public void setId(Long id) {
            mId = id;
        }

        public Invoice getInvoice() {
            return mInvoice;
        }

        public void setInvoice(Invoice invoice) {
            mInvoice = invoice;
        }

        public Boolean getIsByBox() {
            return mIsByBox;
        }

        public void setIsByBox(Boolean isByBox) {
            mIsByBox = isByBox;
        }

        public Object getProduct() {
            return mProduct;
        }

        public void setProduct(Object product) {
            mProduct = product;
        }

        public Long getProductId() {
            return mProductId;
        }

        public void setProductId(Long productId) {
            mProductId = productId;
        }

        public Long getQTY() {
            return mQTY;
        }

        public void setQTY(Long qTY) {
            mQTY = qTY;
        }

        public Double getTotalPrice() {
            return mTotalPrice;
        }

        public void setTotalPrice(Double totalPrice) {
            mTotalPrice = totalPrice;
        }

        public Long getTransactionId() {
            return mTransactionId;
        }

        public void setTransactionId(Long transactionId) {
            mTransactionId = transactionId;
        }

        public Double getUnitPrice() {
            return mUnitPrice;
        }

        public void setUnitPrice(Double unitPrice) {
            mUnitPrice = unitPrice;
        }

        public class Invoice {

            @SerializedName("$ref")
            private String m$ref;

            public String get$ref() {
                return m$ref;
            }

            public void set$ref(String $ref) {
                m$ref = $ref;
            }

        }

    }

}
