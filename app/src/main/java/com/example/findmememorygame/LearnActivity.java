package com.example.findmememorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LearnActivity extends AppCompatActivity {
    private List<Integer> buttonValues;
    private List<ImageButton> buttonList;
    private int backgroundImage = R.drawable.camera;
    private int numberTotalImagesTurnedUp = 0;
    private int numImagesUp = 0;
    private int lastClickedImageIndex = -1;
    private boolean isChecking = false;
    private MediaPlayer mediaPlayer;
    private long startTime;
    private long endTime;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        HashMap<String, Integer> soundMapping = new HashMap<>();
        HashMap<Integer, String> imageMapping = new HashMap<>();
        startTime = System.currentTimeMillis();



        buttonList = new ArrayList<>(Arrays.asList(
                findViewById(R.id.button), findViewById(R.id.button2),
                findViewById(R.id.button3), findViewById(R.id.button4),
                findViewById(R.id.button5), findViewById(R.id.button6),
                findViewById(R.id.button7), findViewById(R.id.button8),
                findViewById(R.id.button9), findViewById(R.id.button10),
                findViewById(R.id.button11), findViewById(R.id.button12)
        ));

//        buttonValues = new ArrayList<>(Arrays.asList(
//                R.drawable.cat, R.drawable.fish, R.drawable.penguin,
//                R.drawable.lion, R.drawable.parrot, R.drawable.squirrel,
//                R.drawable.cat, R.drawable.fish, R.drawable.penguin,
//                R.drawable.lion, R.drawable.parrot, R.drawable.squirrel
//        ));

        buttonValues = new ArrayList<>();
        int numberOfNeededImages = buttonList.size();
        int numberOfActualImages = 0;
        Field[] fields = R.drawable.class.getFields();
        Field[] soundFields = R.raw.class.getFields();
        for (Field soundField : soundFields){
            String resourceName = soundField.getName();
            int resourceId;
            try {
                resourceId = soundField.getInt(null);
                soundMapping.put(resourceName, resourceId);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Field imageField : fields){
            String resourceName = imageField.getName();
            int resourceId;
            try {
                resourceId = imageField.getInt(null);
                imageMapping.put(resourceId, resourceName);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Random rand = new Random();
        while(numberOfActualImages < numberOfNeededImages){
            try {
                int randInt = rand.nextInt(fields.length);
                int resourceId = fields[randInt].getInt(null);
                String imageName = fields[randInt].getName();
                if(!buttonValues.contains(resourceId)  && !imageName.equals("camera") && !imageName.equals("ic_launcher_background") && !imageName.equals("ic_launcher_foreground") && !imageName.equals("guess")){
                    buttonValues.add(resourceId);
                    buttonValues.add(resourceId);
                    numberOfActualImages= numberOfActualImages + 2;
                }

            }catch (Exception e){
                e.printStackTrace();
            }


        }

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
                        if (buttonValues.get(lastClickedImageIndex).equals(buttonValues.get(currentClickedImageIndex))) {
                            // Match
                            buttonList.get(lastClickedImageIndex).setTag(true);
                            button.setTag(true);
                            numImagesUp = 0;
                            isChecking = false;
                            numberTotalImagesTurnedUp += 2;

                            String imageName = imageMapping.get(buttonValues.get(currentClickedImageIndex));
                            Integer soundResourceId = soundMapping.get(imageName);

                            if (soundResourceId != null) {
                                // Play the corresponding sound
                                mediaPlayer = MediaPlayer.create(LearnActivity.this, soundResourceId);
                                // Set the volume to maximum
                                float maxVolume = 1.0f;
                                mediaPlayer.setVolume(maxVolume, maxVolume);

                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        // Release the MediaPlayer resources after sound playback is complete
                                        mediaPlayer.release();
                                    }
                                });
                                mediaPlayer.start();
                            }

                            //check if all the images are turned up = finished the game
                            if (numberTotalImagesTurnedUp == buttonList.size()){
                                endTime = System.currentTimeMillis();
                                long totalTimeInMillis = endTime - startTime;
                                // Convert milliseconds to seconds
                                int totalTimeInSeconds = (int) (totalTimeInMillis / 1000);

                                AlertDialog.Builder builder = new AlertDialog.Builder(LearnActivity.this);
                                builder.setMessage("You finished in: "+ totalTimeInSeconds + " seconds\n"+"Do you want to play again?");
                                builder.setTitle("Congratulations!");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(dialog, which) ->{
                                    Intent intent = new Intent(LearnActivity.this, LearnActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                                builder.setNegativeButton("No",(DialogInterface.OnClickListener)(dialog,which)->{
                                    Intent intent = new Intent(LearnActivity.this, StartupActivity.class);
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