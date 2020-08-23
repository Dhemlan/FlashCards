package com.example.android.flashcards.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM question WHERE answeredCount < :threshold ORDER BY RANDOM()")
    List<Question> loadLearningQuestions(int threshold);

    @Query("SELECT question, answer, answeredCount, id FROM question JOIN category ON id=qid WHERE answeredCount <= :threshold AND categoryKey= :cat GROUP BY id ")
    List<Question> loadSpecificCategory(int cat, int threshold);

    @Query("SELECT * from question WHERE answeredCount >= :threshold ORDER BY RANDOM()")
    List<Question> loadRevisionQuestions(int threshold);

    @Query("SELECT question, answer, answeredCount, id FROM question JOIN category ON id=qid WHERE answeredCount > :threshold AND categoryKey= :cat GROUP BY id ")
    List<Question> loadSpecificRevisionCategory(int cat, int threshold);

    @Insert
    long insertQuestion(Question question);

    @Insert
    void insertQuestionCategory(QuestionCategory category);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateQuestion(Question question);

    @Delete
    void deleteQuestion(Question question);
}
