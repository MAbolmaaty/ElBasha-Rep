package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ClientInvoiceModel implements Serializable {


    @SerializedName("$id")
    private String m$id;
    @SerializedName("ActionDate")
    private String mActionDate;
    @SerializedName("ClientId")
    private int mClientId;
    @SerializedName("ClientName")
    private String mClientName;
    @SerializedName("Discount")
    private Double mDiscount;
    @SerializedName("FinalValue")
    private Double mFinalValue;
    @SerializedName("GPSLocation")
    private String mGPSLocation;
    @SerializedName("Id")
    private int mId;
    @SerializedName("InvoiceTaxNumber")
    private String mInvoiceTaxNumber;
    @SerializedName("IsCredit")
    private Boolean mIsCredit;
    @SerializedName("IssuedBy")
    private int mIssuedBy;
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

    @SerializedName("Products")
    private List<Products> mProducts;



    public List<Products> getmProducts() {
        return mProducts;
    }

    public void setmProducts(List<Products> mProducts) {
        this.mProducts = mProducts;
    }

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

    public int getClientId() {
        return mClientId;
    }

    public void setClientId(int clientId) {
        mClientId = clientId;
    }

    public String getClientName() {
        return mClientName;
    }

    public void setClientName(String clientName) {
        mClientName = clientName;
    }

    public Double getDiscount() {
        return mDiscount;
    }

    public void setDiscount(Double discount) {
        mDiscount = discount;
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getInvoiceTaxNumber() {
        return mInvoiceTaxNumber;
    }

    public void setInvoiceTaxNumber(String invoiceTaxNumber) {
        mInvoiceTaxNumber = invoiceTaxNumber;
    }

    public Boolean getIsCredit() {
        return mIsCredit;
    }

    public void setIsCredit(Boolean isCredit) {
        mIsCredit = isCredit;
    }

    public int getIssuedBy() {
        return mIssuedBy;
    }

    public void setIssuedBy(int issuedBy) {
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


    public static class Products implements Serializable{
        private String $id;
        private float Id;
        private String ProductName_AR;
        private String ProductName_EN;
        private float ProductId;
        private float BoxPrice;
        private float UnitPrice;
        private float QTY;
        private float Transaction_Id;
        private boolean IsByBox;
        private float TotalPrice;
        private float OfficialUnitPrice;


        // Getter Methods

        public String get$id() {
            return $id;
        }

        public float getId() {
            return Id;
        }

        public String getProductName_AR() {
            return ProductName_AR;
        }

        public String getProductName_EN() {
            return ProductName_EN;
        }

        public float getProductId() {
            return ProductId;
        }

        public float getBoxPrice() {
            return BoxPrice;
        }

        public float getUnitPrice() {
            return UnitPrice;
        }

        public float getQTY() {
            return QTY;
        }

        public float getTransaction_Id() {
            return Transaction_Id;
        }

        public boolean getIsByBox() {
            return IsByBox;
        }

        public float getTotalPrice() {
            return TotalPrice;
        }

        public float getOfficialUnitPrice() {
            return OfficialUnitPrice;
        }

        // Setter Methods

        public void set$id(String $id) {
            this.$id = $id;
        }

        public void setId(float Id) {
            this.Id = Id;
        }

        public void setProductName_AR(String ProductName_AR) {
            this.ProductName_AR = ProductName_AR;
        }

        public void setProductName_EN(String ProductName_EN) {
            this.ProductName_EN = ProductName_EN;
        }

        public void setProductId(float ProductId) {
            this.ProductId = ProductId;
        }

        public void setBoxPrice(float BoxPrice) {
            this.BoxPrice = BoxPrice;
        }

        public void setUnitPrice(float UnitPrice) {
            this.UnitPrice = UnitPrice;
        }

        public void setQTY(float QTY) {
            this.QTY = QTY;
        }

        public void setTransaction_Id(float Transaction_Id) {
            this.Transaction_Id = Transaction_Id;
        }

        public void setIsByBox(boolean IsByBox) {
            this.IsByBox = IsByBox;
        }

        public void setTotalPrice(float TotalPrice) {
            this.TotalPrice = TotalPrice;
        }

        public void setOfficialUnitPrice(float OfficialUnitPrice) {
            this.OfficialUnitPrice = OfficialUnitPrice;
        }



        public double getPrice() {
            if (IsByBox)
                return BoxPrice;
            else return UnitPrice;
        }



    }


}
