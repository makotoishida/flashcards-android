package com.example.flashcards.data;

/*
* 単語データを表すクラス
*/
public class Word {
    public int _id;                     // 主キー
    public String english;              // 英語
    public String japanese;             // 日本語
    public boolean done = false;        // true=覚えた、false=忘れた

    public Word(int id, String eng, String jpn) {
        this(id, eng, jpn, false);
    }

    public Word(int id, String eng, String jpn, boolean is_done) {
        _id = id;
        english = eng;
        japanese = jpn;
        done = is_done;
    }

    public String getDoneString() {
        return this.done ? "✔" : "";
    }

}
