package com.example.hoteleats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;

    private List<Product> products;
    MyDatabaseHelper myDB;

    public CustomAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.myDB = new MyDatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_recycler_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.foodName.setText(String.valueOf(products.get(position).getProductName()));
        holder.price.setText(String.valueOf(products.get(position).getPrice()));
        if(products.get(position).getStock().matches("Stock In")) {
            holder.radioGroup.check(R.id.radioButton5);
        } else {
            holder.radioGroup.check(R.id.radioButton6);
        }

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton5:
                        Toast.makeText(context, "Stock In", Toast.LENGTH_SHORT).show();
                        myDB.updateStock("Stock In", String.valueOf(products.get(position).getId()));
                        break;
                    case R.id.radioButton6:
                        Toast.makeText(context, "Stock Out", Toast.LENGTH_SHORT).show();
                        myDB.updateStock("Stock Out", String.valueOf(products.get(position).getId()));
                        break;
                    default:
                        Toast.makeText(context, "nothing selected", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView foodName, price;
        RadioGroup radioGroup;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.textView7);
            price = itemView.findViewById(R.id.textView8);
            radioGroup = itemView.findViewById(R.id.radioGroup2);

        }
    }
}
