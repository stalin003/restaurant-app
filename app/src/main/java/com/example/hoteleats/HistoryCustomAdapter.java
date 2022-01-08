package com.example.hoteleats;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryCustomAdapter extends RecyclerView.Adapter<HistoryCustomAdapter.MyViewHolder> {

    private Context context;
    private List<History> historyList;

    public HistoryCustomAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_recycler_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.customerName.setText(String.valueOf(historyList.get(position).getName()));
        holder.time.setText(String.valueOf(historyList.get(position).getTime()));
        holder.productList.setText(String.valueOf(historyList.get(position).getProductList()));
        holder.price.setText(historyList.get(position).getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customerName, time, productList, price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            customerName = itemView.findViewById(R.id.customer_name);
            time = itemView.findViewById(R.id.purchase_time);
            productList = itemView.findViewById(R.id.purchased_items);
            price = itemView.findViewById(R.id.price);

        }
    }
}
