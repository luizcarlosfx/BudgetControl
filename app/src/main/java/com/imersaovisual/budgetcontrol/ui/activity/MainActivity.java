package com.imersaovisual.budgetcontrol.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imersaovisual.budgetcontrol.R;
import com.imersaovisual.budgetcontrol.model.Transaction;
import com.imersaovisual.budgetcontrol.repository.TransactionRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by LuizCarlos on 03/12/2014.
 */
public class MainActivity extends BaseActivity {

    private Button submitButton;

    private AutoCompleteTextView descriptionEditText;

    private EditText valueEditText;

    private EditText dateEditText;

    private DatePickerDialog datePicker;

    private TextView balanceTextView;

    private TransactionRepository repository;

    private List<Transaction> transactions;

    private Map<String, Transaction> filteredTransactions = new TreeMap<String, Transaction>();

    private Calendar selectedCalendar;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new TransactionRepository(this);

        selectedCalendar = Calendar.getInstance();

        selectedCalendar.setTime(new Date());

        updateTransactionList();

        initializeGUI();
    }

    private void updateTransactionList() {
        transactions = repository.findAll();

        for (Transaction tr : transactions) {

            String description = tr.getDescription();

            filteredTransactions.put(description, tr);
        }
    }


    private void updateGUIElements() {
        descriptionEditText.setText("");
        valueEditText.setText("");

        ArrayAdapter<String> adapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, filteredTransactions.keySet().toArray());

        descriptionEditText.setAdapter(adapter);

        selectedCalendar.setTime(new Date());

        dateEditText.setText(dateFormat.format(selectedCalendar.getTime()));

        double balance  = calculateBalance();

        String currency = getResources().getString(R.string.currency);

        String balanceText = String.format(currency + " %.2f", balance);

        int redColorId = R.color.dark_red;

        int greenId = R.color.dark_green;

        int blackId = R.color.darken;

        int colorId = blackId;

        if(balance > 0)
        {
            colorId = greenId;
        }
        else if(balance < 0)
        {
            colorId = redColorId;
        }

        balanceTextView.setTextColor(getResources().getColor(colorId));

        balanceTextView.setText(balanceText);
    }

    private void initializeGUI() {
        submitButton = (Button) findViewById(R.id.submitButton);

        descriptionEditText = (AutoCompleteTextView) findViewById(R.id.home_descriptionTextView);

        valueEditText = (EditText) findViewById(R.id.home_valueTextView);

        dateEditText = (EditText) findViewById(R.id.home_dateEditText);

        balanceTextView = (TextView) findViewById(R.id.home_balanceTextView);

        descriptionEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                double value = 0;

                String selectedDescription = ((TextView) view).getText().toString();

                for (Transaction tr : transactions) {
                    if (tr.getDescription().equals(selectedDescription)) {
                        value = tr.getValue();
                    }
                }
                valueEditText.setText(value + "");
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, monthOfYear, dayOfMonth);
                dateEditText.setText(dateFormat.format(selectedCalendar.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = descriptionEditText.getText().toString().trim();

                String valueText = valueEditText.getText().toString().trim();

                String errorMessage = null;

                if (description == null || description.equals("")) {
                    errorMessage = getResources().getString(R.string.description_null);
                } else if (valueText == null || valueText.equals("")) {
                    errorMessage = getResources().getString(R.string.value_null);

                    if (!valueText.matches("-?[0-9]+")) {
                        errorMessage = getResources().getString(R.string.value_notNumber);
                    }
                }

                if (errorMessage == null) {
                    double value = Double.parseDouble(valueText);

                    Date date = selectedCalendar.getTime();

                    Transaction transaction = new Transaction(description, date, value);

                    repository.save(transaction);

                    updateTransactionList();

                    updateGUIElements();

                    Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.transaction_saved), Toast.LENGTH_LONG);

                    toast.show();
                }
                else
                {
                    Toast toast = Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG);

                    toast.show();
                }
            }
        });

        updateGUIElements();
    }

    private double calculateBalance()
    {
        double balance = 0;

        for(Transaction tr : transactions)
        {
            balance += tr.getValue();
        }

        return balance;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}
