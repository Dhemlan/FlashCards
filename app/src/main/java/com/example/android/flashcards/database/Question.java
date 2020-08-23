package com.example.android.flashcards.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class Question implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String question;
    private String answer;
    @Ignore
    private List<Integer> categories;
    private int answeredCount;

    @Ignore
    public Question (String question, String answer, List<Integer> categories){
        this.question = question;
        this.answer = answer;
        this.categories = categories;
        this.answeredCount = 0;
    }
    public Question (int id, String question, String answer, int answeredCount){
        this.id = id;
        this.question = question;
        this.answer = answer;
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

    public List<Integer> getCategories() {
        return categories;
    }

    public int getAnsweredCount() {
        return answeredCount;
    }

}


