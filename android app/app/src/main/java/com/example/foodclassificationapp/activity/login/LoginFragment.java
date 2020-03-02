package com.example.foodclassificationapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.MainActivity;
import com.example.foodclassificationapp.constant.Constant;


public class LoginFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText email, password;
    private TextView signUp;
    private Button loginBtn;
    private FragmentManager fragmentManager;
    private LinearLayout loginLayout;

    public LoginFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        initialize();
        setEvents();
        return view;
    }

    private void initialize() {
        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);
        signUp = view.findViewById(R.id.signUp);
        loginBtn = view.findViewById(R.id.loginBtn);
        fragmentManager = getActivity().getSupportFragmentManager();
        loginLayout = view.findViewById(R.id.login_layout);
    }

    private void setEvents() {
        loginBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.signUp:
                fragmentManager.beginTransaction()
                        .replace(R.id.frameContainer, new SignUpFragment(), Constant.SignUp_Fragment).commit();
                break;
        }
    }
}
