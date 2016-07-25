/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adruinoSignal.serial;

/**
 *
 * @author Klaus
 */
public interface SerialPortListener {
  public void receiveSerialMessage(String msg)  ;
}
