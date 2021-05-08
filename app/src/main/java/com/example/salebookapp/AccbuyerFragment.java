package com.example.salebookapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.salebookapp.entities.Account;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.salebookapp.entities.Customer;

import java.util.List;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class AccbuyerFragment extends Fragment {

    Button btnEdit, btnLogout, btnSaveEdit;
    EditText edtEditAddress, edtEditPhone;
    TextView tvAccountName, tvAccountEmail;
    View view;
    AwesomeValidation awesomeValidation;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accbuyer, container, false);

        setUp();
        setData();
        addValidation();
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtEditAddress.setEnabled(true);
                //edtEditAddress.setText("");
                //edtEditPhone.setText("");
                edtEditPhone.setEnabled(true);
                btnSaveEdit.setAlpha(1);
            }
        });
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){
                    String newAddress = edtEditAddress.getText().toString();
                    String newPhone = edtEditPhone.getText().toString();
                    int cusID = Utils.accLogin.getAccID();
                    AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase.getDatabase(getContext()).dao().changeCusProfile(newAddress,newPhone,cusID);
                        }
                    });
                    }
                }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void setUp() {
        btnEdit = view.findViewById(R.id.btn_editAcc);
        btnLogout = view.findViewById(R.id.btn_logOut);
        btnSaveEdit = view.findViewById(R.id.btn_saveEdit);
        edtEditAddress = view.findViewById(R.id.edt_addressEdit);
        edtEditPhone = view.findViewById(R.id.edt_phoneEdit);
        tvAccountEmail = view.findViewById(R.id.tv_accountEmail);
        tvAccountName = view.findViewById(R.id.tv_accountName);
    }
    private void addValidation(){
        awesomeValidation = new AwesomeValidation(BASIC);
        awesomeValidation.addValidation(getActivity(),R.id.edt_addressEdit, RegexTemplate.NOT_EMPTY,R.string.err_value);
        awesomeValidation.addValidation(getActivity(), R.id.edt_phoneEdit, RegexTemplate.TELEPHONE + RegexTemplate.NOT_EMPTY , R.string
                .err_phone);

    }
    private void setData(){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int cusID = LoginActivity.LOGIN_ID;
                Customer customer = AppDatabase.getDatabase(getContext()).dao().getCusById(Utils.accLogin.getAccID());
                tvAccountName.setText(customer.getFullName());
                tvAccountEmail.setText(Utils.accLogin.getUsername());
                edtEditAddress.setText(customer.getAddress());
                edtEditPhone.setText(customer.getPhoneNumber());

            }
        });
    }



}
