package codeztalk.elbasha.delegate.activities.categoryProduct;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.activities.AddInvoiceActivity;
import codeztalk.elbasha.delegate.activities.BaseActivity;
import codeztalk.elbasha.delegate.db.ForsahDB;
import codeztalk.elbasha.delegate.helper.BottomNavigationBehavior;
import codeztalk.elbasha.delegate.models.ClientModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class CategoryProductsActivity extends BaseActivity {
    private static final String TAG = CategoryProductsActivity.class.getSimpleName();
    private RecyclerView recyclerCategories;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryProductModel> categoryArrayList;
    private RecyclerView recyclerProducts;
    ProductCategoryAdapter productCategoryAdapter;
    ArrayList<Product> productArrayList;
    ImageView imageFilter;
    EditText editSearch;
    CharSequence search = "";
    LinearLayout linearToolbar;
    ForsahDB db;
    private double totalPrice = 0.0;
    TextView textTotal;
    TextView textProceed;

    ClientModel clientModel;


    public void getProducts(String catName) {
        productArrayList.clear();


        productArrayList.addAll(db.getAllCategoryProducts(catName));
        productCategoryAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);
        Log.d(TAG, "Category Products Activity Created");

//        preferenceHelper.setUserId("2");
//        preferenceHelper.setUserToken("Bearer 8j1hEJSqvKNXg5NPRCR6pMSHz1BP-y4ROWC0gK3Qk-mVWJKT-uxj4ZsjtK-c77p6heux6Dh2mWs7sP2DIihZuumHiGkavaGIt_cipV6gHvOMPy8F_GmaDp6udINM3FDrKp0qhBmiZpOTj3-FJ0wYI8lA3SmLFSA9Ea5If_xqYsebhs9hshbFkvDUD-CAURvVnBlw7hIzaVoVuhQ358HgOd24RTHkRUFpjgFOxrkfu_TLBKnCgOztoXiIgxOtJDftD-1v0cN3lhr5KBApnCTcxiRYlaf4YOlsrMRZwCQeSdjVKA-LwHlMHIR6ak2ENSD3");

        db = new ForsahDB(this);

        clientModel = (ClientModel) getIntent().getSerializableExtra("clientModel");

        recyclerCategories = findViewById(R.id.recycler_category);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        textTotal = findViewById(R.id.textTotal);
        textProceed = findViewById(R.id.textProceed);

        imageFilter = findViewById(R.id.imageFilter);
        editSearch = findViewById(R.id.editSearch);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                productCategoryAdapter.getFilter().filter(s);
                search = s;


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initializeHoursOffline();
        initializeViews();

        textTotal.setText(preferenceHelper.getTotalPrice());

        textProceed.setOnClickListener(v -> {


            if (Double.parseDouble(textTotal.getText().toString()) > 0) {
                Intent i = new Intent(CategoryProductsActivity.this, AddInvoiceActivity.class);
                i.putExtra("clientModel", clientModel);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(CategoryProductsActivity.this, R.string.productError, Toast.LENGTH_SHORT).show();

            }


        });
    }

    void initializeViews() {
        LinearLayout containerNav = findViewById(R.id.containerNav);
        linearToolbar = findViewById(R.id.linearToolbar);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) containerNav.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        imageFilter.setVisibility(View.VISIBLE);
        imageFilter.setImageResource(R.drawable.ic_back);
        imageFilter.setPadding(5, 5, 5, 5);
         linearToolbar.setBackgroundColor(Color.parseColor("#00489A"));

        imageFilter.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {

        removeCashedData();
        finish();
    }

    public void initializeHoursOffline() {
        //initialize views
        categoryArrayList = new ArrayList<>();
        productArrayList = new ArrayList<>();

        recyclerCategories.setHasFixedSize(true);
        recyclerProducts.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CategoryProductsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager productLayoutManager = new LinearLayoutManager(CategoryProductsActivity.this);

        recyclerCategories.setLayoutManager(mLayoutManager);
        recyclerProducts.setLayoutManager(productLayoutManager);

        categoryAdapter = new CategoryAdapter(categoryArrayList, this, CategoryProductsActivity.this);
        productCategoryAdapter = new ProductCategoryAdapter(productArrayList, this, CategoryProductsActivity.this);

        recyclerCategories.setAdapter(categoryAdapter);
        recyclerProducts.setAdapter(productCategoryAdapter);


        Log.e("getProductsCount ", " >>==<<<" + db.getProductsCount());

        if (db.getProductsCount() > 0) {
            getProductsOffline();
        } else {
            getProductsOnline();
        }

    }

    private void getProductsOffline() {


        categoryArrayList.addAll(db.getAllCategory());
        categoryAdapter.notifyDataSetChanged();

        getProducts(categoryArrayList.get(0).getCategoryNameEN());


        Log.e("off", "off" + productArrayList.size());

    }

    public void calculatePrice(List<Product> inventoryList) {

        totalPrice = Double.parseDouble(preferenceHelper.getTotalPrice());
        for (Product productInventory : inventoryList) {
            totalPrice = totalPrice + (productInventory.getTotalPrice());

        }

        preferenceHelper.setTotalPrice(String.valueOf(totalPrice));
        runOnUiThread(() -> textTotal.setText(String.format("%s", totalPrice)));
    }

    public void calculatePrice() {

        totalPrice = 0;

        List<Product> inventoryList = db.getSelectedCategoryProducts();

        for (Product productInventory : inventoryList) {
            totalPrice = totalPrice + (productInventory.getTotalPrice());

        }

        preferenceHelper.setTotalPrice(String.valueOf(totalPrice));
        runOnUiThread(() -> textTotal.setText(String.format("%s", totalPrice)));
    }

    public void calculatePrice2() {


        totalPrice = db.getTotalPrice();
//        for (Product productInventory : inventoryList)
//        {
//            totalPrice = totalPrice + (productInventory.getTotalPrice());
//        }

        preferenceHelper.setTotalPrice(String.valueOf(totalPrice));

        runOnUiThread(() -> textTotal.setText(String.format("%s", totalPrice)));
    }


    private void getProductsOnline() {
        Log.e("on", "on");

        showSimpleProgressDialog(this, false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<List<CategoryProductModel>> call = apiInterface.getCategoriesProducts(
                preferenceHelper.getUserId()
                , preferenceHelper.getUserToken());

        call.enqueue(new Callback<List<CategoryProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryProductModel>> call, @NonNull Response<List<CategoryProductModel>> response) {
                removeSimpleProgressDialog();

                Log.e("address : ", "  ## :  ## " + new Gson().toJson(response.body()));
                Log.e("response : ", "  ## :  ## " + response.body());

                if (response.body() != null) {

//                    categoryArrayList.addAll(response.body());
//                    categoryAdapter.notifyDataSetChanged();
//                    getProducts(0);

                    db.addToCategory(response.body());

                    for (CategoryProductModel categoryProductModel : response.body()) {
                        db.addToCategoryProducts(categoryProductModel.getProducts());

                    }

                    getProductsOffline();

                }
            }

            @Override
            public void onFailure
                    (@NonNull Call<List<CategoryProductModel>> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }


    void removeCashedData() {
        db.deleteCategoryTable();
        db.deleteProductTable();
        preferenceHelper.setTotalPrice("0");
    }

}



