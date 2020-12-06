package com.example.flashcards.data;

public class Word {
    public int _id;
    public String english;
    public String japanese;
    public Boolean done = false;

    public Word(int id, String eng, String jpn) {
        this(id, eng, jpn, false);
    }

    public Word(int id, String eng, String jpn, Boolean is_done) {
        _id = id;
        english = eng;
        japanese = jpn;
        done = is_done;
    }

    public String getDoneString() {
        return this.done ? "✔" : "";
    }

}
