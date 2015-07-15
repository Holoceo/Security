package com.redmadintern.mikhalevich.security.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.network.Credentials;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 13.07.2015.
 */
public class LoginFragment extends DialogFragment {
    private static final String QUERY_CLIENT_ID = "client_id";
    private static final String QUERY_REDIRECT_URI = "redirect_uri";
    private static final String QUERY_RESPONSE_TYPE = "response_type";
    private static final String TYPE_TOKEN = "token";

    @Bind(R.id.web_view) WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        webView.setWebViewClient(mWebViewClient);
        String authUrl = getAuthUrl();
        webView.loadUrl(authUrl);
    }

    private String getAuthUrl() {
        Uri.Builder authUrlBuilder = Uri.parse(Credentials.AUTH_URL).buildUpon();
        authUrlBuilder.appendQueryParameter(QUERY_CLIENT_ID, Credentials.CLIENT_ID);
        authUrlBuilder.appendQueryParameter(QUERY_REDIRECT_URI, Credentials.AUTH_REDIRECT_URI);
        authUrlBuilder.appendQueryParameter(QUERY_RESPONSE_TYPE, TYPE_TOKEN);
        return authUrlBuilder.build().toString();
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(Credentials.AUTH_REDIRECT_URI)) {
                Uri uri = Uri.parse(url);
                String parts[] = uri.getFragment().split("=");
                String token = parts[1];

                Fragment pinFragment = PinFragment.getInstance(token);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, pinFragment);
                fragmentTransaction.commit();

                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };
}
