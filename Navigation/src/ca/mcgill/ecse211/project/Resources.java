package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

/**
 * This class is used to define static resources in one place for easy access and to avoid
 * cluttering the rest of the codebase. All resources can be imported at once like this:
 * 
 * <p>{@code import static ca.mcgill.ecse211.lab3.Resources.*;}
 */
public class Resources {
  /**
   * The wheel radius in centimeters.
   */
  public static final double WHEEL_RAD = 2.12;
  
  /**
   * The robot width in centimeters.
   */
  public static final double BASE_WIDTH = 12.0;
  
  /**
   * The speed at which the robot moves forward in degrees per second.
   */
  public static final int FORWARD_SPEED = 150;
  
  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED = 90;
  
  /**
   * Offset from the wall (cm).
   */
  public static final int BAND_CENTER = 35;

  /**
   * Width of dead band (cm).
   */
  public static final int BAND_WIDTH = 3;
  
  /**
   * The noise margin.
   */
  public static final double NOISE_MARGIN = 2;

  /**
   * color value that indicates the detection of black line
   */
  public static final double LIGHTSENSOR_DETECTION = 13;
  
  /**
   * The motor acceleration in degrees per second squared.
   */
  public static final int ACCELERATION = 1000;
  
  /**
   * Timeout period in milliseconds.
   */
  public static final int TIMEOUT_PERIOD = 3000;
  
  /**
   * The tile size in centimeters.
   */
  public static final double TILE_SIZE = 30.48;
  
  /**
   * The tile correction for going to (1, 1).
   */
  public static final double TILE_CORRECTION = 0.95;
  
  /**
   *  Offset for alpha angle
   */
    public static final int OFFSET1= -15;

  /**
   *  offset for beta angle
   */
    public static final int OFFSET2 = -6;
  
  /**
   * The left motor.
   */
  public static final EV3LargeRegulatedMotor leftMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("A"));

  /**
   * The right motor.
   */
  public static final EV3LargeRegulatedMotor rightMotor =
      new EV3LargeRegulatedMotor(LocalEV3.get().getPort("D"));
  
  /**
   * The ultrasonic sensor.
   */
  public static final EV3UltrasonicSensor US_SENSOR =
      new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
  
  /**
   * The color sensor.
   */
  public static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);

  /**
   * The LCD.
   */
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();
  
  /**
   * The odometer.
   */
  public static Odometer odometer = Odometer.getOdometer();

  
}
