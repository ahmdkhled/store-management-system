package com.ahmdkhled.storemanagmentsystem.model;

import android.util.Log;

public class OrderItem {

    private int quantity;
    private Product product;
    private Order order;

    public OrderItem(int quantity, Product product, Order order) {
        this.quantity = quantity;
        this.product = product;
        this.order = order;
    }

    public OrderItem(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public OrderItem(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null){return false;}
        if (getClass() != obj.getClass()) { return false; }

        OrderItem item= (OrderItem) obj;

        return this.getProduct().getId().equals(item.getProduct().getId());
    }
}
