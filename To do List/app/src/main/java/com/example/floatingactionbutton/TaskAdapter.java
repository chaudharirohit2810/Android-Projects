package com.example.floatingactionbutton;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.floatingactionbutton.R;
import com.example.floatingactionbutton.Task;

import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable {
    TaskDB taskDB;
    ArrayList<Task> people;
    ArrayList<Task> SearchList;
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
        SearchList = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        CheckBox check;
        TextView tvName, tvDate, tvOverdue, tvDes;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            check = itemView.findViewById(R.id.check);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvOverdue = itemView.findViewById(R.id.tvOverdue);
            tvDes = itemView.findViewById(R.id.tvDes);

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
        viewHolder.tvDes.setText(people.get(position).getDescription());
        viewHolder.tvDes.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            //run on background thread
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Task> filteredList = new ArrayList<>();
                if(charSequence.toString().isEmpty()) {
                    filteredList.addAll(SearchList);
                }else {
                    for(Task task1 : SearchList) {
                        if(task1.getName().toLowerCase().contains(charSequence.toString().toLowerCase().trim())) {
                            filteredList.add(task1);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            //run on ui thread
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                people.clear();
                people.addAll((Collection<? extends Task>) filterResults.values);
                notifyDataSetChanged();
            }
        };
        return filter;
    }

}
