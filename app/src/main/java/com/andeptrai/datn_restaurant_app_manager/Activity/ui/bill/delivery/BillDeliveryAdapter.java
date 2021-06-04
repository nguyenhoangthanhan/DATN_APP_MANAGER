package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.delivery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andeptrai.datn_restaurant_app_manager.R;
import com.andeptrai.datn_restaurant_app_manager.model.InfoRestaurantCurr;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.andeptrai.datn_restaurant_app_manager.URL.urlGetAllBillDeliveryByIdRes;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlUpdateStatusConfirmBillDelivery;

public class BillDeliveryAdapter extends RecyclerView.Adapter {

    private ArrayList<BillDelivery> billDeliveryArrayList;
    Context mContext;
    BillDeliveryInterf billDeliveryInterf;

    public BillDeliveryAdapter(ArrayList<BillDelivery> billDeliveryArrayList, Context mContext, BillDeliveryInterf billDeliveryInterf) {
        this.billDeliveryArrayList = billDeliveryArrayList;
        this.mContext = mContext;
        this.billDeliveryInterf = billDeliveryInterf;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_bill_delivery, parent, false);
        return new BillDeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final BillDelivery billDelivery = billDeliveryArrayList.get(position);
        BillDeliveryViewHolder billDeliveryViewHolder = (BillDeliveryViewHolder) holder;

        billDeliveryViewHolder.txtNameUser.setText(billDelivery.getNameUserOrder());
        billDeliveryViewHolder.txtAddressDelivery.setText(billDelivery.getAddressDelivery());
        billDeliveryViewHolder.txtTotalMoney.setText(billDelivery.getTotalMoneyBill()+"đ");

        String listFoodAndNumber = billDelivery.getDetailBill();

        int numberFood = 0;
        for (int i = 0; i < listFoodAndNumber.length(); i++){
            if (listFoodAndNumber.charAt(i) >= '0' && listFoodAndNumber.charAt(i) <= '9' && listFoodAndNumber.charAt(i - 1) == ' '){
                numberFood++;
            }
        }
        billDeliveryViewHolder.txtFoodNumber.setText(numberFood + " món ăn");

        if (billDelivery.getStatusConfirm() == 0){
            billDeliveryViewHolder.imgAcceptDelivery.setVisibility(View.VISIBLE);
            billDeliveryViewHolder.imgRejectDelivery.setVisibility(View.VISIBLE);
            billDeliveryViewHolder.txtAccepted.setVisibility(View.GONE);
            billDeliveryViewHolder.txtRejected.setVisibility(View.GONE);
        }
        else if (billDelivery.getStatusConfirm() == 1){
            billDeliveryViewHolder.imgAcceptDelivery.setVisibility(View.GONE);
            billDeliveryViewHolder.imgRejectDelivery.setVisibility(View.GONE);
            billDeliveryViewHolder.txtAccepted.setVisibility(View.VISIBLE);
            billDeliveryViewHolder.txtRejected.setVisibility(View.GONE);
        }
        else if (billDelivery.getStatusConfirm() == 2){
            billDeliveryViewHolder.imgAcceptDelivery.setVisibility(View.GONE);
            billDeliveryViewHolder.imgRejectDelivery.setVisibility(View.GONE);
            billDeliveryViewHolder.txtAccepted.setVisibility(View.GONE);
            billDeliveryViewHolder.txtRejected.setVisibility(View.VISIBLE);
        }

        billDeliveryViewHolder.imgDetailBillDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailBillDeliveryActivity.class);
                intent.putExtra("currentBillDelivery", billDelivery);
                mContext.startActivity(intent);
            }
        });

        billDeliveryViewHolder.imgAcceptDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertAcceptBill(billDelivery, position);
            }
        });

        billDeliveryViewHolder.imgRejectDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertRejectBill(billDelivery, position);
            }
        });

        billDeliveryViewHolder.txtEditBillDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billDeliveryInterf.updateBillClickListener(billDelivery, position);
            }
        });

    }

    private void createAlertRejectBill(final BillDelivery billDelivery, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.content_reject_bill).setTitle(R.string.reject_bill);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                acceptBillInDB(billDelivery, position, "2");
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createAlertAcceptBill(final BillDelivery billDelivery, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.content_accept_bill).setTitle(R.string.accept_bill);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                acceptBillInDB(billDelivery, position, "1");
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void acceptBillInDB(final BillDelivery billDelivery, final int position, final String status) {

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlUpdateStatusConfirmBillDelivery
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Update status confirm success")){
                    Toast.makeText(mContext, "Update status confirm success!", Toast.LENGTH_SHORT).show();
                    billDeliveryArrayList.get(position).setStatusConfirm(Integer.parseInt(status));
                    notifyItemChanged(position);
                }
                else{
                    Toast.makeText(mContext, "Update status confirm fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext, "Update status confirm error!---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idBill", billDelivery.getIdBill());
                params.put("statusConfirm", status);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return billDeliveryArrayList.size();
    }

    class BillDeliveryViewHolder extends RecyclerView.ViewHolder{

        TextView txtNameUser, txtFoodNumber, txtTotalMoney, txtAddressDelivery, txtEditBillDelivery
                , txtAccepted, txtRejected;
        ImageView imgDetailBillDelivery, imgAcceptDelivery, imgRejectDelivery;

        public BillDeliveryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameUser = itemView.findViewById(R.id.txtNameUser);
            txtFoodNumber = itemView.findViewById(R.id.txtFoodNumber);
            txtTotalMoney = itemView.findViewById(R.id.txtTotalMoney);
            txtAddressDelivery = itemView.findViewById(R.id.txtAddressDelivery);
            txtEditBillDelivery = itemView.findViewById(R.id.txtEditBillDelivery);
            imgDetailBillDelivery = itemView.findViewById(R.id.imgDetailBillDelivery);
            imgAcceptDelivery = itemView.findViewById(R.id.imgAcceptDelivery);
            imgRejectDelivery = itemView.findViewById(R.id.imgRejectDelivery);
            txtAccepted = itemView.findViewById(R.id.txtAccepted);
            txtRejected = itemView.findViewById(R.id.txtRejected);
        }
    }
}
