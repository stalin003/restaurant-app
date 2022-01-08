package com.example.hoteleats;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CartCustomAdapter extends RecyclerView.Adapter<CartCustomAdapter.MyViewHolder>{

    Context context;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    List<Cart> carts;

    public CartCustomAdapter(Context context, List<Cart> carts) {
        this.context = context;
        this.carts = carts;
        this.preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_recycler_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.foodName.setText(String.valueOf(carts.get(position).getFoodName()));
        holder.amt.setText(String.valueOf(carts.get(position).getPrice()));
        holder.quantity.setText(String.valueOf(carts.get(position).getQuantity()));
        holder.multiply.setText(String.valueOf(carts.get(position).getQuantity()));

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                quantity++;
                holder.quantity.setText(String.valueOf(quantity));
                holder.multiply.setText(String.valueOf(quantity));

                Gson gson = new Gson();

                carts.get(position).setQuantity(quantity);
                String itemList = gson.toJson(carts);

                editor.putString("cart", itemList);
                editor.commit();

            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(holder.quantity.getText().toString());
                quantity--;

                if (quantity < 1) {
                    return;
                }

                holder.quantity.setText(String.valueOf(quantity));
                holder.multiply.setText(String.valueOf(quantity));

                Gson gson = new Gson();

                carts.get(position).setQuantity(quantity);
                String itemList = gson.toJson(carts);

                editor.putString("cart", itemList);
                editor.commit();
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                carts.remove(position);

                Gson gson = new Gson();

                String itemList = gson.toJson(carts);

                editor.putString("cart", itemList);
                editor.commit();
                notifyItemRemoved(position);

                Toast.makeText(context, "removed from cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button plus, minus;
        ImageButton deleteBtn;
        TextView amt, foodName, multiply;
        EditText quantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            plus = itemView.findViewById(R.id.plus_btn);
            minus = itemView.findViewById(R.id.minus_btn);
            amt = itemView.findViewById(R.id.textView_amt);
            foodName = itemView.findViewById(R.id.cart_food_name);
            quantity = itemView.findViewById(R.id.editTextNumberSigned);
            deleteBtn = itemView.findViewById(R.id.imageButton);
            multiply=itemView.findViewById(R.id.textView18);
        }
    }
}
