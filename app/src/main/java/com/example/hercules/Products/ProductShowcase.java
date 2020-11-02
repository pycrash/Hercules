package com.example.hercules.Products;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hercules.Cart.CartActivity;
import com.example.hercules.Database.Database;
import com.example.hercules.Database.Order;
import com.example.hercules.Home.HomeActivity;
import com.example.hercules.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class ProductShowcase extends AppCompatActivity {

    LinearLayout in_cart;
    TextView productname, product_flavour, product_id, product_carbo, product_fat, product_proteins, product_servings,
            product_calories, product_price;
    ImageView product_image;
    boolean isAddedInCart;
    String name, price, flavour, ID, carbo, fat, proteins, calories, servings, nutrional, image;
    CharSequence desc;
    TextView nutrional_facts;
    CardView addToCart, goToCart;
    ElegantNumberButton quantity;
    int i = 0;
    int imageId;
    int multiplier;
    ExpandableTextView product_desc;
    ImageView back_login;
    TextView already_in_your_cart;
    TextView textview_fat, textview_carbo, textView_proteins, textView_calories;

    public static final String TAG = "Product_Showcase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_showcase);

        Log.d(TAG, "onCreate: here");

        in_cart = findViewById(R.id.in_cart);
        goToCart = findViewById(R.id.go_to_cart);
        back_login = findViewById(R.id.back_login);
        productname = findViewById(R.id.product_name);
        product_image = findViewById(R.id.product_image);
        product_flavour = findViewById(R.id.product_flavour);
        product_id = findViewById(R.id.product_id);
        product_price = findViewById(R.id.product_price);
        product_carbo = findViewById(R.id.product_carbo);
        product_fat = findViewById(R.id.product_fat);
        product_proteins = findViewById(R.id.product_proteins);
        product_servings = findViewById(R.id.product_servings);
        product_calories = findViewById(R.id.product_calories);
        nutrional_facts = findViewById(R.id.nutrional_facts);
        addToCart = findViewById(R.id.add_to_cart);
        quantity = findViewById(R.id.cart_number);
        product_desc = findViewById(R.id.expand_description);
        already_in_your_cart = findViewById(R.id.already_in_your_cart);
        textview_fat = findViewById(R.id.textview_fat);
        textview_carbo = findViewById(R.id.textview_carbo);
        textView_proteins = findViewById(R.id.textview_protein);
        textView_calories = findViewById(R.id.textview_calories);


        goToCart.setOnClickListener(view -> {
            Log.d(TAG, "onClick: go to cart clicked");
            Log.d(TAG, "onClick: starting cart activity");
            startActivity(new Intent(ProductShowcase.this, CartActivity.class));
        });


        back_login.setOnClickListener(view -> {
            Log.d(TAG, "onClick: back Pressed, going to home activity");
            startActivity(new Intent(ProductShowcase.this, HomeActivity.class));
            finish();
        });
        Log.d(TAG, "onCreate: getting extra strings from intent");

        int pos = getIntent().getIntExtra(getString(R.string.position), 0);
        int position = pos + 1;
        String category = getIntent().getStringExtra(getString(R.string.category));
        Log.d(TAG, "onCreate: pos : " + pos);
        Log.d(TAG, "onCreate: category : " + category);
        Log.d(TAG, "onCreate: position : " + position);

        Log.d(TAG, "onCreate: setting nutional facts for Rapid BCCA");
        assert category != null;
        if ((pos == 4 || pos == 5) && category.equals(getString(R.string.category_pre))) {
            textview_fat.setText(getString(R.string.ui_leucine));
            textview_carbo.setText(getString(R.string.ui_glutamine));
            textView_proteins.setText(getString(R.string.ui_isoleucine));
            textView_calories.setText(getString(R.string.ui_valine));
        }

        Log.d(TAG, "onCreate: populating the text views with desired info");
        price = getStringResourceByName(category + position + "_price");
        desc = getDesc(category + position + "_desc");
        flavour = getStringResourceByName(category + position + "_flavour");
        ID = getStringResourceByName(category + position + "_code");
        carbo = getStringResourceByName(category + position + "_carbo");
        fat = getStringResourceByName(category + position + "_fat");
        proteins = getStringResourceByName(category + position + "_proteins");
        calories = getStringResourceByName(category + position + "_calories");
        servings = getStringResourceByName(category + position + "_servings");
        nutrional = getStringResourceByName(category + position + "_nutrional_facts");
        name = getStringResourceByName(category + position + "_name");
        image = category + position;
        multiplier = Integer.parseInt(getStringResourceByName(category + position + "_multiplier"));

        product_desc.setText(desc);
        product_price.setText(getString(R.string.price, price));
        product_flavour.setText(flavour);
        productname.setText(name);
        product_id.setText(ID);
        product_carbo.setText(getString(R.string.gram, carbo));
        product_proteins.setText(getString(R.string.gram, proteins));
        product_calories.setText(getString(R.string.kcal, calories));
        product_fat.setText(getString(R.string.gram, fat));
        imageId = getResources().getIdentifier("com.example.hercules:drawable/" + image, null, null);
        product_image.setImageResource(imageId);
        nutrional_facts.setText(getString(R.string.nutritional_facts, nutrional));

        Log.d(TAG, "onCreate: checking if the product is in cart or not");
        isAddedInCart = new Database(getBaseContext()).hasObject(product_id.getText().toString());
        if (isAddedInCart) {
            Log.d(TAG, "onCreate: this product is already in the cart");
            Log.d(TAG, "onCreate: making already in cart layout visible");
            in_cart.setVisibility(View.VISIBLE);
            already_in_your_cart.setVisibility(View.VISIBLE);
            Log.d(TAG, "onCreate: getting the quantity from cart");
            quantity.setNumber(String.valueOf(new Database(getApplicationContext()).getQuantity(ID)));
        } else {
            Log.d(TAG, "onCreate: making already in cart layout invisible");
            in_cart.setVisibility(View.GONE);
            already_in_your_cart.setVisibility(View.GONE);
        }


        quantity.setOnValueChangeListener((view, oldValue, newValue) -> {
            if (newValue > oldValue) {
                i = i + 1;
            } else {
                i = i - 1;
            }
            quantity.setNumber(String.valueOf(i * multiplier));
        });

        Log.d(TAG, "onCreate: setting click listener on addToCart ");
        addToCart.setOnClickListener(view -> {
            Log.d(TAG, "onClick: addToCart is clicked");
            isAddedInCart = new Database(getBaseContext()).hasObject(product_id.getText().toString());
            if (isAddedInCart) {
                Log.d(TAG, "onClick: the item is already in cart");
                if (quantity.getNumber().equals("0")) {
                    Log.d(TAG, "onClick: user has made the quantity 0");
                    Log.d(TAG, "onClick: asking user whether he intended to do so")
                    ;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductShowcase.this, R.style.MyAlertDialogStyle);
                    builder.setMessage("This item is already in the cart. Do you want to modify it ? \n\nWarning: The quantity is set to 0, so it will remove the current item from the cart");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                        Log.d(TAG, "onClick: user has confirmed that he wants to delete this item from cart");
                        Log.d(TAG, "onClick: deleting " + name + " from cart");
                        new Database(getBaseContext()).deleteFromDatabase(ID);
                        Log.d(TAG, "onClick: making already in cart layout invisible");
                        in_cart.setVisibility(View.GONE);

                        Log.d(TAG, "onClick: showing toast that the operation has succeeded");
                        Toast.makeText(ProductShowcase.this, "Cart Updated", Toast.LENGTH_LONG).show();
                    }).setNegativeButton("Cancel", (dialogInterface, i) -> {
                        Log.d(TAG, "onClick: user has cancelled the operation");
                        dialogInterface.cancel();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Log.d(TAG, "onClick: user has specified a new quantity ");
                    Log.d(TAG, "onClick: the item is already in cart, asking user whether he want to change the quantity");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductShowcase.this, R.style.MyAlertDialogStyle);
                    builder.setMessage("This item is already in the cart. Do you want to modify it ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                        Log.d(TAG, "onClick: user wants to perform this operation");
                        Log.d(TAG, "onClick: updating the quantity of " + name + " in cart");
                        new Database(getBaseContext()).updateQuantity(product_id.getText().toString(), Integer.parseInt(quantity.getNumber()));

                        Log.d(TAG, "onClick: showing cart updated toast and dismissing the dialog");
                        Toast.makeText(ProductShowcase.this, "Cart Updated", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(TAG, "onClick: user has cancelled this operation");
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            } else {
                if (quantity.getNumber().equals("0")) {
                    Log.d(TAG, "onClick: item doesn't exists in cart but user hasn't specified a quantity");
                    Log.d(TAG, "onClick: showing no quantity specified dialog");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductShowcase.this, R.style.MyAlertDialogStyle);
                    builder.setMessage("Please specify a quantity");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Log.d(TAG, "onClick: adding the product " + name + " in cart");
                    new Database(getBaseContext()).addToCart(new Order(
                            product_id.getText().toString(),
                            imageId,
                            productname.getText().toString().replace("'", "'''"),
                            Integer.parseInt(quantity.getNumber()),
                            Integer.parseInt(price),
                            multiplier
                    ));

                    Log.d(TAG, "onClick: showing success toast to user");
                    Toast.makeText(ProductShowcase.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    already_in_your_cart.setText("Item added to cart");
                    in_cart.setVisibility(View.VISIBLE);
                    already_in_your_cart.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private String getStringResourceByName(String aString) {
        Log.d(TAG, "getStringResourceByName: getting string resource from name");
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }

    private CharSequence getDesc(String aString) {
        Log.d(TAG, "getDesc: getting product description");
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getText(resId);
    }

}