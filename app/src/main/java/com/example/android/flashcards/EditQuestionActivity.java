package com.example.android.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.android.flashcards.database.Question;

public class EditQuestionActivity extends AppCompatActivity {

    private EditText mQEditText;
    private EditText mAEditText;
    private CheckBox mResetAnswerCountCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        mQEditText = (EditText) findViewById(R.id.edit_q_et);
        mAEditText = (EditText) findViewById(R.id.edit_a_et);

        Question q =  (Question) getIntent().getSerializableExtra(getString(R.string.q_cross_activity_key));

        mQEditText.setText(q.getQuestion());
        mAEditText.setText(q.getAnswer());

    }
}