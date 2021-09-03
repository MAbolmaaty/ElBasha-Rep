package codeztalk.elbasha.delegate.activities.categoryProduct;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryProductModel 
{
    private int offID;

    @SerializedName("$id")
    private String m$id;
    @SerializedName("CategoryName_AR")
    private String mCategoryNameAR;
    @SerializedName("CategoryName_EN")
    private String mCategoryNameEN;
    @SerializedName("Products")
    private List<Product> mProducts;


    public CategoryProductModel(int offID, String mCategoryNameAR, String mCategoryNameEN ) {
        this.offID = offID;
         this.mCategoryNameAR = mCategoryNameAR;
        this.mCategoryNameEN = mCategoryNameEN;
     }

    public String get$id() {
        return m$id;
    }

    public void set$id(String $id) {
        m$id = $id;
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

    public List<Product> getProducts() {
        return mProducts;
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
    }

}
