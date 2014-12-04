package com.imersaovisual.budgetcontrol.ui.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imersaovisual.budgetcontrol.R;
import com.imersaovisual.budgetcontrol.model.Transaction;

/**
 * Created by LuizCarlos on 03/12/2014.
 */
public class TransactionHolder extends RecyclerView.ViewHolder {

    private ImageView valueImage;
    private TextView dateTextView;
    private TextView valueTextView;
    private TextView descriptionTextView;
    private Button deleteButton;
    private Transaction transaction;

    public TransactionHolder(final View thisView) {
        super(thisView);
        valueImage = (ImageView) thisView.findViewById(R.id.transaction_image);
        dateTextView = (TextView) thisView.findViewById(R.id.transaction_tvDate);
        valueTextView = (TextView) thisView.findViewById(R.id.transaction_tvValue);
        descriptionTextView = (TextView) thisView.findViewById(R.id.transaction_tvDescription);
        deleteButton = (Button) thisView.findViewById(R.id.deleteButton);
    }

    public void setDeleteClickListener(View.OnClickListener onDeleteClickListener)
    {
        deleteButton.setOnClickListener(onDeleteClickListener);
    }

    public ImageView getValueImage() {
        return valueImage;
    }

    public void setValueImage(ImageView valueImage) {
        this.valueImage = valueImage;
    }

    public TextView getDateTextView() {
        return dateTextView;
    }

    public void setDateTextView(TextView dateTextView) {
        this.dateTextView = dateTextView;
    }

    public TextView getValueTextView() {
        return valueTextView;
    }

    public void setValueTextView(TextView valueTextView) {
        this.valueTextView = valueTextView;
    }

    public TextView getDescriptionTextView() {
        return descriptionTextView;
    }

    public void setDescriptionTextView(TextView descriptionTextView) {
        this.descriptionTextView = descriptionTextView;
    }

    public Transaction getTransaction()
    {
        return transaction;
    }
}