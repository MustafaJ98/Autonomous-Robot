package ca.mcgill.ecse211.project;
import static ca.mcgill.ecse211.project.Resources.*;

/**
 *  Navigation Class controls the motion of the robot. Modification of Driver Class from Lab 3.
 *   exports static methods travelTo(x, y) and turnTo(theta) 
 *  Group 54
 *   Name: Joanna Koo           McGill ID: 260865846
 *   Name: Mustafa Javed        McGill ID: 260808710
 *
 */
public class Navigation {
  
/**
 *  Move the robot to (x ,y) position on the grid.
 *  @param x, the X position on the grid.
 *  @param y, the Xy position on the grid.
 */
  public static void travelTo(double x, double y) {
    double dx; // change in x direction
    double dy; //change in y direction
    double distance; // total distance to travel
    double targetBearing; // bearing to turn to
    
    double x_init = odometer.getXyt()[0];
    double y_init = odometer.getXyt()[1];
    dx = (x*TILE_SIZE) - x_init;  // calculates the difference b/w current and target position
    dy = (y*TILE_SIZE) - y_init; // in x and y direction
    distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)); //Distance to travel is the hypotenuse of dx and dy
    
    targetBearing = computeBearing(dx,dy); // calculates the bearing based on x and y.
    turnTo(targetBearing); // turn to bearing
    moveStraightFor(distance); // start motion
    
  }
  
  /**
   *  turn the robot to Theta (from the vetical axis) using minimal angle
   * @param theta
   */
  public static void turnTo(double theta) {

    double currentBearing = odometer.getXyt()[2]; //get current bearing
    double deltaTheta = theta - currentBearing;  // calculate the change in direction
    
    if (deltaTheta >= 0 && deltaTheta <= 180 ) {  // delta theta in first quadrant
      turnBy(deltaTheta);          
    }
    else if (deltaTheta > 180 ) {    // delta theta in fourth quadrant
      turnBy(deltaTheta -360 );
    }
    else if (deltaTheta < 0 && deltaTheta > -180 ) { // delta theta is in second quadrant
      turnBy(deltaTheta);
    }
    else if (deltaTheta < 0 && deltaTheta < -180 ) {  // delta theta is in third quadrant
      turnBy(deltaTheta + 360);
    }
    
    
  }
/**
 *  computes bearning and return with in   0 < Bearing < 360
 * @param x
 * @param y
 * @return
 */
  private static double computeBearing(double x, double y) {
    double bearing = 0;

    if ( x >= 0.00 && y >= 0.00 ) { // first quadrant
      bearing =  Math.toDegrees( Math.atan(x/y) );  
    } else if (x <= 0.00 && y >= 0.00) {  //second quadrant
      bearing = 360 - Math.toDegrees( Math.atan(-x/y) );
    } else if (x <= 0.00 && y <= 0.00) { // third quadrant
      bearing =  180 + Math.toDegrees( Math.atan(x/y) );
    } else if (x >= 0.00 && y <= 0.00) { // fourth quadrant
      bearing = 180 - Math.toDegrees( Math.atan(-x/y) );
    }
    return bearing;
    
}
	/**
   * Moves the robot straight for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  
  public static void moveStraightFor(double distance) {
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);
    leftMotor.rotate(convertDistance(distance), true);
    rightMotor.rotate(convertDistance(distance), false);
  }
  
  /**
   * Turns the robot by a specified angle
   * 
   * @param angle
   */
  public static boolean turnBy(double angle) {
      leftMotor.setSpeed(ROTATE_SPEED);
      rightMotor.setSpeed(ROTATE_SPEED);
      leftMotor.rotate(convertAngle(angle), true);
      rightMotor.rotate(-convertAngle(angle), false);
      return true;
  }

  /**
   * Converts input distance to the total rotation of each wheel needed to cover
   * that distance.
   * 
   * @param distance
   * @return the wheel rotations necessary to cover the distance
   */
  public static int convertDistance(double distance) {
      return (int) ((180.0 * distance) / (Math.PI * WHEEL_RAD));
  }
  
  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the
   * robot by that angle.
   * 
   * @param angle
   * @return the wheel rotations necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle) {
      return convertDistance(Math.PI * BASE_WIDTH * angle / 360.0);
  }
  
  
}
