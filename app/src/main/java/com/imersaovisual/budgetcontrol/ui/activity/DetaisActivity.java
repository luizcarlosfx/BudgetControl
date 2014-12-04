package com.imersaovisual.budgetcontrol.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.imersaovisual.budgetcontrol.R;
import com.imersaovisual.budgetcontrol.model.Transaction;
import com.imersaovisual.budgetcontrol.ui.helper.MyLayoutManager;
import com.imersaovisual.budgetcontrol.ui.helper.TransactionRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuizCarlos on 03/12/2014.
 */
public class DetaisActivity extends ActionBarActivity {

    private LinearLayout detailsLayout;

    private TransactionRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transaction_details);

        detailsLayout = (LinearLayout)findViewById(R.id.details_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        Parcelable[] trParceables = intent.getParcelableArrayExtra("transactions");

        List<Transaction> transactions = new ArrayList<Transaction>(trParceables.length);

        for (Parcelable parcelable : trParceables) {
            Transaction tr = (Transaction) parcelable;

            transactions.add(tr);
        }

        RecyclerView recyclerView = new RecyclerView(this);

        mLayoutManager = new MyLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TransactionRecyclerAdapter(this, transactions);


        recyclerView.setAdapter(mAdapter);

        detailsLayout.addView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
