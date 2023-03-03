package com.zasa.superduper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.zasa.superduper.Adapters.Question_Adapter;
import com.zasa.superduper.Models.Question_Model;
import com.zasa.superduper.R;

import java.util.ArrayList;

public class ShowQuestionListActivity extends AppCompatActivity {

    RecyclerView rv_showList;
    ArrayList<Question_Model> questionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question_list);

        rv_showList = findViewById(R.id.showQuesData_rv);


        questionList = (ArrayList<Question_Model>) getIntent().getSerializableExtra("myList");

        Question_Adapter ques_adapter = new Question_Adapter(questionList, this,null);

        rv_showList.setAdapter(ques_adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_showList.setLayoutManager(layoutManager);

    }
}