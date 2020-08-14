package com.example.android.flashcards.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String question;
    private String answer;
    private int category;
    private int answeredCount;

    @Ignore
    public Question (String question, String answer, int category, int answeredCount){
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.answeredCount = answeredCount;
    }
    public Question (int id, String question, String answer, int category, int answeredCount){
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.answeredCount = answeredCount;
    }

    public void incrementAnswerCount(){
        answeredCount++;
    }

    public int getId(){
        return id;
    }

    public String getQuestion(){
        return question;
    }

    public String getAnswer(){
        return answer;
    }

    public int getCategory() {
        return category;
    }

    public int getAnsweredCount() {
        return answeredCount;
    }

}
