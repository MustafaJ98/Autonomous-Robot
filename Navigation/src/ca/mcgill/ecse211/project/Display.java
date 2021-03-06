package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

import java.text.DecimalFormat;

/**
 * This class is used to display the content of the odometer variables (x, y, theta).
 * 
 * 
 * @author Frank P Ferrie 
 *  Class provided by Prof Frank P Ferrie for ECSE_211 Lab_2
 *    
 *   Group 54
 *   Name: Joanna Koo           McGill ID: 260865846
 *   Name: Mustafa Javed        McGill ID: 260808710
 */
 
public class Display implements Runnable {

  private double[] position;
  private final long DISPLAY_PERIOD = 25;
  private long timeout = Long.MAX_VALUE;

  public void run() {
    
    lcd.clear();
    
    long updateStart;
    long updateEnd;

    long startTime = System.currentTimeMillis();
    do {
      updateStart = System.currentTimeMillis();

      // Retrieve x, y and Theta information
      position = odometer.getXyt();
      
      // Print x,y, and theta information
      DecimalFormat numberFormat = new DecimalFormat("######0.00");
      lcd.drawString("X: " + numberFormat.format(position[0]), 0, 0);
      lcd.drawString("Y: " + numberFormat.format(position[1]), 0, 1);
      lcd.drawString("T: " + numberFormat.format(position[2]), 0, 2);
      lcd.drawString("US Distance: " + numberFormat.format(UltrasonicLocalizer.getDistance()), 0, 3);
      
      // this ensures that the data is updated only once every period
      updateEnd = System.currentTimeMillis();
      if (updateEnd - updateStart < DISPLAY_PERIOD) {
        Main.sleepFor(DISPLAY_PERIOD - (updateEnd - updateStart));
      }
    } while ((updateEnd - startTime) <= timeout);

  }
  
  /**
   * Sets the timeout in ms.
   * 
   * @param timeout
   */
  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }
  
  /**
   * Shows the text on the LCD, line by line.
   * 
   * @param strings comma-separated list of strings, one per line
   */
  public static void showText(String... strings) {
    lcd.clear();
    for (int i = 0; i < strings.length; i++) {
      lcd.drawString(strings[i], 0, i);
    }
  }

}