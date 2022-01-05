package com.android.librarydb.SQLite;

import android.content.Context;


public class OrderDAO {
    private Context context;
    private OrderDBHelperCards orderDBHelperCards;
    private OrderDBHelperTags orderDBHelperTags;
    public OrderDAO(Context context){
        this.context=context;
        orderDBHelperCards = new OrderDBHelperCards(this.context);
        orderDBHelperTags = new OrderDBHelperTags(this.context);
    }

    public OrderDBHelperCards getOrderDBHelperCards() {
        return orderDBHelperCards;
    }
    public OrderDBHelperTags getOrderDBHelperTags() {
        return orderDBHelperTags;
    }
}
