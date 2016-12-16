package com.aqib.www.ipms;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;

    String usernameString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


    }

    public void Login(View view) {
        usernameString = username.getText().toString();
        passwordString = password.getText().toString();

        BackgroundTask4UserInfo bgtask = new BackgroundTask4UserInfo(this);
        bgtask.execute(usernameString,passwordString);
    }


    public class BackgroundTask4UserInfo extends AsyncTask<String, Void, String> {
        Context ctx;

        public BackgroundTask4UserInfo(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Login Succesful")) {
                Intent i=new Intent(LoginActivity.this, InvestigationBill.class);
                i.putExtra("user",usernameString);
                startActivity(i);

                LoginActivity.this.finish();
            } else if (result.equals("Failed")) {
                Toast.makeText(ctx, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
//            String method = params[0];
            String login_url = "http://tasin.eu.pn/login.php";
                String login_name = params[0];
                String login_pass = params[1];
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                            URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8");

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    Log.e("LSN",response);
                    return response;
                } catch (MalformedURLException e) {
                    Log.e("LSN",e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e("LSN",e.toString());
                    e.printStackTrace();
                }

            return "Unsuccessful";
        }
    }
}
