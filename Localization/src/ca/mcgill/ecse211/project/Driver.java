package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;


/** Driver controls the motion of the Robot.
* 
* 
* 
* @author Frank P Ferrie 
*  CLass inspired from SquareDriver Class provided for ECSE_211 Lab_2
*    
*   Group 54
*   Name: Joanna Koo            McGill ID: 260865846
*   Name: Mustafa Javed         McGill ID: 260808710
*   
*/
public class Driver {

/**
 * Moves the robot straight for the given distance.
 * @param distance
 */
  public static void moveStraightFor(double distance) {
    leftMotor.rotate(convertDistance(distance), true);
    rightMotor.rotate(convertDistance(distance), false);
  }
  
 /**
  * Turns the robot by a specified angle
  * @param angle
  */
  public static void turnBy(double angle) {
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), false);
  }

  
  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   * 
   * @param distance
   * @return the wheel rotations necessary to cover the distance
   */
  public static int convertDistance(double distance) {
    return (int) ((180.0 * distance) / (Math.PI * WHEEL_RAD));
  }

  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the robot by that
   * angle.
   * 
   * @param angle
   * @return the wheel rotations necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle) {
    return convertDistance(Math.PI * BASE_WIDTH * angle / 360.0);
  }
  
}
