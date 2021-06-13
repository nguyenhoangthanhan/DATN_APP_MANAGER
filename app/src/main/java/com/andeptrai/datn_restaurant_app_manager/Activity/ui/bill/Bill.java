package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill;

import java.io.Serializable;

public class Bill implements Serializable {
    private String idBill;
    private int idUserOrder;
    private String nameUserOrder;
    private String idRestaurant;
    private String timeCreateBill;
    private int statusConfirm;

    public Bill(String idBill, int idUserOrder, String nameUserOrder, String idRestaurant, String timeCreateBill, int statusConfirm) {
        this.idBill = idBill;
        this.idUserOrder = idUserOrder;
        this.nameUserOrder = nameUserOrder;
        this.idRestaurant = idRestaurant;
        this.timeCreateBill = timeCreateBill;
        this.statusConfirm = statusConfirm;
    }

    public Bill(String idBill, int idUserOrder, String idRestaurant, String timeCreateBill, int statusConfirm) {
        this.idBill = idBill;
        this.idUserOrder = idUserOrder;
        this.idRestaurant = idRestaurant;
        this.timeCreateBill = timeCreateBill;
        this.statusConfirm = statusConfirm;
    }

    public int getStatusConfirm() {
        return statusConfirm;
    }

    public void setStatusConfirm(int statusConfirm) {
        this.statusConfirm = statusConfirm;
    }

    public String getNameUserOrder() {
        return nameUserOrder;
    }

    public void setNameUserOrder(String nameUserOrder) {
        this.nameUserOrder = nameUserOrder;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public int getIdUserOrder() {
        return idUserOrder;
    }

    public void setIdUserOrder(int idUserOrder) {
        this.idUserOrder = idUserOrder;
    }

    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getTimeCreateBill() {
        return timeCreateBill;
    }

    public void setTimeCreateBill(String timeCreateBill) {
        this.timeCreateBill = timeCreateBill;
    }
}

