package com.example.flashcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.flashcards.data.Word;

// 一覧リスト表示用アダプタ
public class WordListViewAdapter extends ArrayAdapter<Word> {
    private LayoutInflater inflater;
    private int itemLayout;

    public WordListViewAdapter(@NonNull Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayout = layoutResourceId;
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position)._id;
    }

    // 各行内の要素への参照を保持しておくための入れ物となるクラス。
    class ViewHolder {
        TextView txtEng;
        TextView txtDone;
    }

    @Override
    public @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // 行のViewが新規に生成された場合はtxtEng, txtDoneへの参照をViewHolderに入れておく。
            convertView = inflater.inflate(itemLayout, parent, false);
            holder = new ViewHolder();
            holder.txtEng = convertView.findViewById(R.id.txtRowText);
            holder.txtDone = convertView.findViewById(R.id.txtDone);
            convertView.setTag(holder);
        } else {
            // 行のViewが生成済みの場合はViewHolderを取得する。
            holder = (ViewHolder) convertView.getTag();
        }

        // 現在の行に英語とDoneフラグの値を表示する。
        Word word = getItem(position);
        if(word != null){
            holder.txtEng.setText(word.english);
            holder.txtDone.setText(word.getDoneString());
        }
        return convertView;
    }
}
