package com.example.android.flashcards;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.flashcards.database.AppDatabase;
import com.example.android.flashcards.database.Question;

import java.util.ArrayDeque;
import java.util.Queue;


public class MainActivity extends AppCompatActivity {
    private GestureDetector mDetector;
    private TextView mAnswerTextView;
    private TextView mQuestionTextView;
    private AppDatabase mDb;
    private Queue<Question> mQuestions = new ArrayDeque<>();
    private Question mCurQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionTextView = (TextView) findViewById(R.id.question_tv);
        mAnswerTextView = (TextView) findViewById(R.id.answer_tv);

        mDb = AppDatabase.getInstance(getApplicationContext());
        mQuestions.addAll(mDb.questionDao().loadAllQuestions());
        nextQuestion();


        mQuestionTextView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mAnswerTextView.setVisibility(View.VISIBLE);
                return true;
            }
        });
        
        mDetector = new GestureDetector(this, new FlingListener());

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        };
        mAnswerTextView.setOnTouchListener(touchListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class FlingListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG","onDown: ");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event1.getX() < event2.getX()) {
                Toast.makeText(getApplicationContext(), "Flung right!", Toast.LENGTH_SHORT).show();
                mCurQuestion.incrementAnswerCount();
                mDb.questionDao().updateQuestion(mCurQuestion);
            }
            else {
                Toast.makeText(getApplicationContext(), "Flung left!", Toast.LENGTH_SHORT).show();
                mQuestions.add(mCurQuestion);
            }
            mAnswerTextView.setVisibility(View.GONE);
            nextQuestion();
            return true;
        }
    }

    public void nextQuestion(){
        if (mQuestions.peek() != null) {
            mCurQuestion = mQuestions.remove();
            setQuestionToDisplay(mCurQuestion);
        }
        else noMoreQuestions();
    }

    public void setQuestionToDisplay(Question q) {
        mQuestionTextView.setText("Q: " + q.getQuestion() + "(Answered " + q.getAnsweredCount() + " times)");
        mAnswerTextView.setText("A: " + q.getAnswer());
    }

    public void noMoreQuestions(){
        mQuestionTextView.setVisibility(View.GONE);
        mAnswerTextView.setVisibility(View.GONE);
        findViewById(R.id.no_questions_tv).setVisibility(View.VISIBLE);
    }
}