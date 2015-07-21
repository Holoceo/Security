package com.redmadintern.mikhalevich.security.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;
import com.redmadintern.mikhalevich.security.controller.operations.Service;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by android on 20/07/15.
 */
public class UserImagesFragment extends ImagesBaseFragment {
    private String userName = "sky";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void onLoadImages(final ImagesLoadedCallback cb) {
        final Service service = getService();
        service.fetchUserId(userName, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                service.fetchUserMedia(s, new Callback<List<String>>() {
                    @Override
                    public void success(List<String> strings, Response response) {
                        cb.success(strings);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        cb.failure(error.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                showUserInputDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showUserInputDialog() {
        final EditText txtUser = new EditText(getActivity());
        txtUser.setHint(R.string.dialog_search_user_hint);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_search_user_title)
                .setView(txtUser)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String user = txtUser.getText().toString();
                        userName = user;
                        loadImages();
                    }
                })
                .show();
    }
}
