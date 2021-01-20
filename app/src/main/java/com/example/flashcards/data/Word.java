package com.example.flashcards.data;

/*
* 単語データを表すクラス
*/
public class Word {
    public int _id;                     // 主キー
    public String english;              // 英語
    public String japanese;             // 日本語
    public boolean done = false;        // true=覚えた、false=忘れた
    public int sort_order = 0;          // 一覧表示用の並び順

    public Word(int id, String eng, String jpn) {
        this(id, eng, jpn, false, 0);
    }

    public Word(int id, String eng, String jpn, boolean isDone, int sortOrder) {
        _id = id;
        english = eng;
        japanese = jpn;
        done = isDone;
        sort_order = sortOrder;
    }

    public String getDoneString() {
        return this.done ? "✔" : "";
    }

}
