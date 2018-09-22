package backslash.at.smartbank;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OverviewFragment extends Fragment {

    BankAccount current;
    TextView textViewCurrentBallance;
    TextView textViewDate;

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        textViewCurrentBallance = v.findViewById(R.id.textViewCurrentBallance);
        textViewDate = v.findViewById(R.id.textViewDate);

        ListView listViewOverview = v.findViewById(R.id.listViewOverview);
        final List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(new Transaction("ATSOURCE1", "ATDEST2", Calendar.getInstance().getTime(), 10.99, "AliExpress"));
        transactions.add(new Transaction("ATSOURCE2", "ATDEST2", Calendar.getInstance().getTime(), -9.99, "Amazon"));
        ArrayAdapter<Transaction> arrayAdapter = new ArrayAdapter<Transaction>(getContext(), R.layout.listitem_overview, R.id.textViewTransactionInformation, transactions) {
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView date = v.findViewById(R.id.textViewTransactionDate);
                TextView value = v.findViewById(R.id.textViewTransactionValue);
                TextView information = v.findViewById(R.id.textViewTransactionInformation);

                String dateText = DateFormat.format("yyyy.MM.dd", transactions.get(position).getDate()).toString();
                String valueText = transactions.get(position).getValue().toString();
                String informationText = transactions.get(position).getInformation();

                Double dValue = transactions.get(position).getValue();
                if(dValue > 0) {
                    value.setTextColor(Color.parseColor("#007F32"));
                } else if(dValue < 0) {
                    value.setTextColor(Color.RED);
                }

                date.setText(dateText);
                value.setText(valueText + " ITL");
                information.setText(informationText);

                return v;
            }
        };
        listViewOverview.setAdapter(arrayAdapter);

        Spinner spinner = v.findViewById(R.id.spinnerBankAccount);
        // Values
        ArrayAdapter<BankAccount> adapter = new ArrayAdapter<BankAccount>(getActivity(), android.R.layout.simple_spinner_item, MainActivity.bankAccounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // On select
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Toast.makeText(getActivity(), bankAccounts.get(position), Toast.LENGTH_SHORT).show();
                current = MainActivity.bankAccounts.get(position);
                setCurrentAccountInformation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Toast.makeText(getActivity(), "nix", Toast.LENGTH_SHORT).show();
            }
        });

        current = MainActivity.bankAccounts.get(0);
        setCurrentAccountInformation();

        return v;
    }

    private void setCurrentAccountInformation() {
        textViewCurrentBallance.setText(String.valueOf(current.getBalance()) + " ITL");
        textViewDate.setText(DateFormat.format("dd.MM.yyyy", Calendar.getInstance().getTime()));
        if(current.getBalance() > 0) {
            textViewCurrentBallance.setTextColor(Color.parseColor("#007F32"));
        } else if(current.getBalance() < 0) {
            textViewCurrentBallance.setTextColor(Color.RED);
        }
    }
}
