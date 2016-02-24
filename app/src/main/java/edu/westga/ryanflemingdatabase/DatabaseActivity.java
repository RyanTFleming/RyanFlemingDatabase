package edu.westga.ryanflemingdatabase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class DatabaseActivity extends AppCompatActivity {

    private TextView idView;
    private EditText productBox;
    private EditText quantityBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        this.idView = (TextView) findViewById(R.id.productID);
        this.quantityBox = (EditText) findViewById(R.id.productQuantity);
        this.productBox = (EditText) findViewById(R.id.productName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds a new Product to the database
     * @param view the view
     */
    public void newProduct(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        int quantity = Integer.parseInt(this.quantityBox.getText().toString());
        Product product = new Product(this.productBox.getText().toString(), quantity);
        dbHandler.addProduct(product);
        this.productBox.setText("");
        this.quantityBox.setText("");
    }

    /**
     * finds a product in the database
     * @param view - the view
     */
    public void lookupProduct(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Product product = dbHandler.findProduct(this.productBox.getText().toString());
        if (product != null) {
            this.idView.setText(String.valueOf(product.getID()));
            this.quantityBox.setText(String.valueOf(product.getQuantity()));
        } else {
            this.idView.setText("No Match Found");
        }
    }

    /**
     * Deletes a product from the database
     * @param view - view
     */
    public void removeProduct(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        boolean result = dbHandler.deleteProduct(productBox.getText().toString());
        if (result) {
            this.idView.setText("Record Deleted");
            this.productBox.setText("");
            this.quantityBox.setText("");
        }
    }

    /**
     * updates a product in the database
     * @param view - the view
     */
    public void updateProduct(View view) {

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        int id = Integer.parseInt(this.idView.getText().toString());
        String name = this.productBox.getText().toString();
        int quantity = Integer.parseInt(this.quantityBox.getText().toString());
        Product product = new Product(id, name, quantity);
        boolean result = dbHandler.updateProduct(product);
        if (result) {
            this.idView.setText("Record Updated");
        } else {
            this.idView.setText("Update Failed");
        }
    }
}
