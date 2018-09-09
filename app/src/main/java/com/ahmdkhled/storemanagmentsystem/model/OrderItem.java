package com.ahmdkhled.storemanagmentsystem.model;


import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable{

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

    protected OrderItem(Parcel in) {
        quantity = in.readInt();
        product = in.readParcelable(Product.class.getClassLoader());
        order = in.readParcelable(Order.class.getClassLoader());
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(quantity);
        parcel.writeParcelable(product, i);
        parcel.writeParcelable(order, i);
    }
}
