package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.GAIN;
import static ca.mcgill.ecse211.project.Resources.INVALID_SAMPLE_LIMIT;
import static ca.mcgill.ecse211.project.Resources.MAX_P_SPEED_DIFF;
import static ca.mcgill.ecse211.project.Resources.MIN_DIST;
import static ca.mcgill.ecse211.project.Resources.MOTOR_HIGH;
import static ca.mcgill.ecse211.project.Resources.MOTOR_LOW;
import static ca.mcgill.ecse211.project.Resources.POLL_SLEEP_TIME;
import static ca.mcgill.ecse211.project.Resources.P_MOTOR_SPEED;
import static ca.mcgill.ecse211.project.Resources.WALL_DIST;
import static ca.mcgill.ecse211.project.Resources.WALL_DIST_ERR_THRESH;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.rightMotor;
import static ca.mcgill.ecse211.project.Resources.usSensor;

/**
 * UltrasonicController Class provided by Prof Frank P Ferrie for ECSE_211 LAB_1
 * Controls the robot's movements based on ultrasonic data. <br>
 * <br>
 * Control of the wall follower is applied periodically by the
 * UltrasonicController thread in the while loop in {@code run()}. Assuming that
 * {@code usSensor.fetchSample()} and {@code 
 * processUsData()} take ~20ms, and that the thread sleeps for 50 ms at the end
 * of each loop, then one cycle through the loop is approximately 70 ms. This
 * corresponds to a sampling rate of 1/70ms or about 14 Hz.
 * 
 * @author Frank P Ferrie
 * 
 *         BangBangContrller and PTypeController methods implemented by: (Group54)
 *         Name: Joanna Koo     McGill ID: 260865846
 *         Name: Mustafa Javed  McGill ID: 260808710
 */

public class UltrasonicController implements Runnable {

  /**
   * The distance remembered by the {@code filter()} method.
   */
  private int prevDistance;

  /**
    * The number of invalid samples seen by {@code filter()} so far.
    */

  private int invalidSampleCount;

  /**
    * Buffer (array) to store US samples. Declared as an instance variable to avoid creating a new
    *array each time {@code readUsSample()} is called.
    */
  private float[] usData = new float[usSensor.sampleSize()];

  /**
     * The controller type.
     */
  private String type;

  /**
     * Constructor for an abstract UltrasonicController. It makes the robot move forward.
     */
  public UltrasonicController(String type) {
    this.type = type;
    leftMotor.setSpeed(MOTOR_HIGH);
    rightMotor.setSpeed(MOTOR_HIGH);
    leftMotor.forward();
    rightMotor.forward();
  }

  /**
  * Process a movement based on the US distance passed in (BANG-BANG style).
  * 
  *  <p>In the BangBang Controller, the motor speeds are changed to fixed
  *  values of MOTOR_HIGH and MOTOR_LOW according to the needed adjustment to direction.
  *  
  * @param distance the distance in cm
  */
  public void bangBangController(int distance) {
    int distError = WALL_DIST - distance;                   //calculates if adjustment is needed

    if (Math.abs(distError) <= WALL_DIST_ERR_THRESH) {     // if the required adjustment is within bandwidth
      leftMotor.setSpeed(MOTOR_HIGH);                      // keep going straight 
      rightMotor.setSpeed(MOTOR_HIGH);                     // this ensures that the slight variation in sensor data  
      leftMotor.forward();                                 // does not create oscillations
      rightMotor.forward();
      
    }  else if (distError > 0) {                               //robot is too close to the wall

      if (distance < MIN_DIST) {                            //robot is extremely close to the wall 
                                                            // make instantaneous (sharp) turn
        leftMotor.setSpeed(MOTOR_LOW);
        rightMotor.setSpeed(MOTOR_HIGH);
        leftMotor.forward();                            // by turning the left wheel forward
        rightMotor.backward();                          // and the right wheel backwards

      } else {                                          // robot is fairly close to the wall
                                                        // make a soft turn
        leftMotor.setSpeed(MOTOR_HIGH);                   
        rightMotor.setSpeed(MOTOR_LOW);                 // by decreasing speed of the right motor
        leftMotor.forward();
        rightMotor.forward();
      }

    } else if (distError < 0) {                       //robot is too far away from the wall
                                                      // turn left 
      leftMotor.setSpeed(MOTOR_LOW);                  // by decreasing the speed of the right motor
      rightMotor.setSpeed(MOTOR_HIGH);       
      leftMotor.forward();
      rightMotor.forward();

    }
  
  }

  /**
  * Process a movement based on the US distance passed in (P style).
  * 
  * @param distance the distance in cm
  */
  public void pTypeController(int distance) {

    int distError = WALL_DIST - distance;         //used to determines if any adjustment is required
    int diff = calcDiff(distError);               // determines the value of adjustment needed proportional
                                                  // to the distance error
	
    if (Math.abs(distError) <= WALL_DIST_ERR_THRESH) {    // if the required adjustment is within bandwidth
      leftMotor.setSpeed(P_MOTOR_SPEED);                // keep going straight 
      rightMotor.setSpeed(P_MOTOR_SPEED);               // this ensures that the slight variation in sensor data
      leftMotor.forward();                              //does not create oscillations
      rightMotor.forward();
    
    } else if (distError > 0) {                           //robot is too close to the wall

      if (distance < MIN_DIST) {                       //robot is extremely close to the wall
                                                       // make instantaneous (sharp) turn
        leftMotor.setSpeed(P_MOTOR_SPEED);         
        rightMotor.setSpeed(P_MOTOR_SPEED);
        leftMotor.forward();                        // by turning the left wheel forward
        rightMotor.backward();                      // and turning the right wheel backwards

      } else {                                        //robot is fairly close to the wall
                                                      //make a soft turn
        leftMotor.setSpeed(P_MOTOR_SPEED + diff);     // by increasing speed of the left motor by a proportional difference
        rightMotor.setSpeed(P_MOTOR_SPEED - diff);    // by decreasing speed of the right motor by a proportional difference
        leftMotor.forward();
        rightMotor.forward();
      }
    
    } else if (distError < 0) {                           //robot is too far from the wall

      leftMotor.setSpeed(P_MOTOR_SPEED - diff);      // by decreasing speed of the left motor by a proportional difference
      rightMotor.setSpeed(P_MOTOR_SPEED + diff);     // by increasing speed of the right motor by a proportional difference
      leftMotor.forward();
      rightMotor.forward();
    }
  }

 /**  
   *  calDiff method is used to determine the change in speed required.
   *  
   *  @param distError ( the difference between WALL_Distance and Actual distance
   *  
   *  @return change in speed of motor
   */
  private int calcDiff(int diff) {
    if (diff > (-MIN_DIST) && diff < MIN_DIST) { // bandwidth ensures that the slight variation
      return 0;                                  //in sensor data does not create oscillations
    }

    int correction = GAIN * diff;                             // calculate change in speed required 
   
    return Math.min(Math.abs(correction), MAX_P_SPEED_DIFF);  // this limits the change in speed to a maximum value
                                                               // to prevent too sharp turn at corners
  }

 /*
 * Samples the US sensor and invokes the selected controller on each cycle (non Javadoc).
 * 
 * @see java.lang.Thread#run()
 */
	public void run() {
    if (type.equals("BangBang")) {
      while (true) {
        bangBangController(readUsDistance());
        Main.sleepFor(POLL_SLEEP_TIME);
      }
    } else if (type.equals("PType")) {
      while (true) {
        pTypeController(readUsDistance());
        Main.sleepFor(POLL_SLEEP_TIME);
      }
    }
  }

  /**
   * Returns the filtered distance between the US sensor and an obstacle in cm.
   * 
   * @return the filtered distance between the US sensor and an obstacle in cm
   */
  public int readUsDistance() {
    usSensor.fetchSample(usData, 0);                           // extract from buffer, convert to cm, cast to int, and filter
    return filter ((int) (0.71 * (usData[0] * 100.0)));        // Wall Distance is the cosine component of the measured distance
  }                                                           // cos(45)=0.71, hence multiplied by 0.71

  /**
   * Rudimentary filter - toss out invalid samples corresponding to null signal.
   * 
   * @param distance raw distance measured by the sensor in cm
   * @return the filtered distance in cm
   */
  int filter(int distance) {
    if (distance >= 255 && invalidSampleCount < INVALID_SAMPLE_LIMIT) {
      // bad value, increment the filter value and return the distance remembered from before
      invalidSampleCount++;
      return prevDistance;
    } else {
      if (distance < 255) {
        // distance went below 255: reset filter and remember the input distance.
        invalidSampleCount = 0;
      }
      prevDistance = distance;
      return distance;
    }
  }

}