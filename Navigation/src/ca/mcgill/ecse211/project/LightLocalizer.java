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
        	 leftMotor.setSpeed(ROTATE_SPEED);
        	 rightMotor.setSpeed(ROTATE_SPEED);
        	 
                System.out.println(getcolorval());  // get colorval for testing                             

                while ((getcolorval() >= LIGHTSENSOR_DETECTION)) { // go forward until black line is detected
                  leftMotor.forward();
                  rightMotor.forward();
                }
                Navigation.moveStraightFor(5);
                Navigation.turnBy(90.0);     // turn right when line is detected

                while ((getcolorval() >= LIGHTSENSOR_DETECTION)) {   //go forward until black line is detected
                  leftMotor.forward();
                  rightMotor.forward();
                }
                Navigation.moveStraightFor(5);
             
                Navigation.turnBy(-90);  // turn left when line is detected
               
                odometer.setXYT(30.48, 30.48, 0);

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
    
    public boolean correctOrietnation() {
    	leftMotor.setSpeed(ROTATE_SPEED);
    	rightMotor.setSpeed(ROTATE_SPEED);
    	double currentHeading = odometer.getXyt()[2];
  /*  	double  currentX = odometer.getXyt()[0];
    	double  currentY = odometer.getXyt()[1];
    	double currentHeading = odometer.getXyt()[2];
    	boolean linedetected = false;
    	
     if ( currentHeading >= -5 && currentHeading <= 5 ) {
    	while (odometer.getXyt()[1] < currentY + 10 ) {
    		
    		leftMotor.backward();
    		rightMotor.forward();
    		
    		if ( getcolorval() < LIGHTSENSOR_DETECTION ) {
    			linedetected = true;
    			break;
    		}
    		*/
    	
    	while (getcolorval() >= LIGHTSENSOR_DETECTION && odometer.getXyt()[2] > currentHeading-10) {	
    	leftMotor.backward();
    	rightMotor.forward();
    	}
    	if (getcolorval() < 13) {
    	odometer.setTheta(0);
    	return true;
    	}
    	Navigation.turnTo(currentHeading);
    	
    	while (getcolorval() >= LIGHTSENSOR_DETECTION && odometer.getXyt()[2] < currentHeading + 10) {	
    	leftMotor.backward();
    	rightMotor.forward();
    	}
    	if (getcolorval() < 13) {
    	odometer.setTheta(0);
    	return true;
    	}
    	
    	 Navigation.turnTo(currentHeading);
    	 return false;
    }

 /**
  * getter for colorval variable.
  * @return
  */
    public static int getcolorval() { 
        return colorval;
    }

}