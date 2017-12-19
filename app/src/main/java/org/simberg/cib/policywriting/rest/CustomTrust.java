package org.simberg.cib.policywriting.rest;

import android.content.Context;

import org.simberg.cib.policywriting.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by javadbadirkhanly on 8/23/17.
 */

public class CustomTrust {

    private static CustomTrust sInstance;

    public static X509TrustManager trustManager = null;
    public static SSLSocketFactory sslSocketFactory = null;

    public static CustomTrust getInstance(Context context){
        if (sInstance == null){
            sInstance = new CustomTrust(context.getApplicationContext());
        }

        return sInstance;
    }

    public CustomTrust(Context context) {
           buildSslSocketFactory(context);
    }

    private static void buildSslSocketFactory(Context context) {
        // Add support for self-signed (local) SSL certificates
        // Based on http://developer.android.com/training/articles/security-ssl.html#UnknownCa
        try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From https://www.washington.edu/itconnect/security/ca/load-der.crt
            InputStream is = context.getResources().openRawResource(R.raw.mobile_proxy);
            InputStream caInput = new BufferedInputStream(is);
            Certificate ca;

            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            if (tmf.getTrustManagers().length != 1 || !(tmf.getTrustManagers()[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(tmf.getTrustManagers()));
            }

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            sslSocketFactory = sslContext.getSocketFactory();
            trustManager = (X509TrustManager) tmf.getTrustManagers()[0];

        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }
}
