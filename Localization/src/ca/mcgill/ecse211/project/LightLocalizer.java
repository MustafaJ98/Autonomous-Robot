package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

/**
 * LightLocalizer is used to move the Robot to (1,1) position after the robot
 * has oriented itself.
 * 
 *   Group 54
 *   Name: Joanna Koo           McGill ID: 260865846
 *   Name: Mustafa Javed        McGill ID: 260808710
 *
 */

public class LightLocalizer {

// variable declaration
    private static float buffer[] = new float[Resources.colorSensor.sampleSize()];
    private static int colorval;

    /**
     * travelLine() moves the robot until it reaches the (1,1) point.
     */
    public static void travelLine() {
        leftMotor.setSpeed(FORWARD_SPEED);  //set left motor speed
        rightMotor.setSpeed(FORWARD_SPEED); //set right motor speed

        (new Thread() {  // start as a new thread
         public void run() {

           while (true) {

                System.out.println(getcolorval());  // get colorval for testing                             

                while ((getcolorval() >= LIGHTSENSOR_DETECTION)) { // go forward until black line is detected
                  leftMotor.forward();
                  rightMotor.forward();
                }
                Driver.moveStraightFor(5);
                //leftMotor.rotate(Driver.convertDistance(5), true);  // Distance Correction as Light Sensor
               // rightMotor.rotate(Driver.convertDistance(5), false); // was mounted ahead of wheelbase.
                Driver.turnBy(85.0);     // turn right when line is detected

                while ((getcolorval() >= LIGHTSENSOR_DETECTION)) {   //go forward until black line is detected
                  leftMotor.forward();
                  rightMotor.forward();
                }
                Driver.moveStraightFor(5);
                //leftMotor.rotate(Driver.convertDistance(5), true);   // Distance Correction as Light Sensor
                //rightMotor.rotate(Driver.convertDistance(5), false); // was mounted ahead of wheelbase.
                Driver.turnBy(-89);  // turn left when line is detected
                break;     // reached destination. Breal.
           }

         } 
        }).start();

    }

 /**
  *  Localize starts the light sensor in a thread and regularly updates the colorval variable 
  *  depending on black or white floor.
  */
    public static void activate() {
 
      (new Thread() {   // run as thread
        public void run() {
          while (true) {
            Resources.colorSensor.fetchSample(buffer, 0); //get value from Light sensor
            colorval = (int) buffer[0]; // store value in colorval variable
 
            try {                           //put thread to sleep
              Thread.sleep(50);           // for 50 ms with a frequency of 20Hz.
            } catch (InterruptedException e) {
            } 
          }
        }
      }).start();

    }

 /**
  * getter for colorval variable.
  * @return
  */
    public static int getcolorval() { 
        return colorval;
    }

}