package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;
import ca.mcgill.ecse211.project.Display;
import ca.mcgill.ecse211.project.UltrasonicLocalizer;
import lejos.hardware.Button;

/**
 * The main driver class for the lab.
 * 
 * Main class of the program.  
 * Controls the processing of Robot.
 * 
 * This class has been inspired from Main Class of Lab 2. @author Frank P Ferrie.
 *   Group 54
 *   Name: Joanna Koo           McGill ID: 260865846
 *   Name: Mustafa Javed        McGill ID: 260808710
 */
public class Main {
  /**
   * The main entry point.
   * 
   * @param args not used
   */
  public static void main(String[] args) {
    new Thread(odometer).start();                       // Start Odometer thread
    new Thread(new UltrasonicLocalizer()).start();      // Start USSensor thread
   
    
    DisplayFallingOption();    // ask and wait for press
  
    new Thread(new Display()).start();                  // Start Display thread
    
   
    (new Thread() {                                     // Run separate thread for fallingEdge
      public void run() {
          UltrasonicLocalizer.fallingEdge();      
      }
    }).start();
    
    int buttonChoiceL = USlocalizerORLightlicalizer();
    
    if (buttonChoiceL == Button.ID_LEFT) {               // starts going to (1,1) with ultrasonicSensor
      ( new Thread() { 
        public void run() {
          UltrasonicLocalizer.ultrasonicLocalising();
        }
      }).start();
       
    } else if (buttonChoiceL == Button.ID_RIGHT) {
      LightLocalizer.activate();       // Initiate thread method to activate light sensor
      LightLocalizer.travelLine();     // Go to (1,1) using light sensor 
    }
  
  
    
    while (Button.waitForAnyPress() != Button.ID_ESCAPE) {
    } // do nothing
    System.exit(0);
  }
  
  /**
   * Method to display Button Press and wait for Button press.
   * @return buttonChoice
   */
  private static void DisplayFallingOption() {
    Display.showText("Press for falling edge");
    Button.waitForAnyPress();   
  }
  
  /**
   * Method to display choice for user (USlocalizer OR Light localizer)"
   * Left for US Localizer
   * Right for Light Localizer
   * @return buttonChoiceL
   */
  private static int USlocalizerORLightlicalizer() {
    int buttonChoice;
    do {
      buttonChoice = Button.waitForAnyPress(); // left or right press
    } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
    return buttonChoice;
  }
  
  /**
   * Sleeps current thread for the specified duration.
   * 
   * @param duration sleep duration in milliseconds
   */
  public static void sleepFor(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      // There is nothing to be done here
    }
  }
  
}
