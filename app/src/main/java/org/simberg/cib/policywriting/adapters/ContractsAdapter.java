package org.simberg.cib.policywriting.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.simberg.cib.policywriting.R;
import org.simberg.cib.policywriting.activities.ContractDetailsActivity;
import org.simberg.cib.policywriting.activities.ContractsActivity;
import org.simberg.cib.policywriting.java.Constants;
import org.simberg.cib.policywriting.models.Contract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by javadbadirkhanly on 8/31/17.
 */

public class ContractsAdapter extends RecyclerView.Adapter<ContractsAdapter.ViewHolder> {

    private final static String TAG = ContractsAdapter.class.getSimpleName();

    private List<Contract> contracts;
    private Context context;
    private int contractsType;

    private int lastPosition = -1;

    public ContractsAdapter(Context context, List<Contract> contracts, int contractsType) {
        if (context instanceof ContractsActivity)
            this.context = context;
        this.contracts = contracts;
        this.contractsType = contractsType;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvDay;
        TextView tvMonth;
        View vLeftLine;
        TextView tvFullName;
        TextView tvContractNumber;
        TextView tvContractPrice;
        TextView tvContractStatus;
        TextView tvCarNumber;
        ImageView ivOperationTypeIcon;
        TextView tvOperationType;

        ViewHolder(View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDayContractsAdapter);
            tvMonth = itemView.findViewById(R.id.tvMonthContractsAdapter);
            vLeftLine = itemView.findViewById(R.id.vLeftColoredBorderContractsAdapter);
            tvFullName = itemView.findViewById(R.id.tvFullNameContractsAdapter);
            tvContractNumber = itemView.findViewById(R.id.tvContractNumberContractsAdapter);
            tvContractPrice = itemView.findViewById(R.id.tvContractPriceContractsAdapter);
            tvContractStatus = itemView.findViewById(R.id.tvStatusContractsAdapter);
            tvCarNumber = itemView.findViewById(R.id.tvCarNumberContractsAdapter);
            ivOperationTypeIcon = itemView.findViewById(R.id.ivOperationTypeIconContractsAdapter);
            tvOperationType = itemView.findViewById(R.id.tvOperationTypeContractsAdapter);

            itemView.setOnClickListener(this);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", contracts.get(getAdapterPosition()).getContractNumber());
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(view, "Contract number " +
                            contracts.get(getAdapterPosition()).getContractNumber() +
                             " has been copied to clipboard", Snackbar.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        void clearAnimation() {
            itemView.clearAnimation();
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ContractDetailsActivity.class);
            intent.putExtra(Constants.CONTRACT_NUMBER, contracts.get(getAdapterPosition()).getContractNumber());
            context.startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_contracts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvDay.setText(getDayMonth(contracts.get(position).getCreationDate()).split(" ")[0]);
        holder.tvMonth.setText(getDayMonth(contracts.get(position).getCreationDate()).split(" ")[1]);
        holder.tvFullName.setText(contracts.get(position).getInsuredName());
        holder.tvContractNumber.setText(contracts.get(position).getContractNumber());
        holder.tvContractPrice.setText(contracts.get(position).getPrice() + " " + "AZN");
        holder.tvContractStatus.setText(contracts.get(position).getStatus().getName());
        holder.tvCarNumber.setText(contracts.get(position).getCarNumber());

        if (contractsType == 0) {
            holder.ivOperationTypeIcon.setVisibility(View.VISIBLE);
            holder.tvOperationType.setVisibility(View.VISIBLE);
            holder.tvOperationType.setText(contracts.get(position).getCmtplType());
        } else {
            holder.ivOperationTypeIcon.setVisibility(View.GONE);
            holder.tvOperationType.setVisibility(View.GONE);
        }
        holder.vLeftLine.setBackgroundColor(getColor(contracts.get(position).getStatus().getCode()));
        holder.tvDay.setTextColor(getColor(contracts.get(position).getStatus().getCode()));
        holder.tvMonth.setTextColor(getColor(contracts.get(position).getStatus().getCode()));

        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return contracts.size();
    }

    private String getDayMonth(String inputDateStr) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM");

        Date date = null;

        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputFormat.format(date);
    }

    private int getColor(String statusCode) {
        switch (statusCode) {
            case "TERMINATED_CONTRACT_STATUS":
                return Color.parseColor("#918A96");
            case "CANCELLED_CONTRACT_STATUS":
                return Color.parseColor("#E74C3C");
            case "EXPIRED_CONTRACT_STATUS":
                return Color.parseColor("#9B59B6");
            case "ONGOING_CONTRACT_STATUS":
                return Color.parseColor("#1ABC9C");
            case "PENDING_CONTRACT_STATUS":
                return Color.parseColor("#F39C12");
            case "DRAFTED_CONTRACT_STATUS":
                return Color.parseColor("#3498Db");
            default:
                return Color.parseColor("#000000");
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }
}
