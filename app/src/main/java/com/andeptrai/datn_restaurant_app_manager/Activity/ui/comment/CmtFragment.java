package com.andeptrai.datn_restaurant_app_manager.Activity.ui.comment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andeptrai.datn_restaurant_app_manager.Activity.ui.list_food.Food;
import com.andeptrai.datn_restaurant_app_manager.Activity.ui.list_food.ListFoodAdapter;
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

import static com.andeptrai.datn_restaurant_app_manager.URL.urlGetAllCmtByIdRes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CmtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CmtFragment extends Fragment {

    RecyclerView rvCmt;
    ArrayList<Comment> commentArrayList = new ArrayList<>();
    CmtAdapter cmtAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CmtFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CmtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CmtFragment newInstance(String param1, String param2) {
        CmtFragment fragment = new CmtFragment();
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
        View view =  inflater.inflate(R.layout.fragment_cmt, container, false);

        rvCmt = view.findViewById(R.id.rvCmt);

        cmtAdapter = new CmtAdapter(commentArrayList, getContext());
        LinearLayoutManager linearLayoutManagerFood = new LinearLayoutManager(getContext());
        rvCmt.setAdapter(cmtAdapter);
        rvCmt.setLayoutManager(linearLayoutManagerFood);
        getAllCmtByIdRes();

        return  view;
    }

    private void getAllCmtByIdRes() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, urlGetAllCmtByIdRes
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("Get restaurant comment fail")){

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Comment comment = new Comment();
                            comment.setIdComt(jsonObject.getString("Id_cmt"));
                            comment.setIdUser(jsonObject.getInt("Id_user"));
                            comment.setNameCmter(jsonObject.getString("Name_user"));
                            comment.setIdRestaurant(jsonObject.getString("Id_restaurant"));
                            comment.setContent(jsonObject.getString("Content"));
                            comment.setCmtNumber(jsonObject.getInt("Reply_number"));

                            String listIdLike = jsonObject.getString("List_id_like_cmt");
                            comment.setListLike(listIdLike);
                            comment.setTime_create_cmt(jsonObject.getString("Time_create_cmt"));
                            comment.setPointReview(jsonObject.getDouble("Point_review"));

                            commentArrayList.add(comment);
                        }

                        cmtAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {

                        Toast.makeText(getContext(), "Loi! -- " + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Get all cmt fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Get cmt for this res fail!---"+error.toString(), Toast.LENGTH_LONG).show();
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