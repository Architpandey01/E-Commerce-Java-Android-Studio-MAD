package com.example.ecommerceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecommerceapp.Activity.CategoryActivity;
import com.example.ecommerceapp.Adapter.PopularListAdapter;
import com.example.ecommerceapp.Domain.PopularDomain;
import com.example.ecommerceapp.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterPopular;
    private RecyclerView recyclerViewPopular;
    private ArrayList<PopularDomain> masterProducts = new ArrayList<>();
    private PopularListAdapter popularListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the welcome greeting text (uses string resource)
        TextView welcomeText = findViewById(R.id.textView);
        if (welcomeText != null) {
            welcomeText.setText(getString(R.string.welcome_greeting));
        }

        initRecyclerView();
        initSearch();
        bottomNavigation();
    }

    private void initSearch() {
        android.widget.EditText search = findViewById(R.id.editTextText);
        if (search == null) return;

        search.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim().toLowerCase();
                if (popularListAdapter == null) return;

                if (query.isEmpty()) {
                    popularListAdapter.updateList(new ArrayList<>(masterProducts));
                    return;
                }

                ArrayList<PopularDomain> filtered = new ArrayList<>();
                for (PopularDomain p : masterProducts) {
                    if (p.getTitle() != null && p.getTitle().toLowerCase().contains(query)) {
                        filtered.add(p);
                    }
                }
                popularListAdapter.updateList(filtered);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void bottomNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        ImageView wishlistIcon = findViewById(R.id.imageView10);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        if (wishlistIcon != null) {
            wishlistIcon.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WishlistActivity.class)));
        }

        // Category clicks
        View catPc = findViewById(R.id.cat_pc);
        View catPhone = findViewById(R.id.cat_phone);
        View catHeadsets = findViewById(R.id.cat_headsets);
        View catGaming = findViewById(R.id.cat_gaming);
        View catViewAll = findViewById(R.id.cat_view_all);

        View.OnClickListener categoryClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cat = "";
                if (v.getId() == R.id.cat_pc) cat = "PC";
                else if (v.getId() == R.id.cat_phone) cat = "Phone";
                else if (v.getId() == R.id.cat_headsets) cat = "Headsets";
                else if (v.getId() == R.id.cat_gaming) cat = "Gaming";
                else if (v.getId() == R.id.cat_view_all) cat = "ALL";

                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("category", cat);
                startActivity(intent);
            }
        };

        if (catPc != null) catPc.setOnClickListener(categoryClick);
        if (catPhone != null) catPhone.setOnClickListener(categoryClick);
        if (catHeadsets != null) catHeadsets.setOnClickListener(categoryClick);
        if (catGaming != null) catGaming.setOnClickListener(categoryClick);
        if (catViewAll != null) catViewAll.setOnClickListener(categoryClick);


    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = loadProductsFromAssets();
        if (items == null) items = new ArrayList<>();

        // Keep a master copy for searching
        masterProducts.clear();
        masterProducts.addAll(items);

        recyclerViewPopular = findViewById(R.id.view1);
        // For the main screen, keep horizontal list as before
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        popularListAdapter = new PopularListAdapter(new ArrayList<>(masterProducts));
        recyclerViewPopular.setAdapter(popularListAdapter);
    }

    // Load products from app assets/products.json using Gson
    private ArrayList<PopularDomain> loadProductsFromAssets() {
        try {
            java.io.InputStream is = getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            com.google.gson.reflect.TypeToken<ArrayList<PopularDomain>> token = new com.google.gson.reflect.TypeToken<ArrayList<PopularDomain>>(){};
            ArrayList<PopularDomain> products = new com.google.gson.Gson().fromJson(json, token.getType());
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}