package com.andeptrai.datn_restaurant_app_manager.Activity.ui.list_food;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.andeptrai.datn_restaurant_app_manager.CODE.CREATE_FOOD_NEW;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlCreateNewFood;
import static com.andeptrai.datn_restaurant_app_manager.URL.urlGetAllFoodByIdRes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFoodFragment extends Fragment {

    TextView txtAddNewFood;
    RecyclerView rvFood;
    ArrayList<Food> foodArrayList = new ArrayList<>();
    ListFoodAdapter foodAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFoodFragment newInstance(String param1, String param2) {
        ListFoodFragment fragment = new ListFoodFragment();
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
        View view =  inflater.inflate(R.layout.fragment_list_food, container, false);

        txtAddNewFood = view.findViewById(R.id.txtAddNewFood);
        rvFood = view.findViewById(R.id.rvFood);

        foodAdapter = new ListFoodAdapter(foodArrayList, getContext());
        LinearLayoutManager linearLayoutManagerFood = new LinearLayoutManager(getContext());
        rvFood.setAdapter(foodAdapter);
        rvFood.setLayoutManager(linearLayoutManagerFood);
        getAllFoodByIdRes();

        txtAddNewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "abcdef", Toast.LENGTH_LONG).show();
                dialogAddNewFood();
            }
        });

        return view;
    }

    private void dialogAddNewFood() {

        final Dialog dialogAddNewFood = new Dialog(getContext());
        dialogAddNewFood.setContentView(R.layout.dialog_add_new_food);

        final RadioGroup rbGroup = dialogAddNewFood.findViewById(R.id.rbGroup);
        //final RadioButton rbAvailable = dialogAddNewFood.findViewById(R.id.rbAvailable);
        //final RadioButton rbUnavailable = dialogAddNewFood.findViewById(R.id.rbUnavailable);
        final EditText edtNameNewFood = dialogAddNewFood.findViewById(R.id.edtNameNewFood);
        final EditText edtPriceNew = dialogAddNewFood.findViewById(R.id.edtPriceNew);
        final EditText edtPromotionNew = dialogAddNewFood.findViewById(R.id.edtPromotionNew);
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
                    Toast.makeText(getContext(), "Khuyến mãi phải lớn hơn 0% và nhỏ hơn 100%", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (rbGroup.getCheckedRadioButtonId() == R.id.rbAvailable){
                        Food food = new Food(InfoRestaurantCurr.currentId
                                , new RandomString(CREATE_FOOD_NEW, new Random()).nextString()
                                , edtNameNewFood.getText().toString()
                                , 1
                                , Integer.parseInt(edtPriceNew.getText().toString())
                                , promotionNew
                                , (new SimpleDateFormat("yyyy-MM-dd hh-mm-ss")).format(new Date()));
                        addNewFoodInDB(food);
                    }
                    else if (rbGroup.getCheckedRadioButtonId() == R.id.rbUnavailable){
                        Food food = new Food(InfoRestaurantCurr.currentId
                                , new RandomString(CREATE_FOOD_NEW, new Random()).nextString()
                                , edtNameNewFood.getText().toString()
                                , 0
                                , Integer.parseInt(edtPriceNew.getText().toString())
                                , promotionNew
                                , (new SimpleDateFormat("yyyy-MM-dd hh-mm-ss")).format(new Date()));
                        addNewFoodInDB(food);
                    }
                    else{
                        Toast.makeText(getContext(), "Chưa lựa chọn trạng thái!", Toast.LENGTH_SHORT).show();
                    }
                }
                dialogAddNewFood.dismiss();
            }
        });

        dialogAddNewFood.show();
    }

    private void addNewFoodInDB(final Food food) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlCreateNewFood
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Insert new food success")){
                    foodArrayList.add(0, food);
                    foodAdapter.notifyItemInserted(0);
                    Toast.makeText(getContext(), "Create new food success!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Create new food fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Create new food error ---"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("idRestaurant", food.getId_restaurant());
                params.put("idNewFood", food.getId_food());
                params.put("nameNewFood", food.getName_food());
                params.put("status", food.getStatus()+"");
                params.put("newPrice", food.getPrice()+"");
                params.put("newPromotion", food.getPromotion()+"");
                params.put("timeCreateNewFood", food.getTimeCreateFood());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getAllFoodByIdRes() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlGetAllFoodByIdRes
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("Get food this restaurant fail")){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            foodArrayList.add(new Food(jsonObject.getString("Id_restaurant"),
                                    jsonObject.getString("Id_food"),
                                    jsonObject.getString("Name_food"),
                                    jsonObject.getInt("Status"),
                                    jsonObject.getInt("Price"),
                                    jsonObject.getInt("Promotion")));
                        }
                        foodAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {

                        Toast.makeText(getContext(), "Get food error exception! -- " + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Get food fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Get all food by id res error!---"+error.toString(), Toast.LENGTH_LONG).show();
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
}