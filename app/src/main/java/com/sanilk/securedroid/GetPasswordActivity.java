package com.sanilk.securedroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.sanilk.securedroid.networking.JSONParser;
import com.sanilk.securedroid.networking.requests.IsClientAuthenticRequest;
import com.sanilk.securedroid.networking.requests.IsClientAuthenticRequestInterface;
import com.sanilk.securedroid.networking.responses.IsClientAuthenticResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPasswordActivity extends AppCompatActivity {
    private static final String TAG="GET_PASSWORD_ACTIVITY";

    GoogleSignInAccount googleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);

        googleSignInAccount=getIntent().getParcelableExtra("GOOGLE_ACCOUNT");
        TextView emailTextView=findViewById(R.id.activity_get_password_email);
        emailTextView.setText(googleSignInAccount.getEmail());

        final EditText passwordEditText=findViewById(R.id.activity_get_password_password);

        Button doneButton=findViewById(R.id.activity_get_password_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPassword(googleSignInAccount.getEmail(), passwordEditText.getText().toString());
            }
        });

    }

    private void getPassword(final String email, final String password){
        final Context context=this;
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendIsUserAuthenticRequest(new IsClientAuthenticRequestInterface() {
                        @Override
                        public void onComplete(IsClientAuthenticResponse isClientAuthenticResponse) {
                            Log.d(TAG, "onComplete.");
                            if(isClientAuthenticResponse.isSuccessful()) {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("GOOGLE_ACCOUNT", googleSignInAccount);
                                intent.putExtra("PASSWORD", password);
                                startActivity(intent);
                            }else{
                                Log.d(TAG, "WRONG EMAIL PASSWORD");
                            }
                        }
                    }, email, password);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });
        t.start();
    }

    private void sendIsUserAuthenticRequest(IsClientAuthenticRequestInterface isClientAuthenticRequestInterface,
                                            String email, String password) throws Exception{
        URL url = new URL(MainNetworkingService.URL);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.connect();

        DataOutputStream dos=new DataOutputStream(
                connection.getOutputStream()
        );
        IsClientAuthenticRequest isClientAuthenticRequest=new IsClientAuthenticRequest();
        isClientAuthenticRequest.setEmail(email);
        isClientAuthenticRequest.setPassword(password);
        dos.writeUTF(isClientAuthenticRequest.getJSONString());

        dos.flush();
        dos.close();
        dos=null;

        DataInputStream dis=new DataInputStream(
                connection.getInputStream()
        );
        String response=dis.readUTF();
        JSONParser jsonParser=new JSONParser();
        IsClientAuthenticResponse isClientAuthenticResponse=(IsClientAuthenticResponse)jsonParser.parse(response);

        dis.close();
        dis=null;
        connection.disconnect();

            isClientAuthenticRequestInterface.onComplete(isClientAuthenticResponse);
    }
}
