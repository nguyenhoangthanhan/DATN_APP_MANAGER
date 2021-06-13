package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.reservation;

import android.app.Dialog;
import android.widget.TextView;

public interface SetNumberDialogInterf {
    void setNumberAdultsDialogClickListener(Dialog childrenNumberDialog, String s, TextView txtNumber);
    void setNumberChildrenDialogClickListener(Dialog childrenNumberDialog, String s, TextView txtNumber);
}
