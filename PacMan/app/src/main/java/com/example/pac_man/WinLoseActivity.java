package com.example.pac_man;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WinLoseActivity extends Activity implements View.OnClickListener {
    int levelRessourceId;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_lose);

        fillViewWithData();
    }

    private void fillViewWithData() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Boolean won = extras.getBoolean("win");
            String score = String.valueOf(extras.getInt("score"));
            levelRessourceId = extras.getInt("levelRessourceId");

            if(!won) {
                TextView winState = findViewById(R.id.t_win);
                winState.setText(getString(R.string.t_lose));

                mp=MediaPlayer.create(getApplicationContext(),R.raw.game_over);
                mp.setLooping(true);
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            } else {
                mp=MediaPlayer.create(getApplicationContext(),R.raw.level_win);
                mp.setLooping(true);
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            }

            TextView scoreValue = findViewById(R.id.t_scoreValue);
            scoreValue.setText(score);

            addScoreToHighScores(score);
        }
    }

    private void addScoreToHighScores(String score) {
        SharedPreferences sharedPref = getSharedPreferences("highscores" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String highscores = sharedPref.getString("highscores", "");
        if (highscores != null) {
            editor.putString("highscores", highscores.concat(score + ","));
        }
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_retry:
                Intent retryIntent = new Intent(this, GameActivity.class);
                retryIntent.putExtra("levelRessourceId", levelRessourceId);
                startActivity(retryIntent);
                mp.stop();
                break;
            case R.id.b_menu:
                Intent mainMenuIntent = new Intent(this, MainActivity.class);
                startActivity(mainMenuIntent);
                mp.stop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if ((mp != null) && (mp.isPlaying()))
        {
            mp.pause();
        }
    }
}