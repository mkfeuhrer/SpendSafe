package com.example.mohit.spendsafe;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class minusmoney extends AppCompatActivity {

    private Calendar calendar;
    EditText amount,description;
    private TextView dateView;
    private EditText category;
    private TextView timeView;
    Button submit;
    private int year, month, day,hours,minutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minusmoney);
        dateView = (TextView) findViewById(R.id.textDate);
        timeView = (TextView) findViewById(R.id.textTime);
        category = (EditText) findViewById(R.id.category);
        amount = (EditText) findViewById(R.id.amount);
        submit = (Button)findViewById(R.id.submit);
        description = (EditText)findViewById(R.id.description);
        dateView.setFocusable(false);
        timeView.setFocusable(false);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v);
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showTime(hours,minutes);
        showDate(year, month+1, day);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase data=openOrCreateDatabase("SpendSafe",MODE_PRIVATE,null);
                data.execSQL("create table if not exists minus_money (category varchar primary key,amount varchar,description varchar,date varchar,time varchar);");
                String category_text = category.getText().toString();
                String amount_text = amount.getText().toString();
                String descrip_text = description.getText().toString();
                String date_text = dateView.toString();
                String time_text = timeView.toString();
                data.execSQL("insert into minus_money values ('" + category_text + "','" + amount_text + "','" + descrip_text + "','" + date_text + "','" + time_text + "');");
                Toast.makeText(minusmoney.this,"First add category",Toast.LENGTH_LONG).show();
                Intent i = new Intent(minusmoney.this, home.class);
                startActivity(i);
                finish();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }
    public void setTime(View view)
    {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        else if(id == 998)
        {
            return new TimePickerDialog(this,myTimeListener,hours,minutes,false);
        }
        return null;
    }


    private TimePickerDialog.OnTimeSetListener myTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    showTime(hourOfDay,minute);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private  void showTime(int hours,int minutes)
    {
        timeView.setText(new StringBuilder().append(hours).append(":")
                .append(minutes));
    }
    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
