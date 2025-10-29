package com.example.ecommerceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ecommerceapp.Adapter.PopularListAdapter;
import com.example.ecommerceapp.Domain.PopularDomain;
import com.example.ecommerceapp.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WishlistActivity extends AppCompatActivity {
    private static final String PREF_WISHLIST = "wishlist_prefs";
    private static final String KEY_WISHLIST_SET = "wishlist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        RecyclerView recyclerView = findViewById(R.id.wishlistRecycler);
        TextView emptyText = findViewById(R.id.emptyText);

        List<PopularDomain> allProducts = loadProductsFromAssets();
        Set<String> wishlist = getWishlistSet();

        ArrayList<PopularDomain> items = new ArrayList<>();
        if (allProducts != null) {
            for (PopularDomain p : allProducts) {
                if (p.getTitle() != null && wishlist.contains(p.getTitle())) {
                    items.add(p);
                }
            }
        }

        if (items.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Show items in a grid: 2 columns
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            PopularListAdapter adapter = new PopularListAdapter(items, R.layout.viewholder_pop_grid);
            recyclerView.setAdapter(adapter);
        }
    }

    private Set<String> getWishlistSet() {
        SharedPreferences prefs = getSharedPreferences(PREF_WISHLIST, MODE_PRIVATE);
        return new HashSet<>(prefs.getStringSet(KEY_WISHLIST_SET, new HashSet<>()));
    }

    // Duplicate of MainActivity's loading method to keep this activity self-contained
    private ArrayList<PopularDomain> loadProductsFromAssets() {
        try {
            InputStream is = getAssets().open("products.json");
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
