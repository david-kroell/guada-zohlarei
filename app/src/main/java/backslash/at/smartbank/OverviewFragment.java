package backslash.at.smartbank;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OverviewFragment extends Fragment {
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

        ListView listViewOverview = v.findViewById(R.id.listViewOverview);
        final List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(new Transaction("ATSOURCE1", "ATDEST2", Calendar.getInstance().getTime(), 10.99, "AliExpress"));
        transactions.add(new Transaction("ATSOURCE2", "ATDEST2", Calendar.getInstance().getTime(), 9.99, "Amazon"));
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

                date.setText(dateText);
                value.setText(valueText);
                information.setText(informationText);

                return v;
            }
        };
        listViewOverview.setAdapter(arrayAdapter);

        return v;
    }
}
