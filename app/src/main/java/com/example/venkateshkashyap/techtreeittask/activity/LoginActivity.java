package com.example.venkateshkashyap.techtreeittask.activity;
/**
 * Created by Venkatesh Kashyap on 08/25/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.venkateshkashyap.techtreeittask.R;
import com.example.venkateshkashyap.techtreeittask.constants.AppConstants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;
    private LoginButton mFacebookSignInButton;
    private CallbackManager callbackManager;
    private AccessToken mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSignInButton = findViewById(R.id.btn_sign_in);
        mFacebookSignInButton = findViewById(R.id.login_button);
        //Arrays.asList("public_profile, email, user_birthday");
        //"email", "public_profile", "user_friends"
        mFacebookSignInButton.setReadPermissions("email", "public_profile", "user_friends");

        mSignInButton.setOnClickListener(this);
        mFacebookSignInButton.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        callbackManager = CallbackManager.Factory.create();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, AppConstants.RC_SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.login_button:
                mFacebookSignInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mAccessToken = loginResult.getAccessToken();
                        getUserProfile(mAccessToken);
                    }

                    @Override
                    public void onCancel() {
                        Log.w(TAG, "facebookSignin" );
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.w(TAG, "signInResult:failed message=" + error.getMessage());
                    }
                });
        }

    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra(AppConstants.USER_PROFILE, object.toString());
                    startActivity(intent);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString(AppConstants.FIELDS, "id,name,email,picture.width(120).height(120)");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        //returned result from launching intent from GoogleSignInApi
        if (requestCode == AppConstants.RC_SIGN_IN) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignIn(signInResult);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    /**
     * this method is used to handle the signIn functionality
     *
     * @param signInResult result from launching intent from GoogleSignInApi
     */
    private void handleSignIn(GoogleSignInResult signInResult) {
        Log.d(TAG, "handleSignInResult:" + signInResult.getStatus());
        if (signInResult.isSuccess()) {
            //Sign in successful.
            GoogleSignInAccount account = signInResult.getSignInAccount();
            Log.e(TAG, "display name: " + account.getDisplayName());
            String userName = account.getDisplayName();
            String profileImageUrl = account.getPhotoUrl().toString();
            String email = account.getEmail();

            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra(userName, AppConstants.USERNAME);
            intent.putExtra(profileImageUrl, AppConstants.IMAGE_URL);
            intent.putExtra(email, AppConstants.EMAIL);
            startActivity(intent);
        } else {
            //Signed out
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignIn(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignIn(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //sign in is not available
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
