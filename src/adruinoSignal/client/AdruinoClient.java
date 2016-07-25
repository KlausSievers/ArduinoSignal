/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adruinoSignal.client;

import java.util.LinkedList;
import java.util.List;
import network.Client;

/**
 *
 * @author Klaus
 */
public abstract class AdruinoClient extends Client {

  private static final int PORT = 25156;

  private List<String> trueString = new LinkedList<>();
  private List<String> falseString = new LinkedList<>();

  public AdruinoClient(String host) {
    super(host, PORT);
  }
  
  public void addBooleanString(String trueString, String falseString){
    this.trueString.add(trueString);
    this.falseString.add(falseString);
  }

  @Override
  public void processMessage(String string) {
    String[] pv = string.split("=");
    String name = pv[0];
    String value = pv[1];
    try {
      double doubleValue = Double.parseDouble(value);
      signalDoubleRecieved(name, doubleValue);

    } catch (NumberFormatException ex) {
      boolean booleanValue = false;
      boolean found = false;
      for (String sTrue : trueString) {
        if (string.equalsIgnoreCase(sTrue)) {
          booleanValue = true;
          found = true;
          break;
        }
      }

      if (!found) {
        for (String sFalse : falseString) {
          if (string.equalsIgnoreCase(sFalse)) {
            booleanValue = false;
            found = true;
            break;
          }
        }
      }
      
      if(found){
        signalBooleanRecieved(name, booleanValue);
      } else{
        signalStringRecieved(name, value);
      }      
    }
  }

  

  public abstract void signalDoubleRecieved(String name, double value);

  public abstract void signalBooleanRecieved(String name, boolean value);

  public abstract void signalStringRecieved(String name, String value);

}
