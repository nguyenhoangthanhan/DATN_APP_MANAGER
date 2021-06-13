package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.reservation;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andeptrai.datn_restaurant_app_manager.GET_DATA;
import com.andeptrai.datn_restaurant_app_manager.R;
import com.andeptrai.datn_restaurant_app_manager.RandomString;
import com.andeptrai.datn_restaurant_app_manager.model.InfoRestaurantCurr;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.andeptrai.datn_restaurant_app_manager.CODE.CREATE_NEW_BILL;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlCreateNewBillReservation;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlGetAllBillReservationByIdRes;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlUpdateBillReservationByIdBillClient;

public class BillReservationFragment extends Fragment implements ReservationInterf, SetNumberDialogInterf{

    TextView txtAddNewBillReservation;
    RecyclerView rvBillReservation;

    ArrayList<BillReservation> billReservationArrayList = new ArrayList<>();
    BillReservationAdapter billReservationAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bill_reservation, container, false);

        txtAddNewBillReservation = root.findViewById(R.id.txtAddNewBillReservation);
        rvBillReservation = root.findViewById(R.id.rvBillReservation);

        billReservationAdapter = new BillReservationAdapter(billReservationArrayList, getContext(), this);
        rvBillReservation.setAdapter(billReservationAdapter);
        rvBillReservation.setLayoutManager(new LinearLayoutManager(getContext()));
        getAllBillReservationByIdRes();

        txtAddNewBillReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNewBillReservationDialog();
            }
        });
        return root;
    }

    private void openAddNewBillReservationDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_bill_reservation);

        final TextView txtTimeGo = dialog.findViewById(R.id.txtTimeGo);
        final TextView txtChangeTimeGo = dialog.findViewById(R.id.txtChangeTimeGo);
        final TextView txtNumberAdults = dialog.findViewById(R.id.txtNumberAdults);
        final TextView txtChangeNumberAdults = dialog.findViewById(R.id.txtChangeNumberAdults);
        final TextView txtNumberChildren = dialog.findViewById(R.id.txtNumberChildren);
        final TextView txtChangeNumberChildren = dialog.findViewById(R.id.txtChangeNumberChildren);
        final EditText edtNotes = dialog.findViewById(R.id.edtNotes);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnBack = dialog.findViewById(R.id.btnBack);

        txtTimeGo.setText("00:00:00 00/00/0000");
        txtNumberAdults.setText(0+"");
        txtNumberChildren.setText(0+"");
        edtNotes.setText("");

        txtChangeTimeGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Calendar calendar = GET_DATA.editTimeDelivery(getContext());
                GET_DATA.setyyyyMMddHHmmss(getContext(), txtTimeGo);
//                billReservationOrdered.setDatetimeGo(GET_DATA.formatyyyyMMddHHmmss(calendar));
            }
        });

        txtChangeNumberAdults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdultsNumber(txtNumberAdults);
            }
        });

        txtChangeNumberChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChildrenNumber(txtNumberChildren);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BillReservation billReservation = new BillReservation((new RandomString(CREATE_NEW_BILL, new Random()).nextString())
                        , -1, InfoRestaurantCurr.currentId
                        , (new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()))
                        , txtTimeGo.getText().toString(), Integer.parseInt(txtNumberAdults.getText().toString())
                        , Integer.parseInt(txtNumberChildren.getText().toString())
                        , edtNotes.getText().toString(), 0);
                billReservation.setNameUserOrder("Đặt theo cuộc gọi khách hàng");
                insertNewBillReservationToDB(billReservation, dialog);
            }
        });

        dialog.show();
    }

    private void insertNewBillReservationToDB(final BillReservation billReservation, final Dialog dialog) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlCreateNewBillReservation
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RespondReservation", response.trim());
                if (response.trim().equals("Create new bill reservation success")){
                    Toast.makeText(getContext(), "Create new bill reservation success!", Toast.LENGTH_SHORT).show();
                    billReservationArrayList.add(0, billReservation);
                    billReservationAdapter.notifyItemInserted(0);
                    dialog.dismiss();
                }
                else if (response.trim().equals("Create new bill success, reservation fail")){
                    Toast.makeText(getContext(), "Create new bill success, reservation fail", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Create new bill reservation fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Create new bill reservation error ---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idBill", billReservation.getIdBill());
                params.put("idUserOrder", billReservation.getIdUserOrder() + "");
                params.put("idRestaurant", billReservation.getIdRestaurant());
                params.put("timeCreateBill", billReservation.getTimeCreateBill());

                params.put("datetimeGo", billReservation.getDatetimeGo());
                params.put("adultsNumber", billReservation.getAdultsNumber() + "");
                params.put("childrenNumber", billReservation.getChildrenNumber() + "");
                params.put("notes", billReservation.getNotes());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getAllBillReservationByIdRes() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlGetAllBillReservationByIdRes
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("Get all reservation fail")){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            billReservationArrayList.add(new BillReservation(jsonObject.getString("Id_bill"),
                                    jsonObject.getInt("Id_us_order"),
                                    jsonObject.getString("Name"),
                                    jsonObject.getString("Id_restaurant"),
                                    jsonObject.getString("Time_create_bill"),
                                    jsonObject.getString("Datetime_go"),
                                    jsonObject.getInt("Adults_number"),
                                    jsonObject.getInt("Children_number"),
                                    jsonObject.getString("Notes"),
                                    jsonObject.getInt("Status_confirm")));
                        }

                    } catch (JSONException e) {

                        Toast.makeText(getContext(), "Get bill reservation error exception! -- " + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    billReservationAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getContext(), "Get bill reservation fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Get all bill reservation error!---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idRestaurant", InfoRestaurantCurr.currentId + "");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void updateBillClickListener(BillReservation billReservation, int position) {
        openDialogEditBillReservation(billReservation, position);
    }

    private void openDialogEditBillReservation(final BillReservation billReservation, final int position) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_bill_reservation);

        final TextView txtTimeGo = dialog.findViewById(R.id.txtTimeGo);
        final TextView txtChangeTimeGo = dialog.findViewById(R.id.txtChangeTimeGo);
        final TextView txtNumberAdults = dialog.findViewById(R.id.txtNumberAdults);
        final TextView txtChangeNumberAdults = dialog.findViewById(R.id.txtChangeNumberAdults);
        final TextView txtNumberChildren = dialog.findViewById(R.id.txtNumberChildren);
        final TextView txtChangeNumberChildren = dialog.findViewById(R.id.txtChangeNumberChildren);
        final EditText edtNotes = dialog.findViewById(R.id.edtNotes);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnBack = dialog.findViewById(R.id.btnBack);

        txtTimeGo.setText(billReservation.getDatetimeGo());
        txtNumberAdults.setText(billReservation.getAdultsNumber()+"");
        txtNumberChildren.setText(billReservation.getChildrenNumber()+"");
        edtNotes.setText(billReservation.getNotes());

        txtChangeTimeGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Calendar calendar = GET_DATA.editTimeDelivery(getContext());
                GET_DATA.setyyyyMMddHHmmss(getContext(), txtTimeGo);
//                billReservationOrdered.setDatetimeGo(GET_DATA.formatyyyyMMddHHmmss(calendar));
            }
        });

        txtChangeNumberAdults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdultsNumber(txtNumberAdults);
            }
        });

        txtChangeNumberChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChildrenNumber(txtNumberChildren);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTimeGo = txtTimeGo.getText().toString();
                String newNumberChildren = txtNumberChildren.getText().toString();
                String newNumberAdults = txtNumberAdults.getText().toString();
                String newNotes = edtNotes.getText().toString();
                updateBillReservationInDB(newTimeGo, newNumberChildren, newNumberAdults,newNotes, billReservation, position, dialog);
            }
        });

        dialog.show();
    }


    private void updateBillReservationInDB(final String newTimeGo, final String newNumberChildren
            , final String newNumberAdults, final String newNotes
            , final BillReservation billReservation, final int position, final Dialog dialog) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlUpdateBillReservationByIdBillClient
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Update bill reservation success")){
                    billReservation.setDatetimeGo(newTimeGo);
                    billReservation.setAdultsNumber(Integer.parseInt(newNumberAdults));
                    billReservation.setChildrenNumber(Integer.parseInt(newNumberChildren));
                    billReservation.setNotes(newNotes);
                    billReservationAdapter.notifyItemChanged(position);
                    Toast.makeText(getContext(), "Update bill reservation success!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getContext(), "Update bill reservation fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Update bill reservation error!---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idBillReservation", billReservation.getIdBill());
                params.put("newTimeGo", newTimeGo);
                params.put("newNumberChildren", newNumberChildren);
                params.put("newNumberAdults", newNumberAdults);
                params.put("newNotes", newNotes);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void setAdultsNumber(TextView txtNumber) {
        final Dialog adultsNumberDialog = new Dialog(getContext());
        adultsNumberDialog.setContentView(R.layout.dialog_number_adults_children);

        TextView txtTittle = adultsNumberDialog.findViewById(R.id.txtTittle);
        txtTittle.setText("Người lớn");

        RecyclerView rvNumber = adultsNumberDialog.findViewById(R.id.rvNumber);
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < 200; i++){
            stringArrayList.add((i+1) + "");
        }

        AdultsChildrenNumberDialogAdapter numberAdapter = new AdultsChildrenNumberDialogAdapter(stringArrayList
                , getContext(), adultsNumberDialog, this, "Adults", txtNumber);
        rvNumber.setAdapter(numberAdapter);
        rvNumber.setLayoutManager(new LinearLayoutManager(getContext()));
        Button btnBack = adultsNumberDialog.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adultsNumberDialog.dismiss();
            }
        });

        adultsNumberDialog.show();
    }

    private void setChildrenNumber(TextView txtNumber) {
        final Dialog childrenNumberDialog = new Dialog(getContext());
        childrenNumberDialog.setContentView(R.layout.dialog_number_adults_children);

        TextView txtTittle = childrenNumberDialog.findViewById(R.id.txtTittle);
        txtTittle.setText("Trẻ em");

        RecyclerView rvNumber = childrenNumberDialog.findViewById(R.id.rvNumber);
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < 200; i++){
            stringArrayList.add((i+1) + "");
        }

        AdultsChildrenNumberDialogAdapter numberAdapter = new AdultsChildrenNumberDialogAdapter(stringArrayList
                , getContext(), childrenNumberDialog, this, "Children", txtNumber);
        rvNumber.setAdapter(numberAdapter);
        rvNumber.setLayoutManager(new LinearLayoutManager(getContext()));
        Button btnBack = childrenNumberDialog.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childrenNumberDialog.dismiss();
            }
        });
        childrenNumberDialog.show();
    }

    @Override
    public void setNumberAdultsDialogClickListener(Dialog childrenNumberDialog, String s, TextView txtNumber) {
        txtNumber.setText(s);
        childrenNumberDialog.dismiss();
    }

    @Override
    public void setNumberChildrenDialogClickListener(Dialog childrenNumberDialog, String s, TextView txtNumber) {
        txtNumber.setText(s);
        childrenNumberDialog.dismiss();
    }
}