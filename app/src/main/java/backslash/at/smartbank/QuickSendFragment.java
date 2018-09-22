package backslash.at.smartbank;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuickSendFragment extends Fragment {
    TextView textViewNFCStatusActive;
    TextView textViewNFCStatusPassive;

    public static QuickSendFragment newInstance() {
        QuickSendFragment fragment = new QuickSendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quick_send, container, false);

        textViewNFCStatusActive = v.findViewById(R.id.textViewNFCStatusActive);
        textViewNFCStatusPassive = v.findViewById(R.id.textViewNFCStatusPassive);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if(nfcAdapter != null && nfcAdapter.isEnabled()) {
            textViewNFCStatusActive.setVisibility(View.VISIBLE);
            textViewNFCStatusPassive.setVisibility(View.GONE);
        } else {
            textViewNFCStatusActive.setVisibility(View.GONE);
            textViewNFCStatusPassive.setVisibility(View.VISIBLE);
        }

        Intent test = new Intent(getActivity(), BillDetailsActivity.class);
        startActivity(test);

        return v;
    }
}
