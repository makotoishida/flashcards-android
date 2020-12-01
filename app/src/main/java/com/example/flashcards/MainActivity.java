package com.example.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView list;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);
        list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        mDataset = new ArrayList<>();
        mDataset.add("AAAA");
        mDataset.add("BBBBB");
        mDataset.add("CCCCCC");
        mDataset.add("AAAA");
        mDataset.add("BBBBB");
        mDataset.add("CCCCCC");
        mDataset.add("AAAA");
        mDataset.add("BBBBB");
        mDataset.add("CCCCCC");
        mDataset.add("AAAA");
        mDataset.add("BBBBB");
        mDataset.add("CCCCCC");
        mDataset.add("AAAA");
        mDataset.add("BBBBB");
        mDataset.add("CCCCCC");

        mAdapter = new MyAdapter(mDataset.toArray(new String[0]));
        list.setAdapter(mAdapter);

    }


    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private String[] mDataset;

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView textView;
            public MyViewHolder(View v) {
                super(v);
                textView = (TextView)v.findViewById(R.id.txtRowText);
            }
        }

        public MyAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(mDataset[position]);

        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}