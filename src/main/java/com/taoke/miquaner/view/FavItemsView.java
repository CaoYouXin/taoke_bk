package com.taoke.miquaner.view;

import com.taobao.api.domain.UatmTbkItem;
import com.taoke.miquaner.data.EFavoriteOrder;

import java.util.List;

public class FavItemsView {

    private List<UatmTbkItem> items;
    private List<Long> orders;

    public FavItemsView(List<UatmTbkItem> uatmTbkItems, List<Long> favOrder) {
        this.items = uatmTbkItems;
        this.orders = favOrder;
    }

    public List<UatmTbkItem> getItems() {
        return items;
    }

    public void setItems(List<UatmTbkItem> items) {
        this.items = items;
    }

    public List<Long> getOrders() {
        return orders;
    }

    public void setOrders(List<Long> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "FavItemsView{" +
                "items=" + items +
                ", orders=" + orders +
                '}';
    }
}
