package com.ahmdkhled.storemanagmentsystem.model;

public class OrderItem {

    private int quantity;
    private Product product;
    private OrderItem orderItem;

    public OrderItem(int quantity, Product product, OrderItem orderItem) {
        this.quantity = quantity;
        this.product = product;
        this.orderItem = orderItem;
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

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
