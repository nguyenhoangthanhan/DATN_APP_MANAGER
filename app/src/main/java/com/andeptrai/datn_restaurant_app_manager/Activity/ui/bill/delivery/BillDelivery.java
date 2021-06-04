package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.delivery;

import com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.Bill;

import java.io.Serializable;

public class BillDelivery extends Bill implements Serializable {
    private String addressDelivery;
    private String timeDelivery;
    private long totalMoneyBill;
    private String payment;
    private String notes;
    private String detailBill;
    private String detailFood;

    public BillDelivery(String idBill, int idUserOrder, String nameUserOrder, String idRestaurant
            , String timeCreateBill, int statusConfirm) {
        super(idBill, idUserOrder, nameUserOrder, idRestaurant, timeCreateBill, statusConfirm);
    }

    public BillDelivery(String idBill, int idUserOrder, String nameUserOrder, String idRestaurant, String timeCreateBill, int statusConfirm
            , String addressDelivery, String timeDelivery, long totalMoneyBill, String payment, String notes
            , String detailBill, String detailFood) {
        super(idBill, idUserOrder, nameUserOrder, idRestaurant, timeCreateBill, statusConfirm);
        this.addressDelivery = addressDelivery;
        this.timeDelivery = timeDelivery;
        this.totalMoneyBill = totalMoneyBill;
        this.payment = payment;
        this.notes = notes;
        this.detailBill = detailBill;
        this.detailFood = detailFood;
    }

    public String getDetailFood() {
        return detailFood;
    }

    public void setDetailFood(String detailFood) {
        this.detailFood = detailFood;
    }

    public String getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(String addressDelivery) {
        this.addressDelivery = addressDelivery;
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public long getTotalMoneyBill() {
        return totalMoneyBill;
    }

    public void setTotalMoneyBill(long totalMoneyBill) {
        this.totalMoneyBill = totalMoneyBill;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDetailBill() {
        return detailBill;
    }

    public void setDetailBill(String detailBill) {
        this.detailBill = detailBill;
    }
}
