package com.example.android.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.flashcards.database.AppDatabase;
import com.example.android.flashcards.database.Question;

public class EditQuestionActivity extends AppCompatActivity {

    private EditText mQEditText;
    private EditText mAEditText;
    private CheckBox mResetAnswerCountCheckBox;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        mDb = AppDatabase.getInstance(getApplicationContext());
        mQEditText = (EditText) findViewById(R.id.edit_q_et);
        mAEditText = (EditText) findViewById(R.id.edit_a_et);
        mResetAnswerCountCheckBox = (CheckBox) findViewById(R.id.revision_reset_cb);

        final Question q =  (Question) getIntent().getSerializableExtra(getString(R.string.q_cross_activity_key));

        Button editButton = (Button) findViewById(R.id.edit_bt);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int answeredCount = mResetAnswerCountCheckBox.isChecked() ? 0 : q.getAnsweredCount();
                Question editedQ = new Question(q.getId(),mQEditText.getText().toString(), mAEditText.getText().toString(), answeredCount);
                mDb.questionDao().updateQuestion(editedQ);
                Toast.makeText(EditQuestionActivity.this, "Question edited!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mQEditText.setText(q.getQuestion());
        mAEditText.setText(q.getAnswer());

    }
}