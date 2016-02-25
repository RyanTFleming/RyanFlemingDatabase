package edu.westga.ryanflemingdatabase;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.Suppress;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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
        this.isProductIDSet();
        this.idView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DatabaseActivity.this.isProductIDSet();
            }
        });
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
        this.clearHints();
        this.idView.setText(R.string.not_assigned_string);
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        if (this.productBox.getText().toString().equals("") && this.quantityBox.getText().toString().equals("")) {
            this.productBox.setHint("Required to Add");
            this.quantityBox.setHint("Required to Add");
            return;
        } else if (this.productBox.getText().toString().equals("")) {
            this.productBox.setHint("Required to Add");
            return;
        } else if (this.quantityBox.getText().toString().equals("")) {
            this.quantityBox.setHint("Required to Add");
            return;
        }

        Product oldProduct = dbHandler.findProduct(String.valueOf(this.productBox.getText()));
        if (oldProduct != null) {
            this.idView.setText("Duplicate Product");
            return;
        }

        try {
            int quantity = Integer.parseInt(this.quantityBox.getText().toString());
            Product product = new Product(this.productBox.getText().toString(), quantity);
            dbHandler.addProduct(product);
            this.idView.setText("Record Added");
            this.clearBoxes();
        } catch (NumberFormatException nfe) {
            this.quantityBox.setText("");
            this.quantityBox.setHint("Valid Integer Only");
        }
    }

    /**
     * finds a product in the database
     * @param view - the view
     */
    public void lookupProduct(View view) {
        this.clearHints();
        this.idView.setText(R.string.not_assigned_string);
        this.quantityBox.setHint("");
        this.productBox.setHint("");
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        String name = this.productBox.getText().toString();
        if (name.equals("")) {
            this.productBox.setHint("Required for Find");
            return;
        }
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
        this.clearHints();
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        boolean result = dbHandler.deleteProduct(productBox.getText().toString());
        if (result) {
            this.idView.setText("Record Deleted");
            this.clearBoxes();
        } else {
            this.idView.setText("No Match Found");
        }
    }

    /**
     * updates a product in the database
     * @param view - the view
     */
    public void updateProduct(View view) {
        this.clearHints();
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        if (this.productBox.getText().toString().equals("") && this.quantityBox.getText().toString().equals("")) {
            this.productBox.setHint("Required to Update");
            this.quantityBox.setHint("Required to Update");
            return;
        } else if (this.productBox.getText().toString().equals("")) {
            this.productBox.setHint("Required to Update");
            return;
        } else if (this.quantityBox.getText().toString().equals("")) {
            this.quantityBox.setHint("Required to Update");
            return;
        }

        try {
            String name = this.productBox.getText().toString();
            int id = Integer.parseInt(this.idView.getText().toString());
            int quantity = Integer.parseInt(this.quantityBox.getText().toString());
            Product product = new Product(id, name, quantity);
            boolean result = dbHandler.updateProduct(product);
            if (result) {
                this.idView.setText("Record Updated");
                this.clearBoxes();
            } else {
                this.idView.setText("Update Failed");
            }
        } catch (NumberFormatException nfe) {
            this.idView.setHint("Valid Integer Only");
        }
    }

    /**
     * Removes all records from the database.
     * @param view - the view
     */
    public void removeAllRecords(View view) {
        this.clearHints();
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        int count = dbHandler.deleteAllProducts();
        if (count == 1) {
            this.idView.setText(count + " Record Deleted");
        } else if (count > 1) {
            this.idView.setText(count + " Records Deleted");
        } else {
            this.idView.setText("Table Was Empty");
        }
        this.clearBoxes();
    }

    private void clearBoxes() {
        this.quantityBox.setText("");
        this.productBox.setText("");
        this.clearHints();
    }
    private void clearHints() {
        this.quantityBox.setHint("");
        this.productBox.setHint("");
    }

    @SuppressWarnings("unused")
    private void isProductIDSet() {
        Button btnUpdate = (Button) DatabaseActivity.this.findViewById(R.id.btnUpdate);
        boolean isEnabled;
        try {
            int id = Integer.parseInt(DatabaseActivity.this.idView.getText().toString());
            isEnabled = true;
        } catch (NumberFormatException nfe) {
            isEnabled = false;
        }
        btnUpdate.setEnabled(isEnabled);
    }
}
