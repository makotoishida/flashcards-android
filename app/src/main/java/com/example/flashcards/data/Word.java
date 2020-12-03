package com.example.flashcards.data;

public class Word {
    public int _id;
    public String english;
    public String japanese;
    public Boolean done = false;

    Word(int id, String eng, String jpn) {
        _id = id;
        english = eng;
        japanese = jpn;
    }

}
