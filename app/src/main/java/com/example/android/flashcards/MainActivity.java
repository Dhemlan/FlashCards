package com.example.android.flashcards;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private GestureDetector mDetector;
    private View mAnswerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View questionTextView = findViewById(R.id.question_tv);
        mAnswerTextView = findViewById(R.id.answer_tv);

        questionTextView.setOnTouchListener(new View.OnTouchListener() {
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
            }
            else {
                Toast.makeText(getApplicationContext(), "Flung left!", Toast.LENGTH_SHORT).show();
            }
            mAnswerTextView.setVisibility(View.GONE);
            return true;
        }
    }
}