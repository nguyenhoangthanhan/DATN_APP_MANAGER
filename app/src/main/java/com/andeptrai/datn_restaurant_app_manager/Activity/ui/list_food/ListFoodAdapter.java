package com.andeptrai.datn_restaurant_app_manager.Activity.ui.list_food;

import android.app.Dialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andeptrai.datn_restaurant_app_manager.R;
import com.andeptrai.datn_restaurant_app_manager.model.InfoRestaurantCurr;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.andeptrai.datn_restaurant_app_manager.URL.urlDeleteFood;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlEditFood;

public class ListFoodAdapter extends RecyclerView.Adapter {

    private ArrayList<Food> foodArrayList;
    private Context mContext;

    public ListFoodAdapter(ArrayList<Food> foodArrayList, Context mContext) {
        this.foodArrayList = foodArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View foodView = layoutInflater.inflate(R.layout.item_food_manager,parent, false);
        return new FoodViewHolder(foodView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Food food = foodArrayList.get(position);
        FoodViewHolder foodViewHolder = (FoodViewHolder) holder;
        foodViewHolder.txt_name_food.setText(food.getName_food());
        foodViewHolder.txt_price.setText(food.getPrice() + "đ");
        int price = food.getPrice();
        int promotion = food.getPromotion();
        int price_sale = (int) (price- price*(promotion / 100.0));
        foodViewHolder.txt_price_sale.setText(price_sale+"đ");
        foodViewHolder.txtSalePercent.setText(food.getPromotion()+"%");
        if(food.getStatus()==1){
            foodViewHolder.txtAvailable.setVisibility(View.VISIBLE);
            foodViewHolder.txtUnavailable.setVisibility(View.GONE);
        }
        else{
            foodViewHolder.txtAvailable.setVisibility(View.GONE);
            foodViewHolder.txtUnavailable.setVisibility(View.VISIBLE);
        }

        //edit curr food
        foodViewHolder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editThisFood(food, position);
            }
        });

        //delete curr food
        foodViewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteThisFood(food, position);
            }
        });

    }

    private void deleteThisFood(final Food food, final int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlDeleteFood
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Delete food success")){
                    foodArrayList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(mContext, "Delete food success!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext, "Delete food fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Delete food error ---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idFoodDelete", food.getId_food());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void editThisFood(final Food food, final int position) {

        final Dialog dialogAddNewFood = new Dialog(mContext);
        dialogAddNewFood.setContentView(R.layout.dialog_add_new_food);

        final RadioGroup rbGroup = dialogAddNewFood.findViewById(R.id.rbGroup);
        final RadioButton rbAvailable = dialogAddNewFood.findViewById(R.id.rbAvailable);
        final RadioButton rbUnavailable = dialogAddNewFood.findViewById(R.id.rbUnavailable);
        if(food.getStatus() == 1){
            rbAvailable.setSelected(true);
            rbUnavailable.setSelected(false);
        }else {
            rbAvailable.setSelected(false);
            rbUnavailable.setSelected(true);
        }
        final EditText edtNameNewFood = dialogAddNewFood.findViewById(R.id.edtNameNewFood);
        edtNameNewFood.setText(food.getName_food());
        final EditText edtPriceNew = dialogAddNewFood.findViewById(R.id.edtPriceNew);
        edtPriceNew.setText(food.getPrice()+"");
        final EditText edtPromotionNew = dialogAddNewFood.findViewById(R.id.edtPromotionNew);
        edtPromotionNew.setText(food.getPromotion()+"");
        Button btnAcceptAddFood = dialogAddNewFood.findViewById(R.id.btnAcceptAddFood);
        Button btnCancelAddFood = dialogAddNewFood.findViewById(R.id.btnBackNotify);

        btnCancelAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddNewFood.dismiss();
            }
        });

        btnAcceptAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int promotionNew = Integer.parseInt(edtPromotionNew.getText().toString());

                if (promotionNew > 100 || promotionNew < 0){
                    Toast.makeText(mContext, "Khuyến mãi phải lớn hơn 0% và nhỏ hơn 100%", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (rbGroup.getCheckedRadioButtonId() == R.id.rbAvailable){
                        Food foodEdit = new Food(InfoRestaurantCurr.currentId
                                , food.getId_food()
                                , edtNameNewFood.getText().toString()
                                , 1
                                , Integer.parseInt(edtPriceNew.getText().toString())
                                , promotionNew
                                , food.getTimeCreateFood());
                        editFoodInDB(foodEdit, position);
                    }
                    else if (rbGroup.getCheckedRadioButtonId() == R.id.rbUnavailable){
                        Food foodEdit = new Food(InfoRestaurantCurr.currentId
                                , food.getId_food()
                                , edtNameNewFood.getText().toString()
                                , 0
                                , Integer.parseInt(edtPriceNew.getText().toString())
                                , promotionNew
                                , food.getTimeCreateFood());
                        editFoodInDB(foodEdit, position);
                    }
                    else{
                        Toast.makeText(mContext, "Chưa lựa chọn trạng thái!", Toast.LENGTH_SHORT).show();
                    }
                }
                dialogAddNewFood.dismiss();
            }
        });

        dialogAddNewFood.show();
    }

    private void editFoodInDB(final Food food, final int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlEditFood
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Edit food success")){
                    foodArrayList.remove(position);
                    foodArrayList.add(position, food);
                    notifyItemChanged(position);
                    Toast.makeText(mContext, "Edit food success!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext, "Edit food fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Edit food error ---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idRestaurant", food.getId_restaurant());
                params.put("idFoodEdit", food.getId_food());
                params.put("nameEditFood", food.getName_food());
                params.put("status", food.getStatus()+"");
                params.put("editPrice", food.getPrice()+"");
                params.put("editPromotion", food.getPromotion()+"");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAvtFood, img_edit, img_delete;
        TextView txt_name_food, txt_price, txt_price_sale, txtSalePercent, txtAvailable, txtUnavailable;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvtFood = itemView.findViewById(R.id.img_food);
            txt_name_food = itemView.findViewById(R.id.txt_name_food);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_price_sale = itemView.findViewById(R.id.txt_price_sale);
            txtSalePercent = itemView.findViewById(R.id.txtSalePercent);
            txtAvailable = itemView.findViewById(R.id.txtAvailable);
            txtUnavailable = itemView.findViewById(R.id.txtUnavailable);
            img_edit = itemView.findViewById(R.id.img_edit);
            img_delete = itemView.findViewById(R.id.img_delete);


        }
    }
}
