package com.example.foodclassificationapp.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.foodclassificationapp.R;
import com.example.foodclassificationapp.activity.MainActivity;
import com.example.foodclassificationapp.constant.Constant;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new LoginFragment(),
                            Constant.Login_Fragment).commit();
        }
    }

    protected void startLoginFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.frameContainer, new LoginFragment(), Constant.Login_Fragment).commit();
    }

    @Override
    public void onBackPressed() {

        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Constant.SignUp_Fragment);

        if (SignUp_Fragment != null)
            startLoginFragment();
        else
            super.onBackPressed();
    }

    private void loginAction() {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 3000);
        Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(loginIntent);
    }

}
