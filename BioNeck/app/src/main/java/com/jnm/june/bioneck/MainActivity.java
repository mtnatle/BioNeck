package com.jnm.june.bioneck;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;


public class MainActivity extends ActionBarActivity {

    Hub hub;
    Intent intent;
    TextView mTextView;
    TextView mLockStateView;
    ImageView imageView1;
    //ImageView imageView2;
    //TextView rollView;
    //TextView pitchView;
    //TextView yawView;

    int exercise = 0;
    int location = 0;

    Boolean startLeft = false;
    Boolean startRight = false;
    Boolean startFront = false;
    Boolean startBack = false;
    Boolean startTurnL = false;
    Boolean startTurnR = false;
    Boolean startRotate = false;



    int start = 0;
    Quaternion startOrientation;
    float startRoll;
    float startPitch;
    float startYaw;



    private DeviceListener mListener = new AbstractDeviceListener() {
        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            // Set the text color of the text view to cyan when a Myo connects.
            //mTextView.setTextColor(Color.CYAN);
            mLockStateView.setText("LEFT!");
            //imageView2.setImageResource(R.drawable.neckleft);
            //AnimationDrawable frameAnimation = (AnimationDrawable) imageView2.getDrawable();
            //frameAnimation.start();
            startLeft = true;
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            // Set the text color of the text view to red when a Myo disconnects.
            //mTextView.setTextColor(Color.RED);
        }

        // onArmSync() is called whenever Myo has recognized a Sync Gesture after someone has put it on their
        // arm. This lets Myo know which arm it's on and which way it's facing.

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {


            if(start == 0){
                startOrientation = new Quaternion(rotation);
                startRoll = (float) Math.toDegrees(Quaternion.roll(startOrientation));
                startPitch = (float) Math.toDegrees(Quaternion.pitch(startOrientation));
                startYaw = (float) Math.toDegrees(Quaternion.yaw(startOrientation));
                start++;
            }

            // Calculate Euler angles (roll, pitch, and yaw) from the quaternion.
            float roll = (float) Math.toDegrees(Quaternion.roll(rotation));
            String sRoll = String.valueOf(roll);
            float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));
            String sPitch = String.valueOf(pitch);
            float yaw = (float) Math.toDegrees(Quaternion.yaw(rotation));
            String sYaw = String.valueOf(yaw);
            // Adjust roll and pitch for the orientation of the Myo on the arm.
            if (myo.getXDirection() == XDirection.TOWARD_ELBOW) {
                roll *= -1;
                pitch *= -1;
            }

            if(startRight == true &&
                    (startRoll - roll) <= -25 &&
                    (startRoll - roll) >= -45){

                myo.vibrate(Myo.VibrationType.MEDIUM);
                startRight = false;
                mLockStateView.setText("BACK!");
                imageView1.setImageResource(R.drawable.neckbck1);
                startBack = true;
                exercise++;

            }

            if(startLeft == true &&
                    (startRoll + roll) <= -25 &&
                    (startRoll + roll) >= -45){

                myo.vibrate(Myo.VibrationType.MEDIUM);
                startLeft = false;
                mLockStateView.setText("RIGHT!");
                imageView1.setImageResource(R.drawable.neckright1);
                startRight = true;
                exercise++;
            }

            if(startBack == true &&
                    (startPitch - 35) >= pitch &&
                    (startPitch - 45) <= pitch){


                myo.vibrate(Myo.VibrationType.MEDIUM);
                startBack = false;
                mLockStateView.setText("FORWARD!");
                imageView1.setImageResource(R.drawable.neckfwd1);;
                startFront = true;
                exercise++;
            }

            if(startFront == true &&
                    (startPitch + 35) <= pitch &&
                    (startPitch + 45) >= pitch){


                myo.vibrate(Myo.VibrationType.MEDIUM);
                startFront = false;
                mTextView.setText("Turn your head");
                mLockStateView.setText("LEFT!");
                imageView1.setImageResource(R.drawable.turnleft1);
                startTurnL = true;
                exercise++;
            }

            if(startTurnL == true &&
                (startYaw + 35) <= yaw &&
                (startYaw + 55) >= yaw){


                myo.vibrate(Myo.VibrationType.MEDIUM);
                startTurnL = false;
                mLockStateView.setText("RIGHT!");
                imageView1.setImageResource(R.drawable.turnright1);;
                startTurnR = true;
                exercise++;
            }

            if(startTurnR == true &&
                (startYaw - 45) >= yaw &&
                (startYaw - 55) <= yaw){


                myo.vibrate(Myo.VibrationType.MEDIUM);
                startTurnR = false;
                mTextView.setText("Time to");
                mLockStateView.setText("Rotate!");
                imageView1.setImageResource(R.drawable.rotate);
                startRotate = true;
                exercise++;
            }

            //back
            if(startRotate == true &&
                    location == 0 &&
                    (startPitch - 35) >= pitch &&
                    (startPitch - 55) <= pitch){


                myo.vibrate(Myo.VibrationType.MEDIUM);
                location++;
            }
            //start rotate left
            if(startRotate == true &&
                    location == 1 &&
                    (startRoll - roll) <= -25 &&
                    (startRoll - roll) >= -45){

                myo.vibrate(Myo.VibrationType.MEDIUM);
                location++;
            }

            //forward
            if(startRotate == true &&
                    location == 2 &&
                    (startPitch + 35) <= pitch &&
                    (startPitch + 45) >= pitch){


                myo.vibrate(Myo.VibrationType.MEDIUM);
                location++;
            }

            //right
            if(startRotate == true &&
                    location == 3 &&
                    (startRoll + roll) <= -25 &&
                    (startRoll + roll) >= -45){

                myo.vibrate(Myo.VibrationType.MEDIUM);
                location++;
            }

            if(startRotate == true &&
                    location == 4 &&
                    (startPitch - 35) >= pitch &&
                    (startPitch - 45) <= pitch){


                myo.vibrate(Myo.VibrationType.MEDIUM);
                startRotate = false;
                exercise++;
            }





            if(exercise >= 7){
                exitApp();
            }
            // Next, we apply a rotation to the text view using the roll, pitch, and yaw.
            //imageView1.setRotation(roll);
            //rollView.setText(sRoll);
            //imageView1.setRotationX(pitch);
            //pitchView.setText(sPitch);
            //imageView1.setRotationY(yaw);
            //yawView.setText(sYaw);

        }


    };

    public void onPose(Myo myo, long timestamp, Pose pose) {
        // Handle the cases of the Pose enumeration, and change the text of the text view
        // based on the pose we receive.
        switch (pose) {
            case UNKNOWN:
                mTextView.setText(getString(R.string.hello_world));
                break;
            case REST:
            case DOUBLE_TAP:
                int restTextId = R.string.hello_world;
                switch (myo.getArm()) {
                    case LEFT:
                        restTextId = R.string.arm_left;
                        break;
                    case RIGHT:
                        restTextId = R.string.arm_right;
                        break;
                }
                mTextView.setText(getString(restTextId));
                break;
            case FIST:
                mTextView.setText("Fist");
                break;
            case WAVE_IN:
                mTextView.setText("Wave In");
                break;
            case WAVE_OUT:
                mTextView.setText("Wave out");
                break;
            case FINGERS_SPREAD:
                mTextView.setText("Fingers Spread");
                break;
        }
        if (pose != Pose.UNKNOWN && pose != Pose.REST) {
            // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
            // hold the poses without the Myo becoming locked.
            myo.unlock(Myo.UnlockType.HOLD);
            // Notify the Myo that the pose has resulted in an action, in this case changing
            // the text on the screen. The Myo will vibrate.
            myo.notifyUserAction();
        } else {
            // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
            // stay unlocked while poses are being performed, but lock after inactivity.
            myo.unlock(Myo.UnlockType.TIMED);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Vibrator v = (Vibrator) MainActivity.this.getSystemService(MainActivity.this.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);

        mTextView = (TextView) findViewById(R.id.textview1);
        mLockStateView = (TextView) findViewById(R.id.textview2);
        imageView1 = (ImageView) findViewById(R.id.rectimage);
        //imageView2 = (ImageView) findViewById(R.id.rectimage1);






        hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            // We can't do anything with the Myo device if the Hub can't be initialized, so exit.
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        MainActivity.this.startActivity(intent);
        //Hub.getInstance().attachToAdjacentMyo();

        hub.addListener(mListener);

        //if(exercise >= 7){
        //    hub.shutdown();
        //    exitApp();
        //}
        //float roll = (float) Math.toDegrees(Quaternion.roll(startOrientation));
        //String sRoll = String.valueOf(roll);
        //rollView.setText(sRoll);


    }

    public void exitApp(){
        hub.shutdown();
        intent = new Intent(MainActivity.this, goodJob.class);
        MainActivity.this.startActivity(intent);
        MainActivity.this.finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

