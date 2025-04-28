package com.example.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.Models.Category;
import com.example.expensemanager.Models.Transaction;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowTransactionBinding;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{

    Context context;
    ArrayList<Transaction> transactions;

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
          Transaction transaction = transactions.get(position);
          holder.binding.transactionAmt.setText(String.valueOf(transaction.getAmount()));
          holder.binding.paymentMode.setText(transaction.getAccount());

          holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
          holder.binding.Transactioncategory.setText(transaction.getCategory());

          Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());
          holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
          holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

          holder.binding.paymentMode.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));

          if(transaction.getType().equals(Constants.EXPENSE)) {
              holder.binding.transactionAmt.setTextColor(context.getColor(R.color.redColor));
          } else if(transaction.getType().equals(Constants.INCOME)) {
              holder.binding.transactionAmt.setTextColor(context.getColor(R.color.greenColor));
          }



    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        RowTransactionBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
