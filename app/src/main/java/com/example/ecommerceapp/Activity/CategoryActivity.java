package com.example.ecommerceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ecommerceapp.Adapter.PopularListAdapter;
import com.example.ecommerceapp.Domain.PopularDomain;
import com.example.ecommerceapp.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        String category = getIntent().getStringExtra(EXTRA_CATEGORY);
        TextView title = findViewById(R.id.categoryTitle);
        if (category == null || category.equals("ALL")) {
            title.setText("All Products");
            category = null; // null means no filtering
        } else {
            title.setText(category);
        }

        RecyclerView recyclerView = findViewById(R.id.categoryRecyclerView);
        // Use a GridLayout with 2 columns so products appear 2 per row
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ArrayList<PopularDomain> products = loadProductsFromAssets();
        ArrayList<PopularDomain> filtered = new ArrayList<>();
        if (products != null) {
            if (category == null) filtered.addAll(products);
            else {
                for (PopularDomain p : products) {
                    if (p.getCategory() != null && p.getCategory().equals(category)) {
                        filtered.add(p);
                    }
                }
            }
        }

        PopularListAdapter adapter = new PopularListAdapter(filtered);
        recyclerView.setAdapter(adapter);

    }

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
