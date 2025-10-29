package com.example.ecommerceapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.Domain.PopularDomain;
import com.example.ecommerceapp.Helper.ManagementCart;
import com.example.ecommerceapp.R;

import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    private Button addToCartBtn;
    private TextView titleTxt, priceTxt, descriptionTxt, reviewTxt, scoreTxt;
    private ImageView picItem, backBtn;
    private PopularDomain object;
    private int numberOrder = 1;
    private ManagementCart managementCart;

    // New: wishlist button and prefs
    private ImageView wishlistBtn;
    private SharedPreferences wishlistPrefs;
    private static final String PREF_WISHLIST = "wishlist_prefs";
    private static final String KEY_WISHLIST_SET = "wishlist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        managementCart = new ManagementCart(this);
        // initialize prefs
        wishlistPrefs = getSharedPreferences(PREF_WISHLIST, MODE_PRIVATE);
        initView();
        getBundle();
    }

    private void getBundle() {
        object = (PopularDomain) getIntent().getSerializableExtra("object");
        int drawableResourceId = this.getResources().getIdentifier(object.getPicUrl(), "drawable", this.getPackageName());

        Glide.with(this)
                .load(drawableResourceId)
                .into(picItem);

//        titleTxt.setText(object.getTitle());
//        feeTxt.setText("₹" + object.getPrice());
//        descriptionTxt.setText(object.getDescription());
//        reviewTxt.setText(object.getReview() + "");
//        scoreTxt.setText(object.getScore() + "");

        // Bind all product fields to the UI so the detail view reflects the selected product
        if (object != null) {
            titleTxt.setText(object.getTitle());
            priceTxt.setText("₹" + object.getPrice());
            descriptionTxt.setText(object.getDescription());
            reviewTxt.setText(String.valueOf(object.getReview()));
            scoreTxt.setText(String.valueOf(object.getScore()));

            // update wishlist icon for this product
            String id = object.getTitle();
            boolean inWishlist = isInWishlist(id);
            updateWishlistIcon(inWishlist);

            // wishlist click handler
            wishlistBtn.setOnClickListener(v -> {
                if (isInWishlist(id)) {
                    removeFromWishlist(id);
                    updateWishlistIcon(false);
                } else {
                    addToWishlist(id);
                    updateWishlistIcon(true);
                }
            });
        }

        addToCartBtn.setOnClickListener(v -> {
            object.setNumberinCart(numberOrder);
            managementCart.insertFood(object);
        });

        backBtn.setOnClickListener(v -> finish());

    }

    private void initView() {
        addToCartBtn = findViewById(R.id.addToCartBtn);
        titleTxt = findViewById(R.id.titleTxt);
        priceTxt = findViewById(R.id.priceTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        picItem = findViewById(R.id.itemPic);
        reviewTxt = findViewById(R.id.reviewTxt);
        scoreTxt = findViewById(R.id.scoreTxt);
        backBtn = findViewById(R.id.backBtn);
        // bind wishlist ImageView from layout (imageView14)
        wishlistBtn = findViewById(R.id.imageView14);
    }

    // Wishlist helpers
    private boolean isInWishlist(String id) {
        if (id == null) return false;
        Set<String> set = wishlistPrefs.getStringSet(KEY_WISHLIST_SET, new HashSet<>());
        return set.contains(id);
    }

    private void addToWishlist(String id) {
        if (id == null) return;
        Set<String> set = new HashSet<>(wishlistPrefs.getStringSet(KEY_WISHLIST_SET, new HashSet<>()));
        if (set.add(id)) {
            wishlistPrefs.edit().putStringSet(KEY_WISHLIST_SET, set).apply();
            Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFromWishlist(String id) {
        if (id == null) return;
        Set<String> set = new HashSet<>(wishlistPrefs.getStringSet(KEY_WISHLIST_SET, new HashSet<>()));
        if (set.remove(id)) {
            wishlistPrefs.edit().putStringSet(KEY_WISHLIST_SET, set).apply();
            Toast.makeText(this, "Removed from wishlist", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWishlistIcon(boolean inWishlist) {
        if (inWishlist) {
            // tint the bookmark icon to indicate "saved"
            wishlistBtn.setColorFilter(ContextCompat.getColor(this, R.color.red));
        } else {
            // clear tint
            wishlistBtn.clearColorFilter();
        }
    }
}