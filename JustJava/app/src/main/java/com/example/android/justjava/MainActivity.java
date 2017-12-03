/*
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 *
 * package com.example.android.justjava;
 *
 */

package com.example.android.justjava;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayQuantity(quantity);
        calculatePrice();
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        quantity++;

        if(quantity > 100) {
            quantity = 100;
            Toast.makeText(MainActivity.this, getString(R.string.max_order),
                    Toast.LENGTH_SHORT).show();
        }

        displayQuantity(quantity);
        String price = calculatePrice();
        displayMessage(getString(R.string.total_text, price));
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        quantity--;

        if(quantity < 1) {
          quantity = 1;
            Toast.makeText(MainActivity.this, getString(R.string.min_order),
                    Toast.LENGTH_SHORT).show();
        }

        displayQuantity(quantity);
        String price = calculatePrice();
        displayMessage(getString(R.string.total_text, price));
    }

    /**
     * This method is called when a topping added.
     */
    public void updatePrice(View view){
        String price = calculatePrice();
        displayMessage(getString(R.string.total_text, price));
    }

    /**
     * This method calculates the price based on quantity.
     */
    private String calculatePrice(){
        CheckBox whippedCream = (CheckBox)findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolate = (CheckBox)findViewById(R.id.chocolate_checkbox);

        double basePrice = 5.00;

        if(whippedCream.isChecked()){
            basePrice += 0.50;
        }

        if(chocolate.isChecked()){
            basePrice += 1.20;
        }

        return NumberFormat.getCurrencyInstance().format(quantity * basePrice);
    }

    /**
     * This method calculates any extras and updates price accordingly.
     */
    private ArrayList<String> calculateExtras(){
        CheckBox whippedCream = (CheckBox)findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolate = (CheckBox)findViewById(R.id.chocolate_checkbox);

        ArrayList<String> extras = new ArrayList<String>();

        if(whippedCream.isChecked()){
            extras.add(getString(R.string.whipped_cream_text));
        }

        if(chocolate.isChecked()){
            extras.add(getString(R.string.chocolate_text));
        }

        if(extras.size() <= 0){
            extras.add(getString(R.string.none_text));
        }

        return extras;
    }

    /**
     * This method calculates the price based on quantity.
     */
    private void createOrderSummary(String name, String price, String extras){
        String subject = getString(R.string.email_subject, name);

        String summary = getString(R.string.name_text, name) + "\n" +
                getString(R.string.quantity_text, quantity) + "\n" +
                getString(R.string.extras_text, extras) + "\n" +
                getString(R.string.total_text, price) + "\n" +
                getString(R.string.thank_you_text);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:samhoffman848@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, summary);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String price = calculatePrice();

        ArrayList<String> extras = calculateExtras();
        String extraString =  TextUtils.join(", ", extras);

        EditText nameEdit = (EditText) findViewById(R.id.name_edit);
        String name = nameEdit.getText().toString();

        createOrderSummary(name, price, extraString);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_num);
        quantityTextView.setText(String.valueOf(number));
    }

    /**
     * This method displays the given price on the screen and a thank you message.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.order_summary_text);
        priceTextView.setText(message);
    }
}