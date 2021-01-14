package com.example.flashcards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcards.data.Word;

import java.util.List;

// 一覧リスト表示用アダプタ
public class WordListViewAdapter extends RecyclerView.Adapter<WordListViewAdapter.ViewHolder> {
    private List<Word> mDataset;
    private OnClickListener listener;

    public WordListViewAdapter(List<Word> myDataset) {
        super();
        mDataset = myDataset;
    }

    public void updateDataset(List<Word> newDataset) {
        mDataset.clear();
        mDataset.addAll(newDataset);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word word = mDataset.get(position);
        holder.txtEng.setText(word.english);
        holder.txtDone.setText(word.getDoneString());

        holder.itemView.setOnClickListener(v -> listener.onClick(v, word));
    }

    @Override
    public long getItemId(int position) {
        return mDataset.get(position)._id;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // 各行内の要素への参照を保持しておくための入れ物となるクラス。
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEng;
        TextView txtDone;

        public ViewHolder(@NonNull View v) {
            super(v);
            txtEng = v.findViewById(R.id.txtRowText);
            txtDone = v.findViewById(R.id.txtDone);
        }
    }

    public interface OnClickListener {
        void onClick(View view, Word word);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

}
