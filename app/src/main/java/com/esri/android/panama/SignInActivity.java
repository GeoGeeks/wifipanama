package com.esri.android.panama;

/**
 * Created by semillero on 20/01/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.esri.android.oauth.OAuthView;
import com.esri.android.oauth.OAuthView.OnSslErrorListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.io.EsriSecurityException;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.CallbackListener;
import com.esri.core.portal.Portal;
import com.esri.core.portal.PortalInfo;




/**
 * Implements the sign in UX to ArcGIS portal accounts. Handles sign in to OAuth and non-OAuth secured portals.
 */
public class SignInActivity extends Activity implements OnClickListener, TextWatcher {

    public static final String TAG = SignInActivity.class.getSimpleName();

    private static final String MSG_OBTAIN_CLIENT_ID = "You have to provide a client id in order to do OAuth sign in. You can obtain a client id by registering the application on https://developers.arcgis.com.";;;

    private static final String HTTPS = "https://";

    private static final String HTTP = "http://";

    private AlertDialog alertaCredenciales;

    private static final int OAUTH_EXPIRATION_NEVER = -1;

    private AutoCompleteTextView mPortalUrlEditText;

    private View mContinueButton;

    private String mPortalUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

   /*setContentView(R.layout.sing_in_activity_portal_url_layout);

   // Get a reference to the AutoCompleteTextView in the layout
   mPortalUrlEditText = (AutoCompleteTextView) findViewById(R.id.sign_in_activity_portal_url_edittext);
   // Get the string array
   String[] url = getResources().getStringArray(R.array.url_array);
   // Create the adapter and set it to the AutoCompleteTextView
   ArrayAdapter<String> adapter =
           new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, url);
   mPortalUrlEditText.setAdapter(adapter);
   mPortalUrlEditText.addTextChangedListener(this);

   mContinueButton = findViewById(R.id.sign_in_activity_continue_button);
   mContinueButton.setOnClickListener(this);
   mContinueButton.setEnabled(!mPortalUrlEditText.getText().toString().trim().isEmpty());

   View cancelButton = findViewById(R.id.sign_in_activity_cancel_button);
   cancelButton.setOnClickListener(this);*/
        new FetchAuthenticationTypeTask().execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_activity_continue_button:
                // determine what type of authentication is required to sign in to the specified portal
                new FetchAuthenticationTypeTask().execute();
                break;
            case R.id.sign_in_activity_cancel_button:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            return;
        }

        // update the enabled state of the Continue button
        String url = s.toString().trim();
        mContinueButton.setEnabled(StringUtils.isNotEmpty(url));
    }

    /**
     * Signs into the portal using the generateToken REST endpoint.
     */
    private void signInWithGenerateToken() {
        Toast.makeText(this, "GenerateToken-based sign in not implemented yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * Signs into the portal using OAuth2.
     */
    private void signInWithOAuth() {

        if (mPortalUrl.startsWith(HTTP)) {
            mPortalUrl = mPortalUrl.replace(HTTP, HTTPS);
        }

        String clientId = getString(R.string.client_id);
        if (StringUtils.isEmpty(clientId)) {
            Toast.makeText(this, MSG_OBTAIN_CLIENT_ID, Toast.LENGTH_SHORT).show();
            return;
        }

        // create an OAuthView and show it
        OAuthView oAuthView = new OAuthView(this, mPortalUrl, clientId, OAUTH_EXPIRATION_NEVER,
                new CallbackListener<UserCredentials>() {

                    @Override
                    public void onCallback(final UserCredentials credentials) {
                        if (credentials != null) {
                            Portal portal = new Portal(mPortalUrl, credentials);
                            PortalInfo portalInfo = null;

                            try {
                                // fetch the portal info and user details, they will be cached in the Portal instance
                                portalInfo = portal.fetchPortalInfo();
                                portal.fetchUser();
                            } catch (Exception e) {
                                onError(e);
                            }

                            // hold on to the initialized portal for later use
                            AccountManager.getInstance().setPortal(portal);

                            // enable standard license level
                            if (portalInfo != null) {
                                ArcGISRuntime.License.setLicense(portalInfo.getLicenseInfo());
                            }

                            // we are done signing in
                            Intent cambio = new Intent(SignInActivity.this, WifiPanama.class);
                            startActivity(cambio);
                            //finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SignInActivity.this, getString(R.string.failed_sign_in), Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                });
// handle SSL errors
        oAuthView.setOnSslErrorListener(new OnSslErrorListener() {
            @Override
            public void onReceivedSslError(OAuthView view, final SslErrorHandler handler, SslError error) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignInActivity.this);

                // set title
                alertDialogBuilder.setTitle("Advertencia");

                // set dialog message
                alertDialogBuilder.setMessage("El servidor al que intenta conectar no se puede verificar.\n¿Desea continuar?")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                handler.proceed();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // close the dialog box and showOAuthview again
                        dialog.cancel();
                    }
                });

                // create alert dialog
                alertaCredenciales = alertDialogBuilder.create();
                alertaCredenciales.show();
            }
        });

        setContentView(oAuthView);
    }

    /**
     * Fetches the PortalInfo asynchronously and determines the portal's authentication type.
     */
    private class FetchAuthenticationTypeTask extends AsyncTask<Void, Void, Integer> {

        private static final String TAG_PROGRESS_DIALOG = "TAG_PROGRESS_DIALOG";

        private static final int TYPE_UNDEFINED = -1;

        private static final int TYPE_OAUTH = 0;

        private static final int TYPE_GENERATE_TOKEN = 1;

        private ProgressDialogFragment mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialogFragment.newInstance(getString(R.string.verifying_portal));
            mProgressDialog.show(getFragmentManager(), TAG_PROGRESS_DIALOG);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int authType = TYPE_UNDEFINED;
            try {
                mPortalUrl = "http://www.arcgis.com";
                if (!mPortalUrl.startsWith(HTTP)) {
                    mPortalUrl = new StringBuilder(HTTP).append(mPortalUrl).toString();
                }

                Log.d(TAG, mPortalUrl);

                Portal portal = new Portal(mPortalUrl, null);
                PortalInfo portalInfo = portal.fetchPortalInfo();

                if (portalInfo != null) {
                    authType = portalInfo.isSupportsOAuth() ? TYPE_OAUTH : TYPE_GENERATE_TOKEN;
                }
            } catch (EsriSecurityException ese) {
                // Enterprise Windows auth throws this exception - assume it's not OAuth.
                if (ese.getCode() == EsriSecurityException.AUTHENTICATION_FAILED) {
                    authType = TYPE_GENERATE_TOKEN;
                }
            } catch (Exception e) {
                authType = TYPE_UNDEFINED;
            }

            return Integer.valueOf(authType);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            mProgressDialog.dismiss();

            switch (result.intValue()) {
                case TYPE_OAUTH:
                    signInWithOAuth();
                    break;
                case TYPE_GENERATE_TOKEN:
                    signInWithGenerateToken();
                    break;
                default:
                    // auth type could not be determined - just abort sign in
                    finish();
            }
        }
    }
}
