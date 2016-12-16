package com.aqib.www.ipms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;

public class InvestigationBill extends AppCompatActivity {
public String ID="";
    public static String name,age,contact;
    public static int totalAmount=0;
//    String discount;
//    EditText discountEt,receivedAmount;

    String TITLES[] = {"Home","Search By Doctor","Search By Patient","Search By Admin"};
    int ICONS[] = {R.drawable.search,R.drawable.search,R.drawable.search,R.drawable.search};
    int PROFILE = R.drawable.logo;

//    TextView payable;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;

    EditText patientName, patientAge, patientSex, patientContact, refferedDoctor;

    List<Test> testsList;

    String admin;

//    Spinner spinner;

    public static ArrayList<Patient> data;
    ListView listView;
    ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigation_bill);

        admin=getIntent().getStringExtra("user");
//        Toast.makeText(this,admin,Toast.LENGTH_SHORT).show();

        listView = (ListView) findViewById(R.id.listView);

        patientAge = (EditText) findViewById(R.id.patientAge);
        patientName = (EditText) findViewById(R.id.patientName);
        patientSex = (EditText) findViewById(R.id.patientSex);
        patientContact = (EditText) findViewById(R.id.patientContact);
        refferedDoctor = (EditText) findViewById(R.id.referedDcotor);
//        discountEt=(EditText)findViewById(R.id.discount);
//        receivedAmount=(EditText)findViewById(R.id.receivedEt);
//        payable=(TextView)findViewById(R.id.payableTV);

//        discountEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                discount=discountEt.getText().toString();
//                int dis=Integer.parseInt(discount);
//                int pay=totalAmount-dis;
//                payable.setText(pay);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        data = new ArrayList<Patient>();
        testsList=new ArrayList<>();
        data.add(new Patient("Test Name", "Amount", "Price"));
        listAdapter = new ListAdapter(InvestigationBill.this, data);
        listView.setAdapter(listAdapter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(this,TITLES,ICONS,"IBN Sina Pathology","http://www.ibnsinapathology.com",PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State



//        spinner=(Spinner) findViewById(R.id.spinner);

        processJson();
    }

    private void processJson() {
        try {
            JSONObject jsonObject = new JSONObject(MainActivity.jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("tests");
            int count = 0;
            String test_id, testName, price,room;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                test_id = jo.getString("test_id");
                testName = jo.getString("test_name");
                price = jo.getString("price");
                room = jo.getString("room");

                testsList.add(new Test(test_id,testName,price,room));
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void SetUpList(String testName, String amount) {
        int price=0;
        for (Test test:testsList) {
            if(test.getTestName().equals(testName)){
                price=Integer.parseInt(amount)*Integer.parseInt(test.getPrice());
                totalAmount+=price;
            }
        }



        Patient patient = new Patient(testName, amount, price+"");

        data.add(patient);
        Patient patient1=new Patient("Total","",""+totalAmount);
        data.add(patient1);

        listAdapter.notifyDataSetChanged();

        Log.e("aqib","aqib");

    }

    public void addTest(View view) {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_input_layout, null);
        final Spinner spinner = (Spinner) v.findViewById(R.id.spinner);

        List<String> spinnerList = new ArrayList<String>();
        for(int i=0;i<testsList.size();i++){
            spinnerList.add(testsList.get(i).getTestName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,spinnerList );

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        final EditText quantity = (EditText) v.findViewById(R.id.quantity);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
        aboutDialog.setView(v).
                setCancelable(false).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String test = spinner.getSelectedItem().toString();
                        String amount = quantity.getText().toString();
                        if (amount.equals("")){
                            amount="1";
                        }

//                        Toast.makeText(InvestigationBill.this,amount,Toast.LENGTH_SHORT).show();

                        SetUpList(test, amount);

//                        saveToDatabase
                        SaveDataInDatabase saveDataInDatabase = new SaveDataInDatabase(InvestigationBill.this);
                        name=patientName.getText().toString();
                        age=patientAge.getText().toString();
                        contact=patientContact.getText().toString();
                        saveDataInDatabase.execute(name, age, contact, patientSex.getText().toString(),
                                refferedDoctor.getText().toString(), spinner.getSelectedItem().toString(), amount);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).
                create().
                show();
    }



    public class SaveDataInDatabase extends AsyncTask<String, Void, String> {
        Context ctx;

        public SaveDataInDatabase(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
//            String method = params[0];
            String reg_url = "http://tasin.eu.pn/insert.php";
//            if (method.equals("register")) {
            String name, age, contact, sex, refferedDoctor, test, amount;
            name = params[0];
            age = params[1];
            contact = params[2];
            sex = params[3];
            refferedDoctor = params[4];
            test = params[5];
            amount = params[6];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8") + "&" +
                        URLEncoder.encode("contact", "UTF-8") + "=" + URLEncoder.encode(contact, "UTF-8") + "&" +
                        URLEncoder.encode("sex", "UTF-8") + "=" + URLEncoder.encode(sex, "UTF-8") + "&" +
                        URLEncoder.encode("refferedDoctor", "UTF-8") + "=" + URLEncoder.encode(refferedDoctor, "UTF-8") + "&" +
                        URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&" +
                        URLEncoder.encode("admin", "UTF-8") + "=" + URLEncoder.encode(admin, "UTF-8") + "&" +
                        URLEncoder.encode("test", "UTF-8") + "=" + URLEncoder.encode(test, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                String response = "";
                String line = "";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                IS.close();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            }
            return "Unsuccessful";
        }

        @Override
        protected void onPostExecute(String result) {
//            if (result.equals("Registration Success.......!")) {
//                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            ID=result;
            Log.e("error",result);
//            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next) {
            Intent i=new Intent(InvestigationBill.this,ShowCalc.class);
            i.putExtra("total",totalAmount);
            i.putExtra("name",name);
            i.putExtra("age",age);
            i.putExtra("con",contact);
            i.putExtra("id",ID);
            startActivity(i);
//
        }
        return super.onOptionsItemSelected(item);
    }
}
