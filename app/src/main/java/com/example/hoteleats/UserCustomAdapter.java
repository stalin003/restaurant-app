package com.example.hoteleats;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserCustomAdapter extends RecyclerView.Adapter<UserCustomAdapter.MyViewHolder> {

    Context context;
    List<Product> products;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    List<Cart> tmp;

    public UserCustomAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        this.editor = preferences.edit();
        this.tmp = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_row_recycler_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.foodName.setText(String.valueOf(products.get(position).getProductName()));
        holder.price.setText(String.valueOf(products.get(position).getPrice()));

        Type listType = new TypeToken<List<Cart>>() {}.getType();

        holder.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cartItem = preferences.getString("cart", null);

                if (cartItem != null) {
                    Gson gson = new Gson();
                    tmp = gson.fromJson(cartItem, listType);

                    for (int i =0; i < tmp.size(); i++) {
                        if (tmp.get(i).getFoodName().matches(products.get(position).getProductName())) {
                            Toast.makeText(context, products.get(position).getProductName() +" already added to cart", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                }

                tmp.add(new Cart(products.get(position).getProductName(), products.get(position).getPrice(),1 ));

                Gson gson = new Gson();

                String itemList = gson.toJson(tmp);

                editor.putString("cart", itemList);
                editor.commit();

                Toast.makeText(context, "added " + products.get(position).getProductName() + " to cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView foodName, price;
        Button buyBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.textView_food_name);
            price = itemView.findViewById(R.id.textView_food_price);

            buyBtn = itemView.findViewById(R.id.button_buy);

        }
    }
}
