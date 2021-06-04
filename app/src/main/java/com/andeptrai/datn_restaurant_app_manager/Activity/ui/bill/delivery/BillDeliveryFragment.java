package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andeptrai.datn_restaurant_app_manager.R;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.andeptrai.datn_restaurant_app_manager.URL.urlGetAllBillDeliveryByIdRes;

public class BillDeliveryFragment extends Fragment implements BillDeliveryInterf {

    ImageView icPageBack;
    RecyclerView rvBillDelivery;
    BillDeliveryAdapter billDeliveryAdapter;
    ArrayList<BillDelivery> billDeliveryArrayList = new ArrayList<>();

    private final int CODE_REQUEST_EDIT_BILL_DELIVERY = 1000;
    int currentPositionBill = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bill_delivery, container, false);

        icPageBack = root.findViewById(R.id.icPageBack);

        rvBillDelivery = root.findViewById(R.id.rvBillDelivery);
        billDeliveryAdapter = new BillDeliveryAdapter(billDeliveryArrayList, getContext(), this);
        rvBillDelivery.setAdapter(billDeliveryAdapter);
        rvBillDelivery.setLayoutManager(new LinearLayoutManager(getContext()));
        getAllBillDelivery();

        return root;
    }

    private void getAllBillDelivery() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlGetAllBillDeliveryByIdRes
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("Get all delivery this restaurant fail")){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            billDeliveryArrayList.add(new BillDelivery(jsonObject.getString("Id_bill"),
                                    jsonObject.getInt("Id_us_order"),
                                    jsonObject.getString("Name_user_order"),
                                    jsonObject.getString("Id_restaurant"),
                                    jsonObject.getString("Time_create_bill"),
                                    jsonObject.getInt("Status_confirm"),
                                    jsonObject.getString("Address_delivery"),
                                    jsonObject.getString("Time_delivery"),
                                    jsonObject.getLong("Total_money"),
                                    jsonObject.getString("Payment"),
                                    jsonObject.getString("Notes"),
                                    jsonObject.getString("Detail_bill"),
                                    jsonObject.getString("Detail_food")));
                        }
                        billDeliveryAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {

                        Toast.makeText(getContext(), "Get bill delivery error exception! -- " + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Get bill delivery fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Get all bill delivery by id res error!---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idRestaurant", InfoRestaurantCurr.currentId);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void updateBillClickListener(BillDelivery billDelivery, int position) {
        currentPositionBill = position;
        Intent intent = new Intent(getContext(), EditBillDeliveryActivity.class);
        intent.putExtra("currentBillDelivery", billDelivery);
        startActivityForResult(intent, CODE_REQUEST_EDIT_BILL_DELIVERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST_EDIT_BILL_DELIVERY && resultCode == 1001){
            BillDelivery updateBillDelivery = (BillDelivery) data.getSerializableExtra("updateBillDelivery");
            billDeliveryArrayList.remove(currentPositionBill);
            billDeliveryArrayList.add(currentPositionBill, updateBillDelivery);
            billDeliveryAdapter.notifyItemChanged(currentPositionBill);
        }
    }
}