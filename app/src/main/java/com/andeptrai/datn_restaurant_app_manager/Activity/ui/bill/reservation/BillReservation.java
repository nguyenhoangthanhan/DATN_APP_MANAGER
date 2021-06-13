package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.reservation;

import com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.Bill;

public class BillReservation extends Bill {

    private String datetimeGo;
    private int adultsNumber;
    private int childrenNumber;
    private String notes;

    public BillReservation(String idBill, int idUserOrder, String nameUserOrder, String idRestaurant
            , String timeCreateBill, int statusConfirm) {
        super(idBill, idUserOrder, nameUserOrder, idRestaurant, timeCreateBill, statusConfirm);
    }

    public BillReservation(String idBill, int idUserOrder, String nameUserOrder, String idRestaurant, String timeCreateBill
            , String datetimeGo, int adultsNumber, int childrenNumber, String notes, int statusConfirm) {
        super(idBill, idUserOrder, nameUserOrder, idRestaurant, timeCreateBill, statusConfirm);
        this.datetimeGo = datetimeGo;
        this.adultsNumber = adultsNumber;
        this.childrenNumber = childrenNumber;
        this.notes = notes;
    }

    public BillReservation(String idBill, int idUserOrder, String idRestaurant, String timeCreateBill
            , String datetimeGo, int adultsNumber, int childrenNumber, String notes, int statusConfirm) {
        super(idBill, idUserOrder, idRestaurant, timeCreateBill, statusConfirm);
        this.datetimeGo = datetimeGo;
        this.adultsNumber = adultsNumber;
        this.childrenNumber = childrenNumber;
        this.notes = notes;
    }


    public String getDatetimeGo() {
        return datetimeGo;
    }

    public void setDatetimeGo(String datetimeGo) {
        this.datetimeGo = datetimeGo;
    }

    public int getAdultsNumber() {
        return adultsNumber;
    }

    public void setAdultsNumber(int adultsNumber) {
        this.adultsNumber = adultsNumber;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public void setChildrenNumber(int childrenNumber) {
        this.childrenNumber = childrenNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
