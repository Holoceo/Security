package com.redmadintern.mikhalevich.security.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.common.io.BaseEncoding;
import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.ui.fragment.LoginFragment;
import com.redmadintern.mikhalevich.security.ui.fragment.PinFragment;
import com.redmadintern.mikhalevich.security.utils.PrefsUtil;
import com.redmadintern.mikhalevich.security.utils.RootUtil;
import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

import butterknife.Bind;


public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (savedInstanceState == null) {
            boolean isAuthorized = PrefsUtil.isAuthorized(this);
            Fragment fragment;

            if (isAuthorized) {
                fragment = new PinFragment();
            } else {
                fragment = new LoginFragment();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();

        }

    }
}
