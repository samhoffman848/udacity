/*
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 *
 * package com.example.android.justjava;
 *
 */

package com.example.android.justjava;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        quantity++;
        displayQuantity(quantity);
        String price = calculatePrice();
        displayMessage("Total: " + price);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        quantity--;

        if(quantity < 0) {
          quantity = 0;
        }

        displayQuantity(quantity);
        String price = calculatePrice();
        displayMessage("Total: " + price);
    }

    /**
     * This method calculates the price based on quantity.
     */
    private String calculatePrice(){
        String price = NumberFormat.getCurrencyInstance().format(quantity * 5);
        return price;
    }

    /**
     * This method calculates the price based on quantity.
     */
    private String createOrderSummary(String price){
        String summary = "Name: Sam\n" +
                "Quantity: " + quantity + "\n" +
                "Total: " + price + "\n" +
                "Thank you for your order!";
        return summary;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String price = calculatePrice();
        String summary = createOrderSummary(price);
        displayMessage(summary);
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