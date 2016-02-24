package edu.westga.ryanflemingdatabase;

/**
 * Class for modeling a product
 * contains fields to hold product id, name, and quantity
 *
 * Created by Ryan on 2/23/2016.
 */
public class Product {
    private int _id;
    private String _productName;
    private int _quantity;

    public Product() {

    }

    public Product(int id, String productName, int quantity) {
        this._productName = productName;
        this._id = id;
        this._quantity = quantity;
    }
    public Product(String productName, int quantity) {
        this._productName = productName;
        this._quantity = quantity;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setProductName(String productName) {
        this._productName = productName;
    }

    public String getProductName() {
        return this._productName;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public int getQuantity() {
        return this._quantity;
    }
}
