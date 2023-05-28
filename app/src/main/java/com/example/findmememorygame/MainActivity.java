package com.example.findmememorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toolbar;
import com.example.findmememorygame.R.drawable.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<Integer> buttonValues;
    private List<ImageButton> buttonList;
    private int backgroundImage = R.drawable.camera;
    private int numImagesUp = 0;
    private int numberTotalImagesTurnedUp = 0;
    private int lastClickedImageIndex = -1;
    private boolean isChecking = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonList = new ArrayList<>(Arrays.asList(
                findViewById(R.id.button), findViewById(R.id.button2),
                findViewById(R.id.button3), findViewById(R.id.button4),
                findViewById(R.id.button5), findViewById(R.id.button6),
                findViewById(R.id.button7), findViewById(R.id.button8),
                findViewById(R.id.button9), findViewById(R.id.button10),
                findViewById(R.id.button11), findViewById(R.id.button12)
        ));

        buttonValues = new ArrayList<>(Arrays.asList(
                R.drawable.cat, R.drawable.fish, R.drawable.penguin,
                R.drawable.lion, R.drawable.parrot, R.drawable.squirrel,
                R.drawable.cat, R.drawable.fish, R.drawable.penguin,
                R.drawable.lion, R.drawable.parrot, R.drawable.squirrel
        ));
        Collections.shuffle(buttonValues, new Random());




        for (int i = 0; i < buttonList.size(); i++) {
            //buttonList.get(i).setBackgroundResource(R.drawable.camera); // set the default background
            final int index = i;
            buttonList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageButton button = (ImageButton) view;
                    // Check if already checking for match
                    if (isChecking) {
                        return;
                    }

                    // Check if this image is already up
                    if (button.getTag() != null && (boolean)button.getTag()) {
                        return;
                    }

                    //Flip image up
                    button.setBackgroundResource(buttonValues.get(index)); // set the animal image as the background
                    button.setTag(true);
                    numImagesUp++;

                    //Check for a match
                    if(numImagesUp == 2){
                        isChecking = true;
                        //int currentClickedImageValue = buttonValues.get(index);
                        //if( currentClickedImageValue == lastClickedImageValue){
                        // match
                        final int currentClickedImageIndex = index;
                        if (buttonValues.get(lastClickedImageIndex) == buttonValues.get(currentClickedImageIndex)) {
                            // Match
                            buttonList.get(lastClickedImageIndex).setTag(true);
                            button.setTag(true);
                            numImagesUp = 0;
                            numberTotalImagesTurnedUp += 2;
                            isChecking = false;

                            //check if all the images are turned up = finished the game
                            if (numberTotalImagesTurnedUp == buttonList.size()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Do you want to play again?");
                                builder.setTitle("Congratulations!");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog,which) ->{
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                                builder.setNegativeButton("No",(DialogInterface.OnClickListener)(dialog,which)->{
                                    Intent intent = new Intent(MainActivity.this, StartupActivity.class);
                                    startActivity(intent);
                                    finish();
                                    dialog.cancel();
                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                            }

                        }else {
                            //not match , flip images back
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    button.setBackgroundResource(backgroundImage);
                                    buttonList.get(lastClickedImageIndex).setBackgroundResource(backgroundImage);
                                    button.setTag(false);
                                    buttonList.get(lastClickedImageIndex).setTag(false);
                                    numImagesUp = 0;
                                    isChecking = false;
                                }
                            },1000);

                        }
                    }else {
                        lastClickedImageIndex = buttonList.indexOf(button);
                    }
                }
            });
        }


    }
}