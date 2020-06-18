package com.nathanmbichoh.unity_sacco.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nathanmbichoh.unity_sacco.InvestmentActiveActivity;
import com.nathanmbichoh.unity_sacco.R;
import com.nathanmbichoh.unity_sacco.controller.CheckInternetConnection;
import com.nathanmbichoh.unity_sacco.pojo.InvestmentData;

import java.util.List;

public class MyInvestmentAdapter extends RecyclerView.Adapter<MyInvestmentAdapter.MyInvestmentViewHolder> {

    private List<InvestmentData> investmentDataList;

    public MyInvestmentAdapter(List<InvestmentData> investmentDataList){
        this.investmentDataList = investmentDataList;
    }

    @NonNull
    @Override
    public MyInvestmentAdapter.MyInvestmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.investments_layout, parent, false);
        return new MyInvestmentAdapter.MyInvestmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInvestmentAdapter.MyInvestmentViewHolder holder, int position) {
        InvestmentData investmentData = investmentDataList.get(position);

        holder.tvInvestmentId.setText(investmentData.getInvestmentId());
        holder.tvInvestmentDate.setText(investmentData.getInvestment_date());
        holder.tvInvestmentMaturityDate.setText(investmentData.getInvestment_maturity_date());
        holder.tvInvestmentAmount.setText(investmentData.getInvestment_amount());
        holder.tvInvestmentRate.setText(investmentData.getInvestment_rate());
        holder.tvInvestmentStatus.setText(investmentData.getInvestment_status());

        String status = investmentDataList.get(position).getInvestment_status();
        if (status.equalsIgnoreCase("Active")){
            holder.tvInvestmentStatus.setTextColor(Color.parseColor("#00E676"));
            holder.tvInvestmentStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_success, 0);
        }else if(status.equalsIgnoreCase("Complete")){
            holder.tvInvestmentStatus.setTextColor(Color.parseColor("#FF1744"));
            holder.tvInvestmentStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);
        }
    }



    @Override
    public int getItemCount() {
        return investmentDataList.size();
    }

    static class MyInvestmentViewHolder extends RecyclerView.ViewHolder{
        TextView tvInvestmentId, tvInvestmentDate, tvInvestmentMaturityDate, tvInvestmentAmount, tvInvestmentRate, tvInvestmentStatus;

        public MyInvestmentViewHolder(final View view){
            super(view);
            tvInvestmentId              = view.findViewById(R.id.txtInvestmentId);
            tvInvestmentDate            = view.findViewById(R.id.txtInvestmentDate);
            tvInvestmentMaturityDate    = view.findViewById(R.id.txtInvestmentMaturityDate);
            tvInvestmentAmount          = view.findViewById(R.id.txtInvestmentAmount);
            tvInvestmentRate            = view.findViewById(R.id.txtInvestmentRate);
            tvInvestmentStatus          = view.findViewById(R.id.txtInvestmentStatus);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckInternetConnection.checkConnection(v.getContext());
                    Intent intent = new Intent(v.getContext(), InvestmentActiveActivity.class);
                    intent.putExtra("INVESTMENT_ID",tvInvestmentId.getText());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
