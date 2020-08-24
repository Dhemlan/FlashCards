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
import com.example.android.flashcards.database.QuestionCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText mQEditText;
    private EditText mAEditText;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        mDb = AppDatabase.getInstance(getApplicationContext());
        mQEditText = (EditText) findViewById(R.id.edit_q_et);
        mAEditText = (EditText) findViewById(R.id.edit_a_et);

        Button addButton = (Button) findViewById(R.id.add_bt);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question qToAdd = new Question(mQEditText.getText().toString(), mAEditText.getText().toString(), Arrays.asList(1));
                long qId = mDb.questionDao().insertQuestion(qToAdd);
                List<Integer> newCategories = new ArrayList<Integer>();
                for(int i = 1; i < Utils.CATEGORY_CHECKBOX_IDS.length; i++){
                    CheckBox cur = (CheckBox) findViewById(Utils.CATEGORY_CHECKBOX_IDS[i]);
                    if (cur.isChecked()) newCategories.add(i);
                }
                for (int cat : newCategories){
                    mDb.questionDao().insertQuestionCategory(new QuestionCategory(qId, cat));
                }
                Toast.makeText(AddQuestionActivity.this, "Question added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}