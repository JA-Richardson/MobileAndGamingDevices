package tees.ac.uk.w9383619.mobileandgamedevicesica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new Game(this));
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        Log.v("MainActivity", "Click");
        setContentView(new Game(this));
    }


    public void onClick2(View view) {
        Log.v("How to play", "Click");
        setContentView(R.layout.activity_how_to_play);
    }
}