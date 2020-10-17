package com.example.hercules.Products;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_showcase);

        in_cart = findViewById(R.id.in_cart);
        in_cart.setVisibility(View.GONE);
        goToCart = findViewById(R.id.go_to_cart);
        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductShowcase.this, CartActivity.class));
            }
        });
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
        product_desc = (ExpandableTextView) findViewById(R.id.expand_description);
        already_in_your_cart = findViewById(R.id.already_in_your_cart);
        textview_fat = findViewById(R.id.textview_fat);
        textview_carbo = findViewById(R.id.textview_carbo);
        textView_proteins = findViewById(R.id.textview_protein);
        textView_calories = findViewById(R.id.textview_calories);


        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        int pos = getIntent().getIntExtra("position", 0);
        int position = pos + 1;
        String category = getIntent().getStringExtra("category");



        assert category != null;
        if ((pos == 4 || pos == 5) && category.equals(getString(R.string.category_pre))) {
            textview_fat.setText("Leucine");
            textview_carbo.setText("Glutamine");
            textView_proteins.setText("Isoleucine");
            textView_calories.setText("Valine");
        }
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

        isAddedInCart = new Database(getBaseContext()).hasObject(product_id.getText().toString());
        if (isAddedInCart) {
            in_cart.setVisibility(View.VISIBLE);
            already_in_your_cart.setVisibility(View.VISIBLE);
            quantity.setNumber(String.valueOf(new Database(getApplicationContext()).getQuantity(ID)));
        }
        quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if (newValue > oldValue) {
                    i = i +1;
                    quantity.setNumber(String.valueOf(i*multiplier));

                } else {
                    i = i - 1;
                    quantity.setNumber(String.valueOf(i*multiplier));

                }
            }
        });

            addToCart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                isAddedInCart = new Database(getBaseContext()).hasObject(product_id.getText().toString());
                if (isAddedInCart) {
                    if (quantity.getNumber().equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductShowcase.this, R.style.MyAlertDialogStyle);
                        builder.setMessage("This item is already in the cart. Do you want to modify it ? \n\nWarning: The quantity is set to 0, so it will remove the current item from the cart");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Database(getBaseContext()).deleteFromDatabase(ID);
                                in_cart.setVisibility(View.GONE);
                                Toast.makeText(ProductShowcase.this, "Cart Updated", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductShowcase.this, R.style.MyAlertDialogStyle);
                        builder.setMessage("This item is already in the cart. Do you want to modify it ?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Database(getBaseContext()).updateQuantity(product_id.getText().toString(), Integer.parseInt(quantity.getNumber()));
                                Toast.makeText(ProductShowcase.this, "Cart Updated", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                } else {
                    if (quantity.getNumber().equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductShowcase.this, R.style.MyAlertDialogStyle);
                        builder.setMessage("Please specify a quantity");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        new Database(getBaseContext()).addToCart(new Order(
                                product_id.getText().toString(),
                                imageId,
                                productname.getText().toString().replace("'", "'''"),
                                Integer.parseInt(quantity.getNumber()),
                                Integer.parseInt(price),
                                multiplier
                        ));
                        Toast.makeText(ProductShowcase.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        already_in_your_cart.setText("Item added to cart");
                        in_cart.setVisibility(View.VISIBLE);
                        already_in_your_cart.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
    private CharSequence getDesc(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getText(resId);
    }

}