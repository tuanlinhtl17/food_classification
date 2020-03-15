package com.example.foodclassificationapp.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.main.MainActivity;

import com.example.foodclassificationapp.constant.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private View view;
    private EditText emailET;
    private EditText passwordET;
    private TextView signUp;
    private Button loginBtn;
    private FirebaseAuth fiAuth;
    private FirebaseAuth.AuthStateListener fiAuthStateListener;
    private FragmentManager fragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.login_fragment, container, false);
        initialize();
        setEvents();

        fiAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        };
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initialize() {
        emailET = view.findViewById(R.id.login_email);
        passwordET = view.findViewById(R.id.login_password);
        signUp = view.findViewById(R.id.signUp);
        loginBtn = view.findViewById(R.id.loginBtn);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fiAuth = FirebaseAuth.getInstance();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        fiAuth.addAuthStateListener(fiAuthStateListener);
//    }

    private void setEvents() {
        loginBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                login();
                break;
            case R.id.signUp:
//                fragmentManager.beginTransaction()
//                        .replace(R.id.loginContainer, new SignUpFragment(), Constant.SIGNUP_FRAGMENT).commit();
//                new LoginActivity().openFragment(new SignUpFragment());
                break;
            default:
                break;
        }
    }

    private void login() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            fiAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
