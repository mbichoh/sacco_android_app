package com.nathanmbichoh.unity_sacco.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nathanmbichoh.unity_sacco.R;
import com.nathanmbichoh.unity_sacco.pojo.PaymentData;

import java.util.List;

public class MyPaymentAdapter extends RecyclerView.Adapter<MyPaymentAdapter.MyPaymentViewHolder> {

    private List<PaymentData> paymentDataList;

    public MyPaymentAdapter(List<PaymentData> paymentDataList){
        this.paymentDataList = paymentDataList;
    }

    @NonNull
    @Override
    public MyPaymentAdapter.MyPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.payment_layout, parent, false);
        return new MyPaymentAdapter.MyPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPaymentAdapter.MyPaymentViewHolder holder, int position) {
        PaymentData paymentData = paymentDataList.get(position);

        holder.tvPaymentId.setText(paymentData.getPaymentId());
        holder.tvPaymentName.setText(paymentData.getPayment_name());
        holder.tvPaymentAmounts.setText(paymentData.getPayment_amount());
        holder.tvPaymentDate.setText(paymentData.getPayment_date());
    }



    @Override
    public int getItemCount() {
        return paymentDataList.size();
    }

    static class MyPaymentViewHolder extends RecyclerView.ViewHolder{
        TextView tvPaymentId, tvPaymentDate, tvPaymentName, tvPaymentAmounts;

        public MyPaymentViewHolder(final View view){
            super(view);
            tvPaymentName       = view.findViewById(R.id.txtPaymentName);
            tvPaymentDate       = view.findViewById(R.id.txtPaymentDate);
            tvPaymentAmounts    = view.findViewById(R.id.txtPaymentAmount);
            tvPaymentId         = view.findViewById(R.id.txtPaymentPeriod);
        }
    }
}
