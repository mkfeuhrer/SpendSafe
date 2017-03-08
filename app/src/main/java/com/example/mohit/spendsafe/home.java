package com.example.mohit.spendsafe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Double sum_income=0.0,sum_expense=0.0;
    PieChart pieChart ;
    ArrayList<Entry> entries;
    ArrayList<BarEntry> entries_bar;
    ArrayList<String> PieEntryLabels,BarEntryLables ;
    PieDataSet pieDataSet;
    PieData pieData ;
    Button add,minus;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        pieChart = (PieChart) findViewById(R.id.chart1);
        entries = new ArrayList<>();
        entries_bar = new ArrayList<>();
        BarEntryLables = new ArrayList<String>();
        PieEntryLabels = new ArrayList<String>();
        String inputval="0.0";
        AddValuesToPIEENTRY("",inputval);

        add = (Button)findViewById(R.id.add);
        minus = (Button)findViewById(R.id.minus);
        //database fetch
        SQLiteDatabase data=openOrCreateDatabase("SpendSafe",MODE_PRIVATE,null);
        data.execSQL("create table if not exists add_money (amount varchar,description varchar,date varchar,time varchar);");
        data.execSQL("create table if not exists minus_money (category varchar primary key,amount varchar,description varchar,date varchar,time varchar);");
        String income_fetch = "select amount from add_money";
        Cursor income = data.rawQuery(income_fetch,null);
        if(income.moveToFirst())
        {
            do{
                String tmp_income;
                tmp_income = income.getString(0);
                //System.out.println(tmp_income);
                Double amt = Double.parseDouble(tmp_income);
                sum_income+=amt;
            }while(income.moveToNext());
        }
        String category_fetch = " SELECT category,amount from minus_money";
        Cursor cursor = data.rawQuery(category_fetch, null);
        if(cursor.moveToFirst())
        {
            do{
                String tmp_cat,tmp_amt;
                tmp_cat = cursor.getString(0);
                tmp_amt = cursor.getString(1);
                Double amt = Double.parseDouble(tmp_amt);
                sum_expense+=amt;
                System.out.println(tmp_amt);
                AddValuesToPIEENTRY(tmp_cat,tmp_amt);
            }while(cursor.moveToNext());
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home.this,addmoney.class);
                startActivity(i);
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  i = new Intent(home.this,minusmoney.class);
                startActivity(i);
            }
        });
        pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(15f);
        pieData = new PieData(PieEntryLabels, pieDataSet);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieChart.setData(pieData);
        pieChart.animateY(3000);
        pieChart.getLegend().setEnabled(false);
        pieChart.setDescription("");

        // pieChart.saveToGallery("mychart.jpg", 85);


        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.barchart);
        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
        Float sum_exp = Float.parseFloat(sum_expense.toString());
        Float sum_inc = Float.parseFloat(sum_income.toString());
        System.out.println(sum_exp+" "+sum_inc);
        entries_bar.add(new BarEntry(sum_exp, 1));
        entries_bar.add(new BarEntry(sum_inc, 0));
        BarDataSet bardataset = new BarDataSet(entries_bar, "Cells");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Income");
        labels.add("Expense");
        BarData data1 = new BarData(labels, bardataset);
        barChart.setData(data1);
        bardataset.setColors(ColorTemplate.PASTEL_COLORS);
        //YAxisValueFormatter custom = new MyYAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis =barChart.getAxisRight();
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)


    }

    public void AddValuesToPIEENTRY(String data,String amt){
        Float f = Float.parseFloat(amt);
        entries.add(new BarEntry(f, k));
        PieEntryLabels.add(data);
        k++;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}