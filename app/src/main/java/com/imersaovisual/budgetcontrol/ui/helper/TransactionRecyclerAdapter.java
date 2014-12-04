package com.imersaovisual.budgetcontrol.ui.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imersaovisual.budgetcontrol.R;
import com.imersaovisual.budgetcontrol.model.Transaction;
import com.imersaovisual.budgetcontrol.repository.TransactionRepository;

import java.util.List;


public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionHolder> {

    private List<Transaction> transactionList;

    private Context mContext;

    public TransactionRecyclerAdapter(Context context, List<Transaction> transactionList) {
        this.transactionList = transactionList;
        this.mContext = context;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_card, null);
        TransactionHolder mh = new TransactionHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(TransactionHolder transactionListHolder, final int i) {
        final Transaction transaction = transactionList.get(i);

        int imageId = transaction.getValue() > 0 ? R.drawable.plus : R.drawable.minus;

        transactionListHolder.getValueImage().setImageResource(imageId);

        String date = String.format("%td/%<tm/%<tY", transaction.getDate());

        transactionListHolder.getDateTextView().setText(date);

        transactionListHolder.getDescriptionTextView().setText(transaction.getDescription());

        String currency = mContext.getResources().getString(R.string.currency);

        transactionListHolder.getValueTextView().setText(String.format(currency + " %.2f", transaction.getValue()));

        transactionListHolder.setDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransactionRepository repository = new TransactionRepository(mContext);

                repository.delete(transaction.getId());

                remove(transactionList.indexOf(transaction));
            }
        });
    }

    public void remove(int index)
    {
        transactionList.remove(index);

        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return (null != transactionList ? transactionList.size() : 0);
    }
}
