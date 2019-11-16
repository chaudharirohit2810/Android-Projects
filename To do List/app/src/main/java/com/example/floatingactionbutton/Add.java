package com.example.floatingactionbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NavUtils;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
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

public class Add extends AppCompatActivity {
    EditText etTask, etDes, etDate;
    Button btnAdd;
    Calendar calendar;
    SimpleDateFormat sdf;
    String DBdate;
    DatePickerDialog.OnDateSetListener dateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!Splash.DarkTheme) {
            setTheme(R.style.AppTheme);
        }
//        setTheme(Splash.DarkTheme ? R.style.AppThemeDark : R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add a Task");
        actionBar.setDisplayHomeAsUpEnabled(true);

        etTask = findViewById(R.id.etTask);
        etDes = findViewById(R.id.etDes);
        etDate = findViewById(R.id.etDate);
        btnAdd = findViewById(R.id.btnAdd);

        calendar = Calendar.getInstance();
        final Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM");
        final String formattedDate = simpleDateFormat.format(date);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };
        etDate.setText(formattedDate);

        etDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Add.this, dateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TaskName = etTask.getText().toString();
                String Des = etDes.getText().toString();
                String Date = etDate.getText().toString();
                int is_overdue = 0;

                calendar = Calendar.getInstance();
                String myFormat = "dd MMM"; //In which you need put here
                sdf = new SimpleDateFormat(myFormat, Locale.US);
                String Today = sdf.format(calendar.getTime());
                try {
                    if(sdf.parse(Today).after(sdf.parse(Date))) {
                        is_overdue = 1;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(TaskName.isEmpty()) {
                    makeToast("Enter Valid Name");
                }else {
                    makeToast("Task Added");
                    AddData(TaskName, Des, Date, is_overdue);
                    Intent intent = new Intent(Add.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.settings) {
            startActivity(new Intent(Add.this, Settings.class));
        }
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel() {
        String myFormat = "dd MMM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate.setText(sdf.format(calendar.getTime()));
    }

    public void AddData(String name, String Des, String Date, int is_overdue) {
        TaskDB taskDB = new TaskDB(Add.this);
        taskDB.open();
        taskDB.createTask(name, Des, Date, 0, is_overdue);
        taskDB.getTasks();
        taskDB.close();
    }

    public void makeToast(String name) {
        Toast.makeText(Add.this, name, Toast.LENGTH_SHORT).show();
    }
}
