package com.example.flashcards.data;

import android.provider.UserDictionary;

import java.util.ArrayList;
import java.util.List;

public class WordsRepository {

    private ArrayList<Word> mList = new ArrayList();

    private static WordsRepository sInstance = new WordsRepository();
    private WordsRepository() {}

    public static WordsRepository getInstance() {
        return sInstance;
    }

    private void loadData() {
        mList.add(new Word(1, "world", "世界"));
        mList.add(new Word(2, "space", "宇宙", true));
        mList.add(new Word(3, "moon", "月"));
        mList.add(new Word(4, "star", "星"));
        mList.add(new Word(5, "earth", "地球", true));
        mList.add(new Word(6, "mars", "火星", true));
        mList.add(new Word(7, "venus", "金星"));
        mList.add(new Word(8, "jupiter", "木星"));
        mList.add(new Word(9, "saturn", "土星"));
        mList.add(new Word(10, "air", "空気"));
        mList.add(new Word(11, "gravity", "重力"));
        mList.add(new Word(12, "light", "光"));
        mList.add(new Word(13, "rocket", "ロケット"));
        mList.add(new Word(14, "satellite", "衛星"));
    }

    public List<Word> getList() {
        if (mList.size() == 0) {
            loadData();
        }
        return mList;
    }

    public Word getById(int id) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i)._id == id) return mList.get(i);
        }
        return null;
    }
}
