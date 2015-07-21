package com.redmadintern.mikhalevich.security.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.hash.Hashing;
import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.operations.Service;
import com.redmadintern.mikhalevich.security.ui.view.KeyboardView;
import com.redmadintern.mikhalevich.security.ui.view.PinView;
import com.redmadintern.mikhalevich.security.utils.EncryptUtil;
import com.redmadintern.mikhalevich.security.utils.PrefsUtil;
import com.redmadintern.mikhalevich.security.utils.RootUtil;

import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by Alexander on 15.07.2015.
 */
public class PinFragment extends DialogFragment {
    private static final String KEY_TOKEN = "token";

    private String token;
    private int currentPinPos;
    private char[] pin = new char[4];

    private PinView pinView;
    private KeyboardView keyboardView;

    public static DialogFragment getInstance(String token) {
        DialogFragment pinFragment = new PinFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TOKEN, token);
        pinFragment.setArguments(args);
        return pinFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle extras = getArguments();
        if (extras != null) {
            token = extras.getString(KEY_TOKEN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pin, container, false);
        pinView = new PinView(v);
        keyboardView = new KeyboardView(v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Введите PIN");
        if (savedInstanceState == null && RootUtil.isDeviceRooted())
            Toast.makeText(getActivity(), "На вашем телефоне стоит рут", Toast.LENGTH_LONG).show();

        keyboardView.setClickListener(new KeyboardView.OnClickListener() {
            @Override
            public void onNumberClicked(char num) {
                if (currentPinPos < 4) {
                    pinView.check(currentPinPos);
                    pin[currentPinPos] = num;
                    currentPinPos++;
                }

                if (currentPinPos == 4) {
                    CharSequence sequence = CharBuffer.wrap(pin);
                    String hashCode = Hashing.sha1().hashString(sequence, Charset.defaultCharset()).toString();
                    byte[] encryptKey = Hashing.md5().hashString(sequence, Charset.defaultCharset()).asBytes();

                    if (token == null) {
                        String savedPinHash = PrefsUtil.readPinHash(getActivity());
                        if (savedPinHash.equals(hashCode)) {
                            String encryptedToken = PrefsUtil.readToken(getActivity());
                            try {
                                String decryptedToken = EncryptUtil.decrypt(encryptKey, encryptedToken);
                                Service service = Service.getInstance();
                                service.setToken(decryptedToken);
                                service.setEncryptKey(encryptKey);
                                showStream();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Вы ввели неправильный пин", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //Authorization
                        PrefsUtil.writePinHash(getActivity(), hashCode);
                        try {
                            String encryptedToken = EncryptUtil.encrypt(encryptKey, token);
                            PrefsUtil.writeToken(getActivity(), encryptedToken);
                            Service.getInstance().setToken(token);
                            showStream();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onDelClicked() {
                if (currentPinPos > 0) {
                    pinView.uncheck(--currentPinPos);
                }
            }
        });
    }

    private void showStream() {
        Fragment fragment = new SectionsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
