package com.fedukova.guess;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends Activity implements GestureOverlayView.OnGesturePerformedListener {
    TextView tvInfo;
    EditText etInput;
    Button bControl;
    int guess;
    boolean gameFinished;
    SoundPool sp;
    private int mSoundId;
    int soundLessId;
    int soundMoreId;
    int soundOkId;
    int soundErrId;
    GestureLibrary gLib;
    GestureOverlayView gestures;
    GestureDetectorCompat GDCexample;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tvInfo = (TextView) findViewById(R.id.textView1);
        etInput = (EditText) findViewById(R.id.editText1);
        bControl = (Button) findViewById(R.id.buttonConfirm);
        guess = (int) (Math.random() * 100);
        gameFinished = false;
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        soundLessId = sp.load(this, R.raw.less, 1);
        soundMoreId = sp.load(this, R.raw.more, 1);
        soundOkId = sp.load(this, R.raw.ok, 1);
        soundErrId = sp.load(this, R.raw.err, 1);
        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLib.load()) {
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.gestureView);
        gestures.addOnGesturePerformedListener(this);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    public void onClick(View v) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float leftVolume = curVolume / maxVolume;
        float rightVolume = curVolume / maxVolume;
        int priority = 1;
        int no_loop = 0;
        float normal_playback_rate = 1f;
        if (!gameFinished) {
            if (etInput.getText().length() != 0) {
                int inp = Integer.parseInt(etInput.getText().toString());
                if (inp > guess) {
                    tvInfo.setText(getResources().getString(R.string.ahead));
                    mSoundId = sp.play(soundMoreId, leftVolume, rightVolume, priority, no_loop,
                            normal_playback_rate);

                }
                if (inp < guess) {
                    tvInfo.setText(getResources().getString(R.string.behind));
                    mSoundId = sp.play(soundLessId, leftVolume, rightVolume, priority, no_loop,
                            normal_playback_rate);
                }
                if (inp == guess) {
                    tvInfo.setText(getResources().getString(R.string.hit));
                    bControl.setText(getResources().getString(R.string.play_more));
                    gameFinished = true;
                    mSoundId = sp.play(soundOkId, leftVolume, rightVolume, priority, no_loop,
                            normal_playback_rate);
                }
            } else {
                tvInfo.setText(getResources().getString(R.string.error));
                mSoundId = sp.play(soundErrId, leftVolume, rightVolume, priority, no_loop,
                        normal_playback_rate);
            }
        } else {
            guess = (int) (Math.random() * 100);
            bControl.setText(getResources().getString(R.string.input_values));
            tvInfo.setText(getResources().getString(R.string.try_to_guess));
            gameFinished = false;
        }
        etInput.setText("");
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        //Создаёт ArrayList c загруженными из gestures жестами
        ArrayList<Prediction> predictions = gLib.recognize(gesture);
        if (predictions.size() > 0) {
            //если загружен хотябы один жест из gestures
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1.0) {
                if (prediction.name.equals("one"))
                    etInput.append("1");
                else if (prediction.name.equals("two"))
                    etInput.append("2");
                else if (prediction.name.equals("three"))
                    etInput.append("3");
                else if (prediction.name.equals("four"))
                    etInput.append("4");
                else if (prediction.name.equals("five"))
                    etInput.append("5");
                else if (prediction.name.equals("six"))
                    etInput.append("6");
                else if (prediction.name.equals("seven"))
                    etInput.append("7");
                else if (prediction.name.equals("eight"))
                    etInput.append("8");
                else if (prediction.name.equals("nine"))
                    etInput.append("9");
                else if (prediction.name.equals("zero"))
                    etInput.append("0");

            }
        }
    }
}