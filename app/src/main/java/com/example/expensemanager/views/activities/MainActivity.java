package com.example.expensemanager.views.activities;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensemanager.Models.Transaction;
import com.example.expensemanager.R;
import com.example.expensemanager.adapters.TransactionsAdapter;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.views.fragments.AddTransactionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transaction");

        Constants.setCategories();
        calendar = Calendar.getInstance();
        updateDate();
        binding.nextDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE, 1);
            updateDate();
        });
        binding.prevDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE, -1);
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(view -> {
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","some note.....",new Date(),1000,1));
        transactions.add(new Transaction(Constants.EXPENSE,"Rent","Paytm","some note.....",new Date(),-1500,4));
        transactions.add(new Transaction(Constants.EXPENSE,"Investment","Bank","some note.....",new Date(),-500,2));
        transactions.add(new Transaction(Constants.INCOME,"Other","Cash","some note.....",new Date(),3000,6));

        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(this, transactions);
        binding.transactionList.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionList.setAdapter(transactionsAdapter);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void updateDate(){
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        binding.currDate.setText(Helper.formatDate(calendar.getTime()));
    }
}
