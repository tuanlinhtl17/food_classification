package com.example.foodclassificationapp.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodclassificationapp.R;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText fullName, email, password, passwordConfirm;
    private TextView alreadyUser;
    private Button signUp;
    private CheckBox termsConditions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        initialize();
        setEvents();
        return view;
    }

    private void initialize() {
        fullName = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.userEmail);
        password = view.findViewById(R.id.password);
        passwordConfirm = view.findViewById(R.id.passwordConfirm);
        alreadyUser = view.findViewById(R.id.alreadyUser);
        signUp = view.findViewById(R.id.signUpBtn);
    }

    private void setEvents() {
//        alreadyUser.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.already_user:
                new LoginActivity().startLoginFragment();
                break;
            case R.id.signUp:
                break;
        }
    }
}
