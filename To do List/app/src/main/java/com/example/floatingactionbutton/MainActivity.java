package com.example.floatingactionbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
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
    TaskDB taskDB;
    public static final String path ="com.example.floatingactionbutton.Settings";
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Splash.DarkTheme) {
//            setTheme(R.style.AppThemeDark);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTask(); //To get Tasks in Database
        //Recycler View
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);

        rvListNotCompleted = (RecyclerView) findViewById(R.id.rvListNotCompleted);
        layoutManager2 = new LinearLayoutManager(this);
        rvListNotCompleted.setLayoutManager(layoutManager2);
        adapter2 = new TaskAdapter(this, tasksNotCompleted);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvListNotCompleted);
        rvListNotCompleted.setAdapter(adapter2);
        rvListNotCompleted.setHasFixedSize(true);
//        rvListNotCompleted.addItemDecoration(dividerItemDecoration);


        rvListCompleted = (RecyclerView) findViewById(R.id.rvListCompleted);
        layoutManager1 = new LinearLayoutManager(this);
        rvListCompleted.setLayoutManager(layoutManager1);
        adapter1 = new TaskAdapter(this, tasksCompleted);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvListCompleted);
        rvListCompleted.setAdapter(adapter1);
        rvListCompleted.setHasFixedSize(true);
//        rvListCompleted.addItemDecoration(dividerItemDecoration);

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
                makeToast("Task Deleted");
                Task task = tasks.get(0);
                int j = 0;
                boolean flag1 = false;
                taskDB.open();
                taskDB.deleteEntry(tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).getName());
                for(j = 0; j < tasksNotCompleted.size(); j++) {
                    if(tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).getName().equals(tasksNotCompleted.get(j).getName())) {
                        flag1 = true;
                        task = tasksNotCompleted.get(j);
                        tasksNotCompleted.remove(j);
                        break;
                    }
                }
                for(j = 0; j < tasksCompleted.size(); j++) {
                    if(tasks.get(tasks.indexOf(viewHolder.itemView.getTag())).getName().equals(tasksCompleted.get(j).getName())) {
                        task = tasksCompleted.get(j);
                        tasksCompleted.remove(j);
                        break;
                    }
                }
                tasks.remove(tasks.indexOf(viewHolder.itemView.getTag()));
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                taskDB.close();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Task Deleted", Snackbar.LENGTH_SHORT);
                if(!Splash.DarkTheme) {
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.white));
                }
                final Task finalTask = task;
                final boolean finalFlag = flag1;
                final int finalJ = j;
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        taskDB.open();
                        taskDB.createTask(finalTask.getName(), finalTask.getDescription(), finalTask.getDate(), finalTask.getCompleted(), finalTask.getIsOverdue());
                        taskDB.close();
                        tasks.add(finalTask);
                        if(finalFlag) {
                            tasksNotCompleted.add(finalTask);
                            adapter2.notifyDataSetChanged();
                        }else {
                            tasksCompleted.add(finalTask);
                            adapter1.notifyDataSetChanged();
                        }
                    }
                });
                snackbar.show();


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
            if(direction == ItemTouchHelper.DOWN) {
//                viewHolder.itemView.findViewById(R.id.tvDes).setVisibility(View.VISIBLE);
                makeToast("Swiped Down");
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
            startActivity(new Intent(MainActivity.this, Settings.class));
        }else if(item.getItemId() == R.id.search) {
            androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    TaskAdapter adapter = (TaskAdapter) adapter2;
                    adapter.getFilter().filter(newText);
                    TaskAdapter taskAdapter = (TaskAdapter) adapter1;
                    taskAdapter.getFilter().filter(newText);
                    return false;
                }
            });
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

    private void setTask() {
        tasks = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.main_layout);
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
    }


}
