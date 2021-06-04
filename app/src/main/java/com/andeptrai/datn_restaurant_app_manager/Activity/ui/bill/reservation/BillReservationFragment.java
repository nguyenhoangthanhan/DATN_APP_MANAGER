package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.andeptrai.datn_restaurant_app_manager.R;

public class BillReservationFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bill_reservation, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);

        return root;
    }
}