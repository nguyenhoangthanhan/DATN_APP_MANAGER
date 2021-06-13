package com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.reservation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.delivery.BillDelivery;
import com.andeptrai.datn_restaurant_app_manager.Activity.ui.bill.delivery.BillDeliveryAdapter;
import com.andeptrai.datn_restaurant_app_manager.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.andeptrai.datn_restaurant_app_manager.URL.urlUpdateStatusConfirmBillDelivery;

public class BillReservationAdapter extends RecyclerView.Adapter {

    ArrayList<BillReservation> billReservationArrayList;
    Context mContext;
    ReservationInterf reservationInterf;

    public BillReservationAdapter(ArrayList<BillReservation> billReservationArrayList, Context mContext
            , ReservationInterf reservationInterf) {
        this.billReservationArrayList = billReservationArrayList;
        this.mContext = mContext;
        this.reservationInterf = reservationInterf;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_bill_reservation, parent, false);
        return new BillReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final BillReservation billReservation = billReservationArrayList.get(position);
        BillReservationViewHolder billReservationViewHolder = (BillReservationViewHolder) holder;

        billReservationViewHolder.txtNameUser.setText(billReservation.getNameUserOrder());
        billReservationViewHolder.txtAdultsNumber.setText(billReservation.getAdultsNumber()+" người");
        billReservationViewHolder.txtChildrenNumber.setText(billReservation.getChildrenNumber()+" người");
        billReservationViewHolder.txtTimeCome.setText(billReservation.getDatetimeGo());

        if (billReservation.getStatusConfirm() == 0){
            billReservationViewHolder.imgAccept.setVisibility(View.VISIBLE);
            billReservationViewHolder.imgReject.setVisibility(View.VISIBLE);
            billReservationViewHolder.txtAccepted.setVisibility(View.GONE);
            billReservationViewHolder.txtRejected.setVisibility(View.GONE);
        }
        else if (billReservation.getStatusConfirm() == 1){
            billReservationViewHolder.imgAccept.setVisibility(View.GONE);
            billReservationViewHolder.imgReject.setVisibility(View.GONE);
            billReservationViewHolder.txtAccepted.setVisibility(View.VISIBLE);
            billReservationViewHolder.txtRejected.setVisibility(View.GONE);
        }
        else if (billReservation.getStatusConfirm() == 2){
            billReservationViewHolder.imgAccept.setVisibility(View.GONE);
            billReservationViewHolder.imgReject.setVisibility(View.GONE);
            billReservationViewHolder.txtAccepted.setVisibility(View.GONE);
            billReservationViewHolder.txtRejected.setVisibility(View.VISIBLE);
        }

        billReservationViewHolder.txtEditBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reservationInterf.updateBillClickListener(billReservation, position);
            }
        });

        billReservationViewHolder.imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertAcceptBill(billReservation, position);
            }
        });

        billReservationViewHolder.imgReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertRejectBill(billReservation, position);
            }
        });




    }

    private void createAlertRejectBill(final BillReservation billReservation, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.content_reject_bill).setTitle(R.string.reject_bill);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                acceptBillInDB(billReservation, position, "2");
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

    private void createAlertAcceptBill(final BillReservation billReservation, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.content_accept_bill).setTitle(R.string.accept_bill);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                acceptBillInDB(billReservation, position, "1");
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

    private void acceptBillInDB(final BillReservation billReservation, final int position, final String status) {

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlUpdateStatusConfirmBillDelivery
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Update status confirm success")){
                    Toast.makeText(mContext, "Update status confirm success!", Toast.LENGTH_SHORT).show();
                    billReservationArrayList.get(position).setStatusConfirm(Integer.parseInt(status));
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
                params.put("idBill", billReservation.getIdBill());
                params.put("statusConfirm", status);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return billReservationArrayList.size();
    }

    class BillReservationViewHolder extends RecyclerView.ViewHolder{

        TextView txtNameUser, txtAdultsNumber, txtChildrenNumber, txtTimeCome, txtEditBill
                , txtAccepted, txtRejected;
        ImageView imgAccept, imgReject;

        public BillReservationViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameUser = itemView.findViewById(R.id.txtNameUser);
            txtAdultsNumber = itemView.findViewById(R.id.txtAdultsNumber);
            txtChildrenNumber = itemView.findViewById(R.id.txtChildrenNumber);
            txtTimeCome = itemView.findViewById(R.id.txtTimeCome);
            txtEditBill = itemView.findViewById(R.id.txtEditBill);
            txtAccepted = itemView.findViewById(R.id.txtAccepted);
            txtRejected = itemView.findViewById(R.id.txtRejected);
            imgAccept = itemView.findViewById(R.id.imgAccept);
            imgReject = itemView.findViewById(R.id.imgReject);
        }
    }
}
