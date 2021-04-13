package com.conveter.trivia.data;

import com.conveter.trivia.model.Question;

import java.util.ArrayList;
import java.util.Queue;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> arrayList);
}
