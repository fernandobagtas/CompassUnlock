package com.example.compassunlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // define the display assembly compass picture
    public ImageView image1;

    // define the house picture
    public ImageView image2;

    // record the compass picture angle turned
    public float currentDegree = 0f;

    public float houseDegree = -320f;

    public boolean degreeCheck;

    // device sensor manager
    private SensorManager mSensorManager;

    //define the heading of the direction
    TextView tvHeading;

    //define the other textview
    TextView saying;

    //define the button
    Button button;

    RelativeLayout uLayout;
    RelativeLayout dLayout;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set images
        image1 = (ImageView) findViewById(R.id.imageViewCompass);
        image2 = (ImageView) findViewById(R.id.imageView2);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);

        saying = (TextView) findViewById(R.id.textView);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // set button
        button = (Button) findViewById(R.id.button);

        // set layouts
        uLayout = (RelativeLayout) findViewById(R.id.unlockLayout);
        dLayout = (RelativeLayout) findViewById(R.id.denyLayout);

        //set the screen switcher
        intent = new Intent(this, HomeScreen.class);

        //set the screen to fullscreen
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility
            (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        //tvHeading.setText("Heading: " + Float.toString(degree));
        tvHeading.setText(Float.toString(degree) + "\u00B0");
        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image1.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    /*Function for the button. Edit only this for the screens
    * currentDegree = the degree that is being shown on the screen
    * degreeCheck = boolean check if they got it or not
    */
    public void checkDirection(View view){
        if(currentDegree < (houseDegree + 10) && currentDegree > (houseDegree - 10)){
            degreeCheck = true;

            saying.setVisibility(TextView.GONE);
            image2.setVisibility(ImageView.GONE);
            button.setVisibility(Button.GONE);
            uLayout.setVisibility(RelativeLayout.VISIBLE);
            new CountDownTimer(2000, 1000){
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    //saying.setVisibility(TextView.VISIBLE);
                    //image2.setVisibility(ImageView.VISIBLE);
                    //button.setVisibility(Button.VISIBLE);
                    //uLayout.setVisibility(RelativeLayout.INVISIBLE);

                    startActivity(intent);
                    //System.exit(0);
                }
            }.start();


        }
        else{
            degreeCheck = false;
            Log.d("HEADING", Float.toString(currentDegree));
            Log.d("HEADING", Float.toString(houseDegree));

            saying.setVisibility(TextView.GONE);
            image2.setVisibility(ImageView.GONE);
            button.setVisibility(Button.GONE);
            dLayout.setVisibility(RelativeLayout.VISIBLE);
            new CountDownTimer(2000, 1000){
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    saying.setVisibility(TextView.VISIBLE);
                    image2.setVisibility(ImageView.VISIBLE);
                    button.setVisibility(Button.VISIBLE);
                    dLayout.setVisibility(RelativeLayout.INVISIBLE);
                }
            }.start();
        }
    }
}