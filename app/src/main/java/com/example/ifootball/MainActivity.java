package com.example.ifootball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView info;
    private ProfilePictureView pictureView;
    private Button logoutButton, loginLocalButton, login_local_button;
    private ImageButton backButton, imageSigninButon;
    private EditText username, password;
    private String email, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        findId();
        loginLocalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureView.setVisibility(View.INVISIBLE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                loginLocalButton.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.VISIBLE);
                login_local_button.setVisibility(View.VISIBLE);
                loginLocal();
                back();
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        loginFacebook();
        logoutFacebook();

        signInButton.setSize(SignInButton.SIZE_WIDE);
        signinGoogle();
    }

    private void findId(){
        info = findViewById(R.id.info);
        loginButton = findViewById(R.id.login_button);
        pictureView = findViewById(R.id.avatar);
        signInButton = findViewById(R.id.sign_in_button);
        logoutButton = findViewById(R.id.logout_button);
        loginLocalButton = findViewById(R.id.loginLocal);
        username = findViewById(R.id.loginUserName);
        password = findViewById(R.id.loginPassword);
        login_local_button = findViewById(R.id.login_local);
        backButton = findViewById(R.id.back);
    }

    private void loginFacebook(){
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                result();
            }
            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void logoutFacebook(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                logoutButton.setVisibility(View.INVISIBLE);
                info.setText("");
                pictureView.setProfileId(null);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loginLocal(){
        login_local_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = username.getText().toString();
                pass = password.getText().toString();
                if (email.equals("thang@123") && pass.equals("thang123")){
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công ", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "mail: " + email + "," + pass, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signinGoogle(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult (signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);
//            updateUI(account);
        } catch (ApiException e) {
            Log.w("error", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    @Override
    protected void onStart() {
        logoutButton.setVisibility(View.INVISIBLE);
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    private void result(){
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON", response.getJSONObject().toString());
                try {
                    String email = object.getString("name");
                    info.setText(email);
                    pictureView.setProfileId(Profile.getCurrentProfile().getId());
                    loginButton.setVisibility(View.INVISIBLE);
                    logoutButton.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    private void back(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureView.setVisibility(View.VISIBLE);
                username.setVisibility(View.INVISIBLE);
                password.setVisibility(View.INVISIBLE);
                loginLocalButton.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                login_local_button.setVisibility(View.INVISIBLE);
            }
        });
    }
}
