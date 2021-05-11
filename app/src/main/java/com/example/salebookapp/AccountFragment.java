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
import android.widget.Toast;

import com.example.salebookapp.entities.Account;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.util.List;


public class AccountFragment extends Fragment {

    EditText edt_user, edt_pass;
    Button btn_signin, btn_signup, btn_quit;
    AwesomeValidation awesomeValidation;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        setUp();
        ControlButton();

        // Inflate the layout for this fragment
        return view;
    }

    private void ControlButton() {
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog);
                builder.setTitle("Bạn có chắc muốn thoát !");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getActivity().finish();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edt_user.getText().toString();
                String pass = edt_pass.getText().toString();

                if (user.length() != 0 && pass.length() != 0){
                    AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            List<Account> l = AppDatabase.getDatabase(getContext().getApplicationContext())
                                    .dao().getAccount(user);
                            if (l.size() > 0) {
                                if (l.get(0).getPassword().equals(pass)) {
                                    Utils.accLogin = l.get(0);
                                    Intent intent = new Intent(getContext(), HomeActivity.class);
                                    getActivity().finish();
                                    startActivity(intent);
                                } else {
                                    System.out.println("Mật khẩu chưa chính xác.");
                                    //Toast.makeText(getContext(), "Mật khẩu chưa chính xác.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                System.out.println("Tài khoản hoặc mật khẩu chưa chính xác.");
                                Toast.makeText(getContext(), "Tài khoản hoặc mật khẩu chưa chính xác.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(),"Mời bạn nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    void setUp() {
        edt_user = view.findViewById(R.id.edt_fusername);
        edt_pass = view.findViewById(R.id.edt_fpassword);

        btn_signup = view.findViewById(R.id.btn_fsignup);
        btn_signin = view.findViewById(R.id.btn_fsignin);
        btn_quit = view.findViewById(R.id.btn_fquit);
    }




}
