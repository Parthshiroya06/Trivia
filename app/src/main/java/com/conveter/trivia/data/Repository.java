package com.conveter.trivia.data;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.conveter.trivia.controller.AppController;
import com.conveter.trivia.model.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    ArrayList<Question> arrayList=new ArrayList<>();

    public List<Question> getQuestion(final AnswerListAsyncResponse callback)
    {
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i <response.length() ; i++) {
                try {
                    Question question=new Question(response.getJSONArray(i).get(0).toString(),response.getJSONArray(i).getBoolean(1));
                    arrayList.add(question);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
             if (null != callback)callback.processFinished(arrayList);
        }, error -> {

        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return arrayList                ;
    }
}
