package com.conveter.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.icu.text.RelativeDateTimeFormatter;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.conveter.trivia.data.Repository;
import com.conveter.trivia.databinding.ActivityMainBinding;
import com.conveter.trivia.model.Question;
import com.conveter.trivia.model.Score;
import com.conveter.trivia.util.Prefs;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private  int currentQuestionIndex=0;
    private int scoreCount=0;
    Score score;
    Prefs prefs;
List<Question> questionList;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score=new Score();
        prefs=new Prefs(MainActivity.this);
        currentQuestionIndex=prefs.getState();
        Log.d("prefs","on Create:"+prefs.getHighestScore());
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main );
        binding.heistscore.setText(MessageFormat.format("Highest:{0}", String.valueOf(prefs.getHighestScore())));
        binding.textscore.setText(MessageFormat.format("Score:{0}", String.valueOf(score.getScore())));
       questionList= new Repository().getQuestion(arrayList ->{
                   binding.questionText.setText(arrayList.get(currentQuestionIndex).getAnswer());
           updateCounter(questionList);
       });

        binding.buttonNext.setOnClickListener(v -> {
            getNextQuestion();


        });
        binding.buttonTrue.setOnClickListener(v -> {
            checkAns(true);
            updateQuestion();

        });
        binding.buttonFalse.setOnClickListener(v -> {
            checkAns(false);
            updateQuestion();
        });

    }

    private void getNextQuestion() {
        currentQuestionIndex=(currentQuestionIndex+1) % questionList.size();
        updateQuestion();
    }

    private void checkAns(boolean userChoseCorrect) {
        boolean answer=questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMassage=0;
        if(userChoseCorrect==answer){
            snackMassage=R.string.correct_ans;
            fadeAnimation();
            addPoint();
        }
        else {
            snackMassage=R.string.incorrect_ans;
            shackAnimation();
            deductPoint();
        }
        Snackbar.make(binding.cardView,snackMassage,Snackbar.LENGTH_SHORT).show();
    }

    private void updateCounter(List<Question> questionList) {
        binding.textViewOut.setText(String.format(getString(R.string.texr_formated), currentQuestionIndex, this.questionList.size()));


    }

    public void updateQuestion() {
        String question=questionList.get(currentQuestionIndex).getAnswer();
        binding.questionText.setText(question);
        updateCounter(questionList);
    }
    public void shackAnimation(){
        Animation Shack= AnimationUtils.loadAnimation(MainActivity.this,R.anim.shack_animation);
        binding.cardView.setAnimation(Shack);
        Shack.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionText.setTextColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionText.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
  public void fadeAnimation(){
      AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
      alphaAnimation.setDuration(300);
      alphaAnimation.setRepeatCount(1);
      alphaAnimation.setRepeatMode(Animation.REVERSE);
      binding.cardView.setAnimation(alphaAnimation);
      alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
          @Override
          public void onAnimationStart(Animation animation) {
              binding.questionText.setTextColor(Color.GREEN);
          }

          @Override
          public void onAnimationEnd(Animation animation) {
              binding.questionText.setTextColor(Color.WHITE);
              getNextQuestion();
          }

          @Override
          public void onAnimationRepeat(Animation animation) {

          }
      });
    }

    private void deductPoint(){

        if(scoreCount>0){
            scoreCount-=100;
            score.setScore(scoreCount);
            binding.textscore.setText(MessageFormat.format("Score:{0}", String.valueOf(score.getScore())));
        }
        else{
            scoreCount=0;
            score.setScore(scoreCount);
            binding.textscore.setText(MessageFormat.format("Score:{0}", String.valueOf(score.getScore())));
        }
    }
    private void addPoint()
{
    scoreCount+=100;
    score.setScore(scoreCount);
    binding.textscore.setText(MessageFormat.format("Score:{0}", String.valueOf(score.getScore())));

}

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        prefs.setState(currentQuestionIndex);

        super.onPause();
    }
}