package com.example.android.flashcards.database;

import androidx.room.Entity;

@Entity(primaryKeys = {"qId", "categoryKey"}, tableName = "category")
public class QuestionCategory {
    private long qId;
    private int categoryKey;

    public QuestionCategory(long qId, int categoryKey){
        this.qId = qId;
        this.categoryKey = categoryKey;
    }
    public long getQId() {
        return qId;
    }

    public int getCategoryKey() {
        return categoryKey;
    }
}

