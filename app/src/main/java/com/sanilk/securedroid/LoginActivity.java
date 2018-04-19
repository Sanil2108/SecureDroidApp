package com.sanilk.securedroid;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.sanilk.securedroid.networking.JSONParser;
import com.sanilk.securedroid.networking.requests.IsEmailTakenRequest;
import com.sanilk.securedroid.networking.requests.IsEmailTakenRequestInterface;
import com.sanilk.securedroid.networking.responses.IsEmailTakenResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    SignInButton signInButton;
    GoogleSignInClient client;

    private int RC_SIGN_IN=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton=findViewById(R.id.sign_in_button);

        final GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client=GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent=client.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    private void signInButtonClicked(final GoogleSignInClient client){
        if(client==null){
            finish();
        }
//        Handler uiHandler=new Handler(Looper.getMainLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//
//            }
//        };
    }

    private void sendIsEmailTakenRequest(IsEmailTakenRequestInterface isEmailTakenRequestInterface, String email) throws Exception{
        URL url = new URL(MainNetworkingService.URL);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.connect();

        DataOutputStream dos=new DataOutputStream(
                connection.getOutputStream()
        );
        IsEmailTakenRequest isEmailTakenRequest=new IsEmailTakenRequest();
        isEmailTakenRequest.setEmail(email);
        dos.writeUTF(isEmailTakenRequest.getJSONString());

        dos.flush();
        dos.close();
        dos=null;

        DataInputStream dis=new DataInputStream(
                connection.getInputStream()
        );
        String response=dis.readUTF();
        JSONParser jsonParser=new JSONParser();
        IsEmailTakenResponse isEmailTakenResponse=(IsEmailTakenResponse) jsonParser.parse(response);

        dis.close();
        dis=null;
        connection.disconnect();

        isEmailTakenRequestInterface.onComplete(isEmailTakenResponse);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount googleSignInAccount=task.getResult(ApiException.class);
                signedIn(googleSignInAccount);
            }catch (Exception e){

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        if(account==null){
            Intent signInIntent = client.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }else {
            signedIn(account);
        }
    }

    private Context context=this;
    public void signedIn(final GoogleSignInAccount googleSignInAccount){
        final String email=googleSignInAccount.getEmail();
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendIsEmailTakenRequest(new IsEmailTakenRequestInterface() {
                        @Override
                        public void onComplete(IsEmailTakenResponse isEmailTakenResponse) {
                            Intent intent;
                            if(isEmailTakenResponse.isEmailAvailable()){
                                intent=new Intent(context, SetPasswordActivity.class);
                            }else{
                                intent=new Intent(context, GetPasswordActivity.class);
                            }
                            intent.putExtra("GOOGLE_ACCOUNT", googleSignInAccount);
                            startActivity(intent);
                        }
                    },email);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        t.start();
//        Intent intent=new Intent(this, GetPasswordActivity.class);
//        intent.putExtra("GOOGLE_ACCOUNT", googleSignInAccount);
//        startActivity(intent);
    }
}
