package com.example.android.flashcards;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.android.flashcards.database.AppDatabase;
import com.example.android.flashcards.database.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Utils {
    private static int NUMBER_OF_QUESTION_COMPONENTS = 3;

    public static final void addFromFileToDb(Context context, AppDatabase db){
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.questions);
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));

            ArrayList<String> input = new ArrayList<String>();
            String line = bufferedReader.readLine();
            int lineCount = 0;
            while (line != null) {
                input.add(line);
                lineCount++;
                if (lineCount % NUMBER_OF_QUESTION_COMPONENTS == 0) {
                    Question question = new Question(input.get(0), input.get(1), Integer.parseInt(input.get(2)));
                    db.questionDao().insertQuestion(question);
                    input.clear();
                    lineCount = 0;
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            inputStream.close();
        }
        catch (Exception e){
            Log.e("TAG","File not found");
        }

    }

    private static final int CATEGORY_BIG_O = 1;
    private static final int CATEGORY_SORTING = 2;
}
