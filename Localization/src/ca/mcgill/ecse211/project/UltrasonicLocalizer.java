package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

import ca.mcgill.ecse211.project.Driver;

/**
 * This class includes methods for localizing the robot using the ultrasonic sensor.
 * 
 *   Group 54
 *   Name: Joanna Koo           McGill ID: 260865846
 *   Name: Mustafa Javed        McGill ID: 260808710
 */
public class UltrasonicLocalizer implements Runnable {
 
  private static double[] angles = {0, 0};                              // The 2 angles detected
  private float[] ultrasonicData = new float[US_SENSOR.sampleSize()];   // an array to store ultrasonic distance
  private static double distance, deltaTheta;                       
  static double distance_1, distance_2 = 0;
  
  /**
   * A method to continuously get the distance from the wall using the ultrasonic sensor
   */
  public void run() {
    while (true) {
      US_SENSOR.getDistanceMode().fetchSample(ultrasonicData, 0);   // get distance
      distance = (int) (ultrasonicData[0] * 100.0);                 // extract from buffer, cast to int
      distance = distance > 150 ? 150 : distance;           // A filter to filter out large numbers, 
                                                            // i.e. return 150 for values greater than 150
      try {
        Thread.sleep(20);
        } catch (Exception e) {
      }
    }
  }
  
  /**
   * A method to localize the robot to (1,1) coordinates and at a 0 degree angle.
   */
  public static void ultrasonicLocalising() {
      Driver.turnBy(-87);                      // turn 90 degrees counter-clockwise
      
    while (getDistance() < TILE_SIZE ) {    // keep going backwards along the x until it reaches the black line
          leftMotor.backward();
          rightMotor.backward();
    }
      
    Driver.turnBy(-87);    //turn additional 90 degrees counter-clockwise
    
    while (getDistance() < TILE_SIZE ) {     // keep going backwards along the y until it reaches (1,1)
          leftMotor.backward();
          rightMotor.backward();
    }
    
    Driver.turnBy(173); //turn the robot 180 degrees so it orients to 0 degree
    
  }
  
  /**
   * This method uses fallingEdges to calculate its current angle.
   */
  public static void fallingEdge() {
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    
    while (distance < BAND_CENTER + NOISE_MARGIN) {      // The robot turns clockwise, checking if the robot faces a wall
      leftMotor.forward();
      rightMotor.backward();
    }
    
    while (distance > BAND_CENTER - NOISE_MARGIN) {      // Continues turning clockwise until a wall is detected
      leftMotor.forward();
      rightMotor.backward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    angles[0] = odometer.getXyt()[2]; // store the first angle measured
    
    
    while (distance < BAND_CENTER + NOISE_MARGIN) {      // Now it turns counter-clockwise
      leftMotor.backward();
      rightMotor.forward();
    }
    while (distance > BAND_CENTER - NOISE_MARGIN) {     // Continues turning counter-clockwise until a wall is detected
      leftMotor.backward();
      rightMotor.forward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    angles[1] = odometer.getXyt()[2]; // store the second angle measured
    
    if (angles[0] > angles[1]) { // calculate deltaT using the two measured angles
      deltaTheta = 45 - (angles[0] + angles[1]) / 2.0 + OFFSET1;
    } else {
      deltaTheta = 225 - (angles[0] + angles[1]) / 2.0 + OFFSET2;
    }
    
    double updatedHeading = odometer.getXyt()[2] + deltaTheta;    // add deltaT to the current heading, updating the current heading to true heading
    updatedHeading = (updatedHeading > 360) ? updatedHeading - 360 : updatedHeading;
    odometer.setXYT(0, 0, updatedHeading);
    Driver.turnBy(360-updatedHeading); // the robot orients to 0 degree angle

    leftMotor.stop(true);
    rightMotor.stop(false);
  }
  
  public static double getDistance() {
    return distance;
  }
}
