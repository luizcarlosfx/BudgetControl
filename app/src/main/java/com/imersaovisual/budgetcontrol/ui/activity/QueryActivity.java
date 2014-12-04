package com.imersaovisual.budgetcontrol.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.imersaovisual.budgetcontrol.R;
import com.imersaovisual.budgetcontrol.model.Transaction;
import com.imersaovisual.budgetcontrol.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class QueryActivity extends BaseActivity {

    private RadioGroup displayModeRadioGroup;
    private DatePicker datePicker;
    private Button detailsButton;
    private Button loadButton;
    private TextView dateTextView;
    private TextView incomeTextView;
    private TextView debitTextView;
    private TextView balanceTextView;
    private DisplayMode mode = DisplayMode.DAY;

    private TransactionRepository repository;
    private List<Transaction> transactions = new ArrayList<Transaction>();

    private List<Transaction> filteredTransactions;

    private Calendar selectedCalendar = Calendar.getInstance();

    private enum DisplayMode {DAY, MONTH, YEAR}

    @Override
    public int getLayoutId() {
        return R.layout.activity_query;
    }

    @Override
    public int getCurrentPosition() {
        return 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new TransactionRepository(this);

        selectedCalendar.setTime(new Date());

        initializeViews();

        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTransactionData();

        updateGUI();
    }

    private void initializeViews() {
        displayModeRadioGroup = (RadioGroup) findViewById(R.id.rgDisplayMode);
        detailsButton = (Button) findViewById(R.id.details_button);
        loadButton = (Button) findViewById(R.id.loadButton);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        incomeTextView = (TextView) findViewById(R.id.incomeTextView);
        debitTextView = (TextView) findViewById(R.id.debitTextView);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
    }

    private void setListeners() {
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTransactionData();
                updateGUI();
            }
        });

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(QueryActivity.this, DetaisActivity.class);

                Transaction[] transactionArray = new Transaction[filterTransactions().size()];

                filterTransactions().toArray(transactionArray);

                intent.putExtra("transactions", transactionArray);

                startActivity(intent);
            }
        });

        displayModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dayRadioButton) {
                    mode = DisplayMode.DAY;
                } else if (checkedId == R.id.monthRadioButton) {
                    mode = DisplayMode.MONTH;
                } else if (checkedId == R.id.yearRadioButton) {
                    mode = DisplayMode.YEAR;
                }

                updateGUI();
            }
        });
    }

    private List<Transaction> filterTransactions() {
        List<Transaction> filteredList = new ArrayList<Transaction>();

        if (mode == DisplayMode.DAY) {
            for (Transaction transaction : transactions) {
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(transaction.getDate());

                if (calendar.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == selectedCalendar.get(Calendar.DAY_OF_MONTH)) {
                    filteredList.add(transaction);
                }
            }
        } else if (mode == DisplayMode.MONTH) {
            for (Transaction transaction : transactions) {
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(transaction.getDate());

                if (calendar.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH)) {
                    filteredList.add(transaction);
                }
            }
        } else if (mode == DisplayMode.YEAR) {
            filteredList.addAll(transactions);
        }

        return filteredList;
    }

    void updateGUI() {
        String selectedDate = "";

        if (mode == DisplayMode.DAY) {
            selectedDate = String.format("%s/%s/%s", selectedCalendar.get(Calendar.DAY_OF_MONTH), selectedCalendar.get(Calendar.MONTH) + 1, selectedCalendar.get(Calendar.YEAR));
        } else if (mode == DisplayMode.MONTH) {
            selectedDate = String.format("%s/%s", selectedCalendar.get(Calendar.MONTH) + 1, selectedCalendar.get(Calendar.YEAR));
        } else if (mode == DisplayMode.YEAR) {
            selectedDate = String.format("%s", selectedCalendar.get(Calendar.YEAR));
        }
        dateTextView.setText(selectedDate);

        filteredTransactions = filterTransactions();

        float totalIncome = 0;
        float totalDebit = 0;
        float totalBalance = 0;

        for (Transaction tr : filteredTransactions) {
            if (tr.getType() == Transaction.Type.INCOME) {
                totalIncome += tr.getValue();
            } else {
                totalDebit += tr.getValue();
            }
        }

        totalBalance = totalIncome + totalDebit;

        String balance = getResources().getString(R.string.balance);
        String currency = getResources().getString(R.string.currency);
        String income = getResources().getString(R.string.income);
        String debit = getResources().getString(R.string.debit);

        String balanceText = String.format(balance + ": " + currency + " %.2f", totalBalance);

        String incomeText = String.format(income + ": " + currency + " %.2f", totalIncome);

        String debitText = String.format(debit + ": " + currency + " %.2f", totalDebit);

        int redColorId = R.color.dark_red;

        int greenId = R.color.dark_green;

        int blackId = R.color.darken;

        int colorId = blackId;

        if (totalBalance > 0) {
            colorId = greenId;
        } else if (totalBalance < 0) {
            colorId = redColorId;
        }

        balanceTextView.setTextColor(getResources().getColor(colorId));

        incomeTextView.setText(incomeText);
        debitTextView.setText(debitText);
        balanceTextView.setText(balanceText);
    }

    void loadTransactionData() {
        selectedCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

        selectedCalendar.set(Calendar.MONTH, datePicker.getMonth());

        selectedCalendar.set(Calendar.YEAR, datePicker.getYear());

        long thisYear = yearTime(selectedCalendar.get(Calendar.YEAR));

        long nextYear = yearTime(selectedCalendar.get(Calendar.YEAR) + 1);

        String[] args = new String[]{thisYear + "", nextYear + ""};

        transactions = repository.find(TransactionRepository.DATE + " >= ? and " + TransactionRepository.DATE + " < ? ", args, null, null, null, null);
    }

    private long yearTime(int year) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        calendar.set(Calendar.MONTH, Calendar.JANUARY);

        calendar.set(Calendar.YEAR, year);

        return calendar.getTimeInMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
