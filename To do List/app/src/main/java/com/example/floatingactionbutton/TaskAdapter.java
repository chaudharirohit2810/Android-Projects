package com.example.floatingactionbutton;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.floatingactionbutton.R;
import com.example.floatingactionbutton.Task;

import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    TaskDB taskDB;
    ArrayList<Task> people;
    Context context;
    ItemClicked activity;

    public interface ItemClicked {
        void whenChecked(int pos);
        void whenUnchecked(int pos);
        void displayInfo(int pos);
    }

    public TaskAdapter(Context context, ArrayList<Task> list) {
        activity = (ItemClicked) context;
        this.context = context;
        people = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        CheckBox check;
        TextView tvName, tvDate, tvOverdue;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            check = itemView.findViewById(R.id.check);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvOverdue = itemView.findViewById(R.id.tvOverdue);

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(check.isChecked()) {
                        activity.whenChecked(people.indexOf(view.getTag()));
                    }else {
                        activity.whenUnchecked(people.indexOf(view.getTag()));
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!check.isChecked()) {
                        activity.displayInfo(people.indexOf(view.getTag()));
                    }
                }
            });

        }
    }
    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        taskDB = new TaskDB(context);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitems, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(people.get(position));
        viewHolder.check.setTag(people.get(position));
        if(people.get(position).getIsOverdue() == 0) {
            viewHolder.tvOverdue.setVisibility(View.INVISIBLE);
        }
        if(people.get(position).getCompleted() == 1) {
            viewHolder.check.setChecked(true);
            viewHolder.tvName.setTextColor(Color.parseColor("#808080"));
            viewHolder.tvName.setPaintFlags(viewHolder.check.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if(people.get(position).getCompleted() == 1) {
            viewHolder.check.setChecked(true);
        }else{
            viewHolder.check.setChecked(false);
        }
        viewHolder.tvName.setText(people.get(position).getName());
        viewHolder.tvDate.setText(people.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}
