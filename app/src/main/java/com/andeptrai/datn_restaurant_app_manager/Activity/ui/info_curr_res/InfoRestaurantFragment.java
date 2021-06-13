package com.andeptrai.datn_restaurant_app_manager.Activity.ui.info_curr_res;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andeptrai.datn_restaurant_app_manager.R;
import com.andeptrai.datn_restaurant_app_manager.model.InfoRestaurantCurr;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.andeptrai.datn_restaurant_app_manager.URL.urlGetAllKind;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlGetReviewPointByIdRes;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlUpdateResByIdRes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoRestaurantFragment extends Fragment implements View.OnClickListener {

    TextView txtId, txtNameRestaurant, txtPhone, txtPassword, txtAddress
            , txtReviewPoint, txtService, txtKind;
    TextView txtChangeName, txtChangePhone, txtChangePassword, txtChangeAddress, txtUpdateReviewPoint
            , txtChangeService, txtChangeKind;
    Button btnSave;

    ArrayList<Kind> kindArrayList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoRestaurantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoRestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoRestaurantFragment newInstance(String param1, String param2) {
        InfoRestaurantFragment fragment = new InfoRestaurantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_info_restaurant, container, false);
        getAllKind();
        txtId = view.findViewById(R.id.txtId);
        txtNameRestaurant = view.findViewById(R.id.txtNameRestaurant);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtReviewPoint = view.findViewById(R.id.txtReviewPoint);
        txtService = view.findViewById(R.id.txtService);
        txtKind = view.findViewById(R.id.txtKind);
        txtChangeName = view.findViewById(R.id.txtChangeName);
        txtChangePhone = view.findViewById(R.id.txtChangePhone);
        txtChangePassword = view.findViewById(R.id.txtChangePassword);
        txtChangeAddress = view.findViewById(R.id.txtChangeAddress);
        txtUpdateReviewPoint = view.findViewById(R.id.txtUpdateReviewPoint);
        txtChangeService = view.findViewById(R.id.txtChangeService);
        txtChangeKind = view.findViewById(R.id.txtChangeKind);
        btnSave = view.findViewById(R.id.btnSave);

        txtId.setText(InfoRestaurantCurr.currentId);
        txtNameRestaurant.setText(InfoRestaurantCurr.currentName);
        txtPhone.setText(InfoRestaurantCurr.currentPhone);
        txtPassword.setText(InfoRestaurantCurr.currentPwd);
        txtAddress.setText(InfoRestaurantCurr.currentAddress);
        txtReviewPoint.setText(InfoRestaurantCurr.currentRPoint + "");
        if (InfoRestaurantCurr.currentStatus == 1) { txtService.setText(R.string.service_res1); }
        else if (InfoRestaurantCurr.currentStatus == 2) { txtService.setText(R.string.service_res2); }
        else if (InfoRestaurantCurr.currentStatus == 3) { txtService.setText(R.string.service_res3); }
        txtKind.setText(InfoRestaurantCurr.listKind);

        txtChangeName.setOnClickListener(this);
        txtChangePhone.setOnClickListener(this);
        txtChangePassword.setOnClickListener(this);
        txtChangeAddress.setOnClickListener(this);
        txtUpdateReviewPoint.setOnClickListener(this);
        txtChangeService.setOnClickListener(this);
        txtChangeKind.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtChangeName){
            openChangeNameRestaurantDialog();
        }
        if (view.getId() == R.id.txtChangePassword){
            openChangePasswordDialog();
        }
        if (view.getId() == R.id.txtChangePhone){
            openChangePhoneDialog();
        }
        if (view.getId() == R.id.txtChangeAddress){
            openChangeAddressDialog();
        }
        if (view.getId() == R.id.txtChangeService){
            openChangeServiceDialog();
        }
        if (view.getId() == R.id.txtChangeKind){
            openChangeKindDialog();
        }
        if (view.getId() == R.id.txtUpdateReviewPoint){
            updateReviewPoint();
        }
        if (view.getId() == R.id.btnSave){
            updateInDB();
        }
    }


    private void updateInDB() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdateResByIdRes
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Update restaurant success")){
                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
                else if (response.trim().equals("Update restaurant success, list kind fail")){
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Update restaurant fail!---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idRestaurant", InfoRestaurantCurr.currentId);
                params.put("nameResNew", InfoRestaurantCurr.currentName);
                params.put("phoneNew", InfoRestaurantCurr.currentPhone+"");
                params.put("passwordNew", InfoRestaurantCurr.currentPwd);
                params.put("addressNew", InfoRestaurantCurr.currentAddress);
                params.put("reviewPointNew", InfoRestaurantCurr.currentRPoint+"");
                params.put("statusNew", InfoRestaurantCurr.currentStatus+"");
                params.put("listKindNew", InfoRestaurantCurr.listKind);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateReviewPoint() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetReviewPointByIdRes
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("Update review point fail")){
                    double currentReviewPoint = Double.parseDouble(response);
                    currentReviewPoint = (double) Math.round(currentReviewPoint * 10) / 10;
                    InfoRestaurantCurr.currentRPoint = currentReviewPoint;
                    txtReviewPoint.setText(currentReviewPoint + "");
                    Toast.makeText(getContext(), "Update review point success!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Update review point fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Update review point error!---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idRestaurant", InfoRestaurantCurr.currentId);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void openChangeKindDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_select_kind);

        final RecyclerView rvKind = dialog.findViewById(R.id.rvKind);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnBack = dialog.findViewById(R.id.btnBack);

        final ArrayList<Kind> kindArrayList1;
        if (InfoRestaurantCurr.currentStatus == 1){
            kindArrayList1 = new ArrayList<>();
            for (int i = 0; i < kindArrayList.size();i++){
                if(kindArrayList.get(i).getClassifyKind() == 2){
                    kindArrayList1.add(kindArrayList.get(i));
                }
            }
        }
        else if (InfoRestaurantCurr.currentStatus == 2){
            kindArrayList1 = new ArrayList<>();
            for (int i = 0; i < kindArrayList.size();i++){
                if(kindArrayList.get(i).getClassifyKind() == 1){
                    kindArrayList1.add(kindArrayList.get(i));
                }
            }
        }
        else{
            kindArrayList1 = kindArrayList;
        }

        KindAdapter kindAdapter = new KindAdapter(kindArrayList1, getContext()
                , InfoRestaurantCurr.currentStatus, InfoRestaurantCurr.listKind);
        rvKind.setAdapter(kindAdapter);
        rvKind.setLayoutManager(new LinearLayoutManager(getContext()));

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listKindNew = "";
                for (int i = 0; i < kindArrayList1.size();i++){
                    if (kindArrayList1.get(i).isCheck()){
                        listKindNew += kindArrayList1.get(i).getNameKind() + ",";
                    }
                }
                InfoRestaurantCurr.listKind = listKindNew;
                txtKind.setText(listKindNew);
                dialog.dismiss();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openChangeServiceDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_service);

        final RadioGroup groupService = dialog.findViewById(R.id.groupService);
        final RadioButton txtServiceReservation = dialog.findViewById(R.id.txtServiceReservation);
        final RadioButton txtServiceDelivery = dialog.findViewById(R.id.txtServiceDelivery);
        final RadioButton txtServiceAll = dialog.findViewById(R.id.txtServiceAll);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnBack = dialog.findViewById(R.id.btnBack);

        if (InfoRestaurantCurr.currentStatus == 1) {
            groupService.check(R.id.txtServiceReservation);
        }
        else if (InfoRestaurantCurr.currentStatus == 2) {
            groupService.check(R.id.txtServiceDelivery);
        }
        else if (InfoRestaurantCurr.currentStatus == 3) {
            groupService.check(R.id.txtServiceAll);
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupService.getCheckedRadioButtonId() == R.id.txtServiceReservation){
                    InfoRestaurantCurr.currentStatus = 1;
                    txtService.setText(txtServiceReservation.getText().toString());
                }
                else if (groupService.getCheckedRadioButtonId() == R.id.txtServiceDelivery){
                    InfoRestaurantCurr.currentStatus = 2;
                    txtService.setText(txtServiceDelivery.getText().toString());
                }
                else if (groupService.getCheckedRadioButtonId() == R.id.txtServiceAll){
                    InfoRestaurantCurr.currentStatus = 3;
                    txtService.setText(txtServiceAll.getText().toString());
                }
                dialog.dismiss();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openChangeAddressDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_content);

        final EditText edtContent = dialog.findViewById(R.id.edtContent);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnBack = dialog.findViewById(R.id.btnBack);

        edtContent.setText(InfoRestaurantCurr.currentAddress+"");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtAddress.setText(edtContent.getText().toString());
                InfoRestaurantCurr.currentAddress = edtContent.getText().toString();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openChangePhoneDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_content);

        final EditText edtContent = dialog.findViewById(R.id.edtContent);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnBack = dialog.findViewById(R.id.btnBack);

        edtContent.setText(InfoRestaurantCurr.currentPhone+"");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPhone.setText(edtContent.getText().toString()+"");
                InfoRestaurantCurr.currentPhone = (edtContent.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openChangePasswordDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_content);

        final EditText edtContent = dialog.findViewById(R.id.edtContent);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnBack = dialog.findViewById(R.id.btnBack);

        edtContent.setText(InfoRestaurantCurr.currentPwd);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPassword.setText(edtContent.getText().toString());
                InfoRestaurantCurr.currentPwd = (edtContent.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openChangeNameRestaurantDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_content);

        final EditText edtContent = dialog.findViewById(R.id.edtContent);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnBack = dialog.findViewById(R.id.btnBack);

        edtContent.setText(InfoRestaurantCurr.currentName);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNameRestaurant.setText(edtContent.getText().toString());
                InfoRestaurantCurr.currentName = edtContent.getText().toString();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getAllKind() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetAllKind, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                        for (int i = 0; i < response.length();i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                kindArrayList.add(new Kind(
                                        object.getString("Id_kind"),
                                        object.getString("Name_kind"),
                                        object.getInt("Classify_kind")
                                ));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(arrayRequest);
    }
}