package codeztalk.elbasha.delegate.activities.categoryProduct;

import com.google.gson.annotations.SerializedName;

public class Product {

    private int offID;
    private int productAmount;
    private int type;
    private Double OfficialUnitPrice;





    @SerializedName("$id")
    private String m$id;
    @SerializedName("BoxPrice")
    private Double mBoxPrice;
    @SerializedName("BoxUnit")
    private int mBoxUnit;
    @SerializedName("CategoryName_AR")
    private String mCategoryNameAR;
    @SerializedName("CategoryName_EN")
    private String mCategoryNameEN;
    @SerializedName("Id")
    private int mId;
    @SerializedName("ProductCode")
    private String mProductCode;
    @SerializedName("ProductName_AR")
    private String mProductNameAR;
    @SerializedName("ProductName_EN")
    private String mProductNameEN;
    @SerializedName("UnitInQty")
    private int mUnitInQty;
    @SerializedName("UnitOutQty")
    private int mUnitOutQty;
    @SerializedName("UnitPrice")
    private Double mUnitPrice;
    @SerializedName("UnitStockQty")
    private int mUnitStockQty;


    public Product(int offID,
                   int mId,
                   String m$id,
                   String mProductNameAR,
                   String mProductNameEN,
                   String mProductCode,
                   Double mBoxPrice,
                   Double mUnitPrice,
                   int mBoxUnit,
                   int productAmount,
                   int type,
                   int unitStockQty,
                   String categoryName,
                   String categoryAR,
                   Double OfficialUnitPrice ) {

        this.offID = offID;
        this.mId = mId;
        this.m$id = m$id;

        this.productAmount = productAmount;
        this.type = type;
        this.mBoxPrice = mBoxPrice;
        this.mBoxUnit = mBoxUnit;
        this.mProductCode = mProductCode;
        this.mProductNameAR = mProductNameAR;
        this.mProductNameEN = mProductNameEN;
        this.mUnitPrice = mUnitPrice;
        this.mUnitStockQty = unitStockQty;
        // TODO : Remove this
        if (unitStockQty == 0){
            this.mUnitStockQty = 6;
        }
        // Until here
        this.mCategoryNameEN = categoryName;
        this.mCategoryNameAR = categoryAR;
        this.OfficialUnitPrice = OfficialUnitPrice;
     }



    public Double getOfficialUnitPrice() {
        return OfficialUnitPrice;
    }

    public void setOfficialUnitPrice(Double officialUnitPrice) {
        OfficialUnitPrice = officialUnitPrice;
    }

    public int getOffID() {
        return offID;
    }

    public void setOffID(int offID) {
        this.offID = offID;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public int getBoxUnit() {
        return mBoxUnit;
    }

    public void setBoxUnit(int boxUnit) {
        mBoxUnit = boxUnit;
    }

    public String getCategoryNameAR() {
        return mCategoryNameAR;
    }

    public void setCategoryNameAR(String categoryNameAR) {
        mCategoryNameAR = categoryNameAR;
    }

    public String getCategoryNameEN() {
        return mCategoryNameEN;
    }

    public void setCategoryNameEN(String categoryNameEN) {
        mCategoryNameEN = categoryNameEN;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getProductCode() {
        return mProductCode;
    }

    public void setProductCode(String productCode) {
        mProductCode = productCode;
    }

    public String getProductNameAR() {
        return mProductNameAR;
    }

    public void setProductNameAR(String productNameAR) {
        mProductNameAR = productNameAR;
    }

    public String getProductNameEN() {
        return mProductNameEN;
    }

    public void setProductNameEN(String productNameEN) {
        mProductNameEN = productNameEN;
    }

    public int getUnitInQty() {
        return mUnitInQty;
    }

    public void setUnitInQty(int unitInQty) {
        mUnitInQty = unitInQty;
    }

    public int getUnitOutQty() {
        return mUnitOutQty;
    }

    public void setUnitOutQty(int unitOutQty) {
        mUnitOutQty = unitOutQty;
    }

    public Double getUnitPrice() {
        return mUnitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        mUnitPrice = unitPrice;
    }

    public int getUnitStockQty() {
        return mUnitStockQty;
    }

    public void setUnitStockQty(int unitStockQty) {
        mUnitStockQty = unitStockQty;
    }


    public int getBoxNumber() {
        return mUnitStockQty/mBoxUnit;
    }

    public double getTotalPrice() {
        if (type == 0)
            return productAmount * mUnitPrice;
        else return productAmount * mBoxPrice;
    }

    public double getPrice() {
        if (type == 0)
            return mUnitPrice;
        else return mBoxPrice;
    }

    public String isBox() {
        if (type == 0)
            return "false";
        else return "true";
    }

}

