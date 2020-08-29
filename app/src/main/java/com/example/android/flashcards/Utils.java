package com.example.android.flashcards;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.android.flashcards.database.AppDatabase;
import com.example.android.flashcards.database.Question;
import com.example.android.flashcards.database.QuestionCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Utils {

    static List<Question> questionsToAdd = new ArrayList<>();

    public static List<Integer> getSelectedCategoryCheckBoxes(View view) {
        List<Integer> checkedBoxes = new ArrayList<Integer>();
        for (int i = 1; i < CATEGORY_CHECKBOX_IDS.length; i++) {
            CheckBox cur = (CheckBox) view.findViewById(CATEGORY_CHECKBOX_IDS[i]);
            if (cur.isChecked()) checkedBoxes.add(i);
        }
        return checkedBoxes;
    }

    public static final void bulkAddQuestions(final AppDatabase db){
        populateQuestions();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (Question q : questionsToAdd) {
                    long id = db.questionDao().insertQuestion(q);
                    for (int category : q.getCategories()) {
                        db.questionDao().insertQuestionCategory(new QuestionCategory(id, category));
                    }
                }
            }
        });
    }

    private static void populateQuestions(){
        questionsToAdd.add(new Question("Describe the 3 asymptotic notation forms",
                "Big theta - bounds growth rate above and below.\nBig O - bounds growth rate above.\nBig Omega - bounds growth rate below.",
                Arrays.asList(1)));
        questionsToAdd.add(new Question("Define stability with regard to sorting algorithms", "Equal sorted elements retain the same relative position as the input\n",
                Arrays.asList(2)));
        questionsToAdd.add(new Question("What makes a sorting algorithm adaptive?","Changes in the time complexity based on the order of input elements (e.g. bubble sort is faster with an already sorted input)",
                Arrays.asList(1,2)));
        questionsToAdd.add(new Question("How does selection sort work? Is it stable? Is it adaptive?", "Scans all elements to find the minimum, then swaps that element into the appropriate position. Repeat. It is unstable and non-adaptive.",
                Arrays.asList(2)));
        questionsToAdd.add(new Question("How does bubble sort work? Is it stable? Is it adaptive?","Multiple passes are made from the end, with any element less than an adjacent element swapped. Multiple elements may be swapped and single element may be swapped multiple times in a single pass. Algorithm terminates once a pass completes with no swaps",
                Arrays.asList(2)));
        questionsToAdd.add(new Question("How does insertion sort work? Is it stable? Is it adaptive?", "The low end is the working sorted section. Each element is compared starting at the end of the sorted section then looping backwards, with greater elements shuffled up until the appropriate place is found",
                Arrays.asList(2)));
        questionsToAdd.add(new Question("What are the average, best, worst and space complexities of selection sort?","Worst O(n^2), Average Theta((n^2), Best Omega(n^2), Space = O(1)",
                Arrays.asList(1,2)));
        questionsToAdd.add(new Question("What are the average, best, worst and space complexities of insertion sort?", "Worst O(n^2)\n Average Theta((n^2)\n Best Omega(n)\n Space = O(1)",
                Arrays.asList(1,2)));
        questionsToAdd.add(new Question("What are the average, best, worst and space complexities of bubble sort?","Worst O(n^2), Average Theta((n^2), Best Omega(n), Space = O(1)",
                Arrays.asList(1,2)));
        questionsToAdd.add(new Question("Match the pairs - {Theta, O, Omega} {best, worst, average}", "Theta -> Average, O -> Worst, Omega -> Best",
                Arrays.asList(1)));
        questionsToAdd.add(new Question("When might bubble sort be non-adaptive?", "If the algorithm doesn't terminate on a full pass without any swaps",
                Arrays.asList(2)));
        questionsToAdd.add(new Question("When might bubble sort be unstable?", "If the low value is compared to the high value with <= (rather than <)",
                Arrays.asList(2)));
        questionsToAdd.add(new Question("What are the 3 major constructs of a database", "Attributes, entities and relationships",
                Arrays.asList(DATABASES_CATEGORY)));
        questionsToAdd.add(new Question("Describe database key hierarchy", "A [super] key is an attribute group that is unique over an entity set\nA candidate key is a minimal key\nA primary key is a candidate key selected by the database designer",
                Arrays.asList(DATABASES_CATEGORY)));
        questionsToAdd.add(new Question("Describe this line of code:/n int x = a > b ? a : b", "The ? represents the conditional (a.k.a ternary) operator\nIt will return a or b depending on the value of the a > b boolean expression",
                Arrays.asList(JAVA_CATEGORY)));
        questionsToAdd.add(new Question("Compare the Entity Relationship (ER) and Relational Data models", "ER utilises diagrams to describe relationships between entities and the associated attributes of both\nThe relational data model creates a collection of inter-connected tables (relations)",
                Arrays.asList(DATABASES_CATEGORY)));
        questionsToAdd.add(new Question("Describe a relation schema. What is an tuple of a relation schema? An instance?", "A relation/table (denoted R,S,T...) has a name and a set of attributes (i.e the structure of the table)\nA tuple is a list of the related values for each attribute (i.e. an entry).\nAn instance is a set of tuples.",
                Arrays.asList(DATABASES_CATEGORY)));
        questionsToAdd.add(new Question("What are the 4 types of database integrity constraints?", "Domain (constraint on allowed attribute values)\nKey (uniqueness)\nEntity (no null attribute values)\nReferential (references between relations)",
                Arrays.asList(DATABASES_CATEGORY)));
        questionsToAdd.add(new Question("Describe a N:M mapping in a relational data model", "3 tables (2 entities + the relationship) with the relationship table containing foreign keys to the entity tables and any attributes of the relationship",
                Arrays.asList(DATABASES_CATEGORY)));
        questionsToAdd.add(new Question("Describe a 1:N/1:1 mapping in a relational data model", "2 tables with the relationship (foreign key + any attributes) incorporated into one of the entities" ,
                Arrays.asList(DATABASES_CATEGORY)));

    }

        /**
        questionsToAdd.add(new Question("", "",
                Arrays.asList()));
        **/

    public static final int All_CATEGORY = 0;
    public static final int BIG_O_CATEGORY = 1;
    public static final int SORTING_CATEGORY = 2;
    public static final int DATABASES_CATEGORY = 3;
    public static final int JAVA_CATEGORY = 4;

    // Add to array below, strings, add/edit xml, shared prefs key/value strings

    public static final int[] CATEGORY_CHECKBOX_IDS = {0, R.id.categories_cb1, R.id.categories_cb2, R.id.categories_cb3, R.id.categories_cb4};



}
