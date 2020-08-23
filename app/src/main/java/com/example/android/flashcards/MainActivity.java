package com.example.android.flashcards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.android.flashcards.database.AppDatabase;
import com.example.android.flashcards.database.Question;
import com.example.android.flashcards.database.QuestionCategory;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


import static com.example.android.flashcards.Utils.bulkAddQuestions;


public class MainActivity extends AppCompatActivity {
    private GestureDetector mDetector;
    private TextView mAnswerTextView;
    private TextView mQuestionTextView;
    private TextView mMessageTextView;
    private AppDatabase mDb;
    private Queue<Question> mQuestions;
    private Question mCurQuestion;
    private Button mMoreQuestionsButton;

    private static final int MIN_TEXT_SIZE = 12;
    private static final int MAX_TEXT_SIZE = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionTextView = (TextView) findViewById(R.id.question_tv);
        mAnswerTextView = (TextView) findViewById(R.id.answer_tv);
        mMessageTextView = (TextView) findViewById(R.id.messages_tv);
        mMoreQuestionsButton = (Button) findViewById(R.id.load_more_qs_bt);

        mDb = AppDatabase.getInstance(getApplicationContext());
        fetchQuestions();

        mQuestionTextView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mAnswerTextView.setVisibility(View.VISIBLE);
                mQuestionTextView.setTextSize(MIN_TEXT_SIZE);
                return true;
            }
        });

        mMoreQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchQuestions();
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
    protected void onResume() {
        fetchQuestions();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_import_questions){
            bulkAddQuestions(mDb);
            fetchQuestions();
        }
        else if (item.getItemId() == R.id.menu_change_pool){
            Intent intent = new Intent(this, ChangeQuestionPoolActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.menu_edit_question){
            if (mCurQuestion != null) {
                Intent intent = new Intent(this, EditQuestionActivity.class);
                intent.putExtra(getString(R.string.q_cross_activity_key), mCurQuestion);
                startActivity(intent);
            }
            else Toast.makeText(this, "Load a question first!", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.menu_clear_db){
            mDb.clearAllTables();
            loadQuestion();
        }
        return true;
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
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.questionDao().updateQuestion(mCurQuestion);
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Flung left!", Toast.LENGTH_SHORT).show();
            }
            mAnswerTextView.setVisibility(View.GONE);
            mQuestionTextView.setTextSize(MAX_TEXT_SIZE);
            mQuestions.remove();
            loadQuestion();
            return true;
        }
    }

    public void fetchQuestions(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean revisionMode = sharedPrefs.getBoolean(getString(R.string.revision_cb_key), false);
        int category = Integer.parseInt(sharedPrefs.getString(getString(R.string.category_lp_key), "0"));
        int revisionThreshold = sharedPrefs.getInt(getString(R.string.revision_sb_key), R.integer.default_revision_threshold);

        mQuestions = new ArrayDeque<>();
        if (revisionMode) {
            //if (category != Utils.CATEGORY_ALL) mQuestions.addAll(mDb.questionDao().loadSpecificCategory(category, revisionThreshold));
             mQuestions.addAll(mDb.questionDao().loadRevisionQuestions(revisionThreshold));
        }
        else{
            if (category != Utils.CATEGORY_ALL) mQuestions.addAll(mDb.questionDao().loadSpecificCategory(category, revisionThreshold));
            else mQuestions.addAll(mDb.questionDao().loadLearningQuestions(revisionThreshold));
        }
        loadQuestion();
    }

    public void loadQuestion(){
        mCurQuestion = mQuestions.peek();
        if (mCurQuestion != null) {
            setQuestionToDisplay(mCurQuestion);
            mQuestionTextView.setVisibility(View.VISIBLE);
            mMessageTextView.setVisibility(View.GONE);
            mMoreQuestionsButton.setVisibility(View.GONE);
        }
        else {
            mQuestionTextView.setVisibility(View.GONE);
            mAnswerTextView.setVisibility(View.GONE);
            mMessageTextView.setVisibility(View.VISIBLE);
            mMoreQuestionsButton.setVisibility(View.VISIBLE);
        };
    }

    public void setQuestionToDisplay(Question q) {
        mQuestionTextView.setText(q.getQuestion() + " (Answered " + q.getAnsweredCount() + " times)");
        mAnswerTextView.setText(q.getAnswer());
    }
}