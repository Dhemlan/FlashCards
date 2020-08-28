package com.example.android.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.flashcards.database.AppDatabase;
import com.example.android.flashcards.database.Question;
import com.example.android.flashcards.database.QuestionCategory;

import java.util.ArrayList;
import java.util.List;

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

        mQEditText.setText(q.getQuestion());
        mAEditText.setText(q.getAnswer());

        final List<Integer> categories = new ArrayList<Integer>();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                 categories.addAll(mDb.questionDao().loadQuestionsCategories(q.getId()));

                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         for (int cat : categories){
                             CheckBox selectedCb = (CheckBox) findViewById(Utils.CATEGORY_CHECKBOX_IDS[cat]);
                             selectedCb.setChecked(true);
                         }
                     }
                 });
            }
        });

        Button editButton = (Button) findViewById(R.id.edit_bt);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int answeredCount = mResetAnswerCountCheckBox.isChecked() ? 0 : q.getAnsweredCount();
                final int qId = q.getId();
                final Question editedQ = new Question(qId,mQEditText.getText().toString(), mAEditText.getText().toString(), answeredCount);

                final List<Integer> newCategories = new ArrayList<Integer>();
                for(int i = 1; i < Utils.CATEGORY_CHECKBOX_IDS.length; i++){
                    CheckBox cur = (CheckBox) findViewById(Utils.CATEGORY_CHECKBOX_IDS[i]);
                    if (cur.isChecked()) newCategories.add(i);
                }
                final List<Integer> toAdd = new ArrayList<Integer>(newCategories);
                toAdd.removeAll(categories);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.questionDao().updateQuestion(editedQ);
                        for (int cat : toAdd){
                            mDb.questionDao().insertQuestionCategory(new QuestionCategory(qId, cat));
                        }
                        categories.removeAll(newCategories);
                        for(int cat : categories){
                            mDb.questionDao().deleteQuestionCategory(new QuestionCategory(qId, cat));
                        }
                    }
                });

                Toast.makeText(EditQuestionActivity.this, "Question edited!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });




    }
}