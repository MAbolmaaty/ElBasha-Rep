package codeztalk.elbasha.delegate.models;

import com.google.gson.annotations.SerializedName;

public class ProductModel {

    private int offID;
    private int productAmount;
    private int type;


    @SerializedName("$id")
    private String m$id;
    @SerializedName("BoxPrice")
    private Double mBoxPrice;
    @SerializedName("BoxUnit")
    private int mBoxUnit;
    @SerializedName("Id")
    private int mId;
    @SerializedName("ProductCode")
    private String mProductCode;
    @SerializedName("ProductName_AR")
    private String mProductNameAR;
    @SerializedName("ProductName_EN")
    private String mProductNameEN;
    @SerializedName("UnitPrice")
    private Double mUnitPrice;

    @SerializedName("UnitStockQty")
    private int unitStockQty;

    @SerializedName("UnitInQty")
    private int unitInQty;

    @SerializedName("UnitOutQty")
    private int unitOutQty;

    public ProductModel(int offID,
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
                        int unitStockQty) {

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
        this.unitStockQty = unitStockQty;
    }

    public int getUnitStockQty() {
        return unitStockQty;
    }

    public void setUnitStockQty(int unitStockQty) {
        this.unitStockQty = unitStockQty;
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

    public String getM$id() {
        return m$id;
    }

    public void setM$id(String m$id) {
        this.m$id = m$id;
    }

    public Double getmBoxPrice() {
        return mBoxPrice;
    }

    public void setmBoxPrice(Double mBoxPrice) {
        this.mBoxPrice = mBoxPrice;
    }

    public int getmBoxUnit() {
        return mBoxUnit;
    }

    public void setmBoxUnit(int mBoxUnit) {
        this.mBoxUnit = mBoxUnit;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmProductCode() {
        return mProductCode;
    }

    public void setmProductCode(String mProductCode) {
        this.mProductCode = mProductCode;
    }

    public String getmProductNameAR() {
        return mProductNameAR;
    }

    public void setmProductNameAR(String mProductNameAR) {
        this.mProductNameAR = mProductNameAR;
    }

    public String getmProductNameEN() {
        return mProductNameEN;
    }

    public void setmProductNameEN(String mProductNameEN) {
        this.mProductNameEN = mProductNameEN;
    }

    public Double getmUnitPrice() {
        return mUnitPrice;
    }

    public void setmUnitPrice(Double mUnitPrice) {
        this.mUnitPrice = mUnitPrice;
    }


    public int getUnitInQty() {
        return unitInQty;
    }

    public void setUnitInQty(int unitInQty) {
        this.unitInQty = unitInQty;
    }

    public int getUnitOutQty() {
        return unitOutQty;
    }

    public void setUnitOutQty(int unitOutQty) {
        this.unitOutQty = unitOutQty;
    }

    public double getTotalPrice() {
        if (type == 0)
            return productAmount * mUnitPrice;
        else return productAmount * mBoxPrice;
    }

    public double getPrice() {
        if (type == 0)
            return  mUnitPrice;
        else return  mBoxPrice;
    }

    public String isBox() {
        if (type == 0)
            return  "false";
        else return  "true";
    }

}
