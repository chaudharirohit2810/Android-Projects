package com.example.floatingactionbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClicked{
    FloatingActionButton fab;
    RecyclerView rvListCompleted, rvListNotCompleted;
    RecyclerView.LayoutManager layoutManager1, layoutManager2;
    ActionBar actionBar;
    public static RecyclerView.Adapter adapter1, adapter2;
    public static ArrayList<Task> tasks;
    public static ArrayList<Task> tasksCompleted;
    public static ArrayList<Task> tasksNotCompleted;
    SimpleDateFormat sdf;
    TaskDB taskDB;
    Date date;
    public static final String path ="com.example.floatingactionbutton.Settings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Splash.DarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();
        tasksCompleted = new ArrayList<>();
        tasksNotCompleted = new ArrayList<>();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(R.color.ActionBarDark));
        taskDB = new TaskDB(MainActivity.this);
        taskDB.open();
        tasks = taskDB.getTasks();
        taskDB.close();
        for(int i = 0; i < tasks.size(); i++) {
                if(tasks.get(i).getCompleted() == 1) {
                    tasksCompleted.add(tasks.get(i));
                }else {
                    tasksNotCompleted.add(tasks.get(i));
                }
        }
        //Recycler View


        rvListNotCompleted = (RecyclerView) findViewById(R.id.rvListNotCompleted);
        layoutManager2 = new LinearLayoutManager(this);
        rvListNotCompleted.setLayoutManager(layoutManager2);
        adapter2 = new TaskAdapter(this, tasksNotCompleted);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvListNotCompleted);
        rvListNotCompleted.setAdapter(adapter2);
        rvListNotCompleted.setHasFixedSize(true);


        rvListCompleted = (RecyclerView) findViewById(R.id.rvListCompleted);
        layoutManager1 = new LinearLayoutManager(this);
        rvListCompleted.setLayoutManager(layoutManager1);
        adapter1 = new TaskAdapter(this, tasksCompleted);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvListCompleted);
        rvListCompleted.setAdapter(adapter1);
        rvListCompleted.setHasFixedSize(true);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Add.class));
            }
        });

    }
    public void makeToast(String name) {
        Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
            final RecyclerView.ViewHolder newv = viewHolder;
            if(direction == ItemTouchHelper.LEFT) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
                alertdialog.setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                makeToast("Task Deleted");
                                taskDB.open();
                                taskDB.deleteEntry(tasks.get(tasks.indexOf(newv.itemView.getTag())).getName());
                                for(int j = 0; j < tasksNotCompleted.size(); j++) {
                                    if(tasks.get(tasks.indexOf(newv.itemView.getTag())).getName().equals(tasksNotCompleted.get(j).getName())) {
                                        tasksNotCompleted.remove(j);
                                        adapter2.notifyDataSetChanged();
                                        break;
                                    }
                                }
                                for(int j = 0; j < tasksCompleted.size(); j++) {
                                    if(tasks.get(tasks.indexOf(newv.itemView.getTag())).getName().equals(tasksCompleted.get(j).getName())) {
                                        tasksCompleted.remove(j);
                                        adapter1.notifyDataSetChanged();
                                        break;
                                    }
                                }
                                tasks.remove(tasks.indexOf(newv.itemView.getTag()));
                                taskDB.close();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter2.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        }).show();



            }
            if(direction == ItemTouchHelper.RIGHT) {
                taskDB.open();
                if(tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).getCompleted() == 0) {
                    makeToast("Task Completed");
                    taskDB.updateChecked(tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).getName(), 1);
                    for (int j = 0; j < tasksNotCompleted.size(); j++) {
                        if (tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).getName().equals(tasksNotCompleted.get(j).getName())) {
                            tasksCompleted.add(tasksNotCompleted.get(j));
                            tasksNotCompleted.remove(j);
                            break;
                        }
                    }
                    taskDB.close();
                    tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).setCompleted(1);
                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                }else {
                    makeToast("Task Not Completed");
                    taskDB.updateChecked(tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).getName(), 0);
                    for (int j = 0; j < tasksCompleted.size(); j++) {
                        if (tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).getName().equals(tasksCompleted.get(j).getName())) {
                                    tasksNotCompleted.add(tasksCompleted.get(j));
                            tasksCompleted.remove(j);
                            break;
                        }
                    }
                    taskDB.close();
                    tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).setCompleted(0);
                    adapter1.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                }
            }

        }

        @Override
        public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.swipeLeft))
                    .addSwipeLeftActionIcon(R.drawable.delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.swipeRight))
                    .addSwipeRightActionIcon(R.drawable.check)
                    .create()
                    .decorate();
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.settings) {
//            setTheme(R.style.AppThemeDark);
//            setContentView(R.layout.activity_main);
            startActivity(new Intent(MainActivity.this, Settings.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void whenChecked(int pos) {
        makeToast("Task Completed");
        for(int i = 0; i < tasks.size(); i++) {
            if(tasksNotCompleted.get(pos).getName().equals(tasks.get(i).getName())) {
                taskDB.open();
                taskDB.updateChecked(tasks.get(i).getName(), 1);
                tasks.get(i).setCompleted(1);
                taskDB.close();
                break;
            }
        }
        tasksCompleted.add(tasksNotCompleted.get(pos));
        tasksNotCompleted.remove(pos);
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void whenUnchecked(int pos) {
        makeToast("Task Not Completed");
        for(int i = 0; i < tasks.size(); i++) {
            if(tasksCompleted.get(pos).getName().equals(tasks.get(i).getName())) {
                taskDB.open();
                taskDB.updateChecked(tasks.get(i).getName(), 0);
                tasks.get(i).setCompleted(0);
                taskDB.close();
                break;
            }
        }
        tasksNotCompleted.add(tasksCompleted.get(pos));
        tasksCompleted.remove(pos);
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void displayInfo(int pos) {
        Intent intent = new Intent(MainActivity.this, Info.class);
        intent.putExtra("Task", tasksNotCompleted.get(pos).getName());
        intent.putExtra("Des", tasksNotCompleted.get(pos).getDescription());
        intent.putExtra("Date", tasksNotCompleted.get(pos).getDate());
        startActivity(intent);
    }


}
