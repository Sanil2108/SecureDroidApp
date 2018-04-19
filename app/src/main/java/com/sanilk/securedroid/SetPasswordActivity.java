package com.sanilk.securedroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.sanilk.securedroid.networking.JSONParser;
import com.sanilk.securedroid.networking.requests.CreateUserRequest;
import com.sanilk.securedroid.networking.requests.CreateUserRequestInterface;
import com.sanilk.securedroid.networking.requests.IsEmailTakenRequest;
import com.sanilk.securedroid.networking.responses.CreateUserResponse;
import com.sanilk.securedroid.networking.responses.IsEmailTakenResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetPasswordActivity extends AppCompatActivity {
    private static final String TAG="SET_PASSWORD_ACTIVITY";

    GoogleSignInAccount googleSignInAccount;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        context=this;

        final EditText editText=findViewById(R.id.activity_set_password_password);

        googleSignInAccount=getIntent().getParcelableExtra("GOOGLE_ACCOUNT");

        Button button=findViewById(R.id.activity_set_password_done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password=editText.getText().toString();
                Thread t=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String email=googleSignInAccount.getEmail();
                        try {
                            sendCreateUserRequest(new CreateUserRequestInterface() {
                                @Override
                                public void onComplete(CreateUserResponse createUserResponse) {
                                    Log.d(TAG, "Complete");

                                    Intent intent=new Intent(context, MainActivity.class);
                                    intent.putExtra("GOOGLE_ACCOUNT", googleSignInAccount);
                                    intent.putExtra("PASSWORD", password);
                                    startActivity(intent);
                                }
                            }, email, password);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        });
    }

    private void sendCreateUserRequest(CreateUserRequestInterface createUserRequestInterface, String email, String password) throws Exception{
        URL url = new URL(MainNetworkingService.URL);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.connect();

        DataOutputStream dos=new DataOutputStream(
                connection.getOutputStream()
        );
        CreateUserRequest createUserRequest=new CreateUserRequest();
        createUserRequest.setEmail(email);
        createUserRequest.setPassword(password);
        dos.writeUTF(createUserRequest.getJSONString());

        dos.flush();
        dos.close();
        dos=null;

        DataInputStream dis=new DataInputStream(
                connection.getInputStream()
        );
        String response=dis.readUTF();
        JSONParser jsonParser=new JSONParser();
        CreateUserResponse createUserResponse=(CreateUserResponse)jsonParser.parse(response);

        dis.close();
        dis=null;
        connection.disconnect();

        createUserRequestInterface.onComplete(createUserResponse);
    }
}
