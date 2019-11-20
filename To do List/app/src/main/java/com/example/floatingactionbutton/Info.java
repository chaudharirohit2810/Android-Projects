package com.example.floatingactionbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Info extends AppCompatActivity {
    String Name, Des, Date;
    EditText etInfoTask, etInfoDes, etInfoDate;
    Button btnUpdate;
    Calendar calendar;
    SimpleDateFormat sdf;
    DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!Splash.DarkTheme) {
            setTheme(R.style.AppTheme);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Update");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Name = getIntent().getStringExtra("Task");
        Des = getIntent().getStringExtra("Des");
        Date = getIntent().getStringExtra("Date");
        etInfoTask = findViewById(R.id.etInfoTask);
        etInfoDes = findViewById(R.id.etInfoDes);
        etInfoDate = findViewById(R.id.etInfoDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        etInfoTask.setText(Name);
        etInfoDate.setText(Date);
        etInfoDes.setText(Des);

        calendar = Calendar.getInstance();
        final java.util.Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM");
        String formattedDate = simpleDateFormat.format(date);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };

        etInfoDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Info.this, dateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Name.equals(etInfoTask.getText().toString()) && Des.equals(etInfoDes.getText().toString()) && Date.equals(etInfoDate.getText().toString())) {
                    Intent intent = new Intent(Info.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Calendar calendar = Calendar.getInstance();
                    String myFormat = "dd MMM"; //In which you need put here
                    sdf = new SimpleDateFormat(myFormat, Locale.US);
                    String Today = sdf.format(calendar.getTime());
                    int is_overdue = 0;
                    try {
                        if(sdf.parse(Today).after(sdf.parse(etInfoDate.getText().toString()))) {
                            is_overdue = 1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    makeToast("Task Updated");
                    TaskDB taskDB = new TaskDB(Info.this);
                    taskDB.open();
                    taskDB.updateAll(etInfoTask.getText().toString(), etInfoDes.getText().toString(), etInfoDate.getText().toString(),is_overdue, Name);
                    taskDB.close();
                    Intent intent = new Intent(Info.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    public void makeToast(String name) {
        Toast.makeText(Info.this, name, Toast.LENGTH_SHORT).show();
    }

    private void updateLabel() {
        String myFormat = "dd MMM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etInfoDate.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
