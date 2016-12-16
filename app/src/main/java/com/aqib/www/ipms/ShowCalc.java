package com.aqib.www.ipms;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowCalc extends AppCompatActivity {

    TextView totalTV, PayableTV;
    public int pay;

    EditText discountET, receivedET;
    public int receivedAmount;
    public int due;
    String discountStr;
    int total;
    String name, age, contact, ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calc);


        totalTV = (TextView) findViewById(R.id.totalTV);
        ID = getIntent().getStringExtra("id");
        total = getIntent().getIntExtra("total", 0);
        name = getIntent().getStringExtra("name");
        contact = getIntent().getStringExtra("contact");
        age = getIntent().getStringExtra("age");
        Log.d("lsn", total + "");
        totalTV.setText(total + "");
        discountET = (EditText) findViewById(R.id.discount);

//
        PayableTV = (TextView) findViewById(R.id.payableTV);
//
        receivedET = (EditText) findViewById(R.id.receivedEt);
//        int received=Integer.parseInt(receivedET.getText().toString());
        discountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e("baal",s.toString());
                String discount = s.toString();
                if (discount.equals("")) {
                    discount = "0";
                }
                pay = total - (total * Integer.parseInt(discount)) / 100;
                PayableTV.setText(pay + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void Print(View view) {
        discountStr = discountET.getText().toString();
        String receive = receivedET.getText().toString();
        if (receive.equals("")) {
            receive = "0";
        }
        receivedAmount = Integer.parseInt(receive);
        due = pay - receivedAmount;


        View v = LayoutInflater.from(this).inflate(R.layout.dialog_receipt, null);
        TextView nameT = (TextView) v.findViewById(R.id.dialogNameTV);
        nameT.setText(name);
        TextView ageT = (TextView) v.findViewById(R.id.dialogAgeTV);
        ageT.setText(age);
        TextView contactT = (TextView) v.findViewById(R.id.dialogContactTV);
        contactT.setText(contact);
        TextView received = (TextView) v.findViewById(R.id.dialogReceivedTV);
        received.setText(receivedAmount + "");
        TextView totalT = (TextView) v.findViewById(R.id.dialogTotalTV);
        totalT.setText(total + "");
        TextView discountT = (TextView) v.findViewById(R.id.dialogDiscountTV);
        discountT.setText(discountStr);
        TextView payT = (TextView) v.findViewById(R.id.dialogPayableTV);
        payT.setText(pay + "");
        TextView dueT = (TextView) v.findViewById(R.id.dialogDueTV);
        dueT.setText(due + "");
        TextView IDTV=(TextView)v.findViewById(R.id.IDTV);
        IDTV.setText(ID);

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
        aboutDialog.setView(v).
                setCancelable(true).
                create().
                show();
    }
}
