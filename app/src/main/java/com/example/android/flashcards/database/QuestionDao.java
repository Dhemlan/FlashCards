package com.example.android.flashcards.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM question")
    List<Question> loadAllQuestions();

    @Insert
    void insertQuestion(Question question);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateQuestion(Question question);

    @Delete
    void deleteQuestion(Question question);
}
