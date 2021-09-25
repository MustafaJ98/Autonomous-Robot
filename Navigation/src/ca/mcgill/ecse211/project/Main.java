package ca.mcgill.ecse211.project;

//static import to avoid duplicating variables and make the code easier to read
import static ca.mcgill.ecse211.project.Resources.*;
import ca.mcgill.ecse211.project.UltrasonicLocalizer;
import lejos.hardware.Button;


/**
 * The main driver class for the lab.
 *  Group 54
 *   Name: Joanna Koo           McGill ID: 260865846
 *   Name: Mustafa Javed        McGill ID: 260808710
 */
public class Main {
	static int mapChoice;
  /**
   * The main entry point.
   * 
   * @param args not used
   */
  public static void main(String[] args) {
    new Thread(new Display()).start();    //start display
    new Thread(odometer).start();        // start odometer
    new Thread(new UltrasonicLocalizer()).start(); // start US Localizer
  
    Button.waitForAnyPress();
    UltrasonicLocalizer.fallingEdge();               
    
    int buttonChoiceL = USlocalizerORLightlicalizer();
    
    if (buttonChoiceL == Button.ID_LEFT) {               // starts going to (1,1) with ultrasonicSensor
    	
    	UltrasonicLocalizer.ultrasonicLocalising();      
    } else if (buttonChoiceL == Button.ID_RIGHT) {
  	  LightLocalizer.activate();       // Initiate thread method to activate light sensor
      LightLocalizer.travelLine();     // Go to (1,1) using light sensor 
     }
     
     mapChoice = chooseMap();  //chose map using keypad
    (new Thread() {
      public void run() { 
    	  if (mapChoice == Button.ID_UP) {  // if chosen map == 1
           Navigation.travelTo(1, 3);
           Navigation.travelTo(2, 2);
           Navigation.travelTo(3, 3);
           Navigation.travelTo(3, 2);
           Navigation.travelTo(2, 1);
        }
          else if (mapChoice == Button.ID_RIGHT) {  // if chosen map == 2
   	       Navigation.travelTo(2, 2);
           Navigation.travelTo(1, 3);
           Navigation.travelTo(3, 3);
           Navigation.travelTo(3, 2);
    	   Navigation.travelTo(2, 1);
    	  }
          else if (mapChoice == Button.ID_DOWN) {// if chosen map == 3
           Navigation.travelTo(2, 1);
           Navigation.travelTo(3, 2);
           Navigation.travelTo(3, 3);
           Navigation.travelTo(1, 3);
       	   Navigation.travelTo(2, 2);
       	  }
          else if (mapChoice == Button.ID_LEFT) { // if chosen map == 4
          Navigation.travelTo(1, 2);
      	  Navigation.travelTo(2, 3);
          Navigation.travelTo(2, 1);
          Navigation.travelTo(3, 2);
       	  Navigation.travelTo(3, 3);
       	  }
    	 
    	  
      }
    }).start();
    
    while (Button.waitForAnyPress() != Button.ID_ESCAPE) {
    } //do nothing
    System.exit(0);
    
  }
  
  /**
   *  Choose map. 
   *  up = 1
   *  right = 2
   *  down = 3
   *  left = 4
   *  
   * @return chosen map as int
   */
 
  private static int chooseMap() {
	int buttonChoice;
	do {
	      buttonChoice = Button.waitForAnyPress(); // left or right press
	    } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT
	    		&& buttonChoice != Button.ID_UP && buttonChoice!= Button.ID_DOWN);
	    
	return buttonChoice;
  }

/**
  * Method to display choice for user (USlocalizer OR Lightlocalizer)"
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
 * 
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
