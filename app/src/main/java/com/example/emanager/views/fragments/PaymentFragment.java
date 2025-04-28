package com.example.emanager.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emanager.R;

import java.util.UUID;

public class PaymentFragment extends Fragment {

    private EditText etClientUpiId, etAmount;
    private Button btnPay;
    private static final int UPI_PAYMENT_REQUEST_CODE = 123;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        etClientUpiId = view.findViewById(R.id.etClientUpiId);
        etAmount = view.findViewById(R.id.etAmount);
        btnPay = view.findViewById(R.id.btnPay);

        btnPay.setOnClickListener(v -> {
            String clientUpiId = etClientUpiId.getText().toString().trim();
            String amount = etAmount.getText().toString().trim();

            if (!clientUpiId.isEmpty() && !amount.isEmpty()) {
                try {
                    double amountValue = Double.parseDouble(amount);
                    if (amountValue <= 0) {
                        Toast.makeText(requireContext(), "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
                    } else {
                        payUsingUpi(clientUpiId, amount);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Please enter UPI ID and amount", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void payUsingUpi(String clientUpiId, String amount) {
        String txnId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        String txnRefId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", clientUpiId)
                .appendQueryParameter("pn", "Client Name")
                .appendQueryParameter("tn", "Payment for services")
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("tid", txnId)
                .appendQueryParameter("tr", txnRefId)
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        if (chooser.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(chooser, UPI_PAYMENT_REQUEST_CODE);
        } else {
            Toast.makeText(requireContext(), "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK || resultCode == 11) {
                if (data != null) {
                    String response = data.getStringExtra("response");
                    if (response != null) {
                        if (response.toLowerCase().contains("success")) {
                            Toast.makeText(requireContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                        } else if (response.toLowerCase().contains("failed")) {
                            handlePaymentFailure(response);
                        } else {
                            Toast.makeText(requireContext(), "Transaction Failed: Unknown error", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Transaction failed: Cancelled by User", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Transaction cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handlePaymentFailure(String response) {
        if (response != null && response.contains("limit")) {
            Toast.makeText(requireContext(), "Payment failed: Bank limit exceeded. Try smaller amount.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(requireContext(), "Payment failed: " + response, Toast.LENGTH_SHORT).show();
        }
    }
}
