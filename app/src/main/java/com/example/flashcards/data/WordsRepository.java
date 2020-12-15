package com.example.flashcards.data;

import android.content.Context;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class WordsRepository {

    private ArrayList<Word> mList = null;

    private static WordsRepository sInstance = new WordsRepository();

    private WordsRepository() {}

    public static WordsRepository getInstance() {
        if (sInstance == null) {
            sInstance = new WordsRepository();
        }
        return sInstance;
    }

    private void loadData() {
        if (mList == null) mList = new ArrayList();
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
        if (mList == null) loadData();
        return mList;
    }

    public Word getById(int id) {
        if (id == 0){
            return new Word(0, "", "");
        }

        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i)._id == id) return mList.get(i);
        }

        return null;
    }

    public void updateDone(int id, boolean done) throws InvalidKeyException {
        Word word = getById(id);
        if (word == null) {
            throw new InvalidKeyException("");
        }

        word.done = done;
    }

    private int getMaxId() {
        int max = Integer.MIN_VALUE;
        for (Word i: mList) {
            if (i._id > max) max = i._id;
        }
        return max;
    }

    public void save(Word word) throws InvalidKeyException {
        if (word._id == 0) {
            word._id = getMaxId() + 1;
            mList.add(word);
            return;
        }

        Word existing = getById(word._id);
        if (existing == null) {
            throw new InvalidKeyException("");
        }
        existing.english = word.english;
        existing.japanese = word.japanese;
        existing.done = word.done;
    }

    public void delete(int id) throws Exception {
        Word existing = getById(id);
        if (existing == null) return;
        mList.remove(existing);
    }

}
