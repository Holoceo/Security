package com.redmadintern.mikhalevich.security.controller.network;

import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Alexander on 14.07.2015.
 */
public class HttpClient {
    private static OkHttpClient okHttpClient;

    private HttpClient() {
    }

    public static OkHttpClient getOkHttpInstance() {
        if (okHttpClient == null) {

            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add(Credentials.HOSTNAME, "sha1/SdRFQnBx52aKbcMlIq6ob95bTwM=")
                    .add(Credentials.HOSTNAME, "sha1/lfnXQ0sc5x3vQhHua+PA4CVvrZU")
                    .build();

            okHttpClient = new OkHttpClient();
            okHttpClient.setCertificatePinner(certificatePinner);
        }
        return okHttpClient;
    }
}
