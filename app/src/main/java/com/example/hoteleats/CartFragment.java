package com.example.hoteleats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    CartCustomAdapter customAdapter;
    Button buyBtn;

    MyDatabaseHelper myDb;

    private static final String TAG = "CartFragment";

    List<Cart> tmp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDb = new MyDatabaseHelper(view.getContext());
        buyBtn = view.findViewById(R.id.button);
        tmp = new ArrayList<>();
        Type listType = new TypeToken<List<Cart>>() {}.getType();

        preferences = view.getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        this.editor = preferences.edit();

        String cartItem = preferences.getString("cart", null);

        recyclerView = view.findViewById(R.id.cart_recycle);


        if (cartItem != null) {

            Gson gson = new Gson();

            tmp = gson.fromJson(cartItem, listType);



        }

        customAdapter = new CartCustomAdapter(view.getContext(), tmp);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cartItem = preferences.getString("cart", null);

                if (cartItem != null) {

                    Gson gson = new Gson();

                    List<Cart> cartItems = gson.fromJson(cartItem, listType);

                    String productList = "";
                    float totalPrice = 0;
                    String customerName = preferences.getString("name", "default");
                    String time;

                    for (int i = 0; i < cartItems.size(); i++) {
                        productList += cartItems.get(i).getFoodName() + " x ";
                        productList += String.valueOf(cartItems.get(i).getQuantity()) + "\n";

                        totalPrice += (Float.parseFloat(cartItems.get(i).getPrice()) * cartItems.get(i).getQuantity());
                    }

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
                    time = df.format(c.getTime());

                    Log.d(TAG, "onClick: customer name: " + customerName + ", product list: " + productList + ", total price: " + totalPrice + ", time: " + time);

                    myDb.generateHistory(new History(customerName, time, productList, String.valueOf(totalPrice)));


                }
            }
        });

    }
}