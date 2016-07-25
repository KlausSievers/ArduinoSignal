/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adruinoSignal.serial;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author Klaus
 */

//@TODO auf listener umbauen
public class SerialPortReader implements AutoCloseable {
  
  private List<SerialPortListener> listener;

  private String port;
  private int baud;
  private int dataBits;
  private int stopBits;
  private int parityBits;

  private SerialPort serialPort;

  public SerialPortReader(String port, int baud){
    this(port, baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
  }

  public SerialPortReader(String port, int baud, int dataBits, int stopBits, int parityBits) {
    this.port = port;
    this.baud = baud;
    this.dataBits = dataBits;
    this.stopBits = stopBits;
    this.parityBits = parityBits;
    
    listener = new LinkedList<>();

  }

  public void open() throws SerialPortException {
    serialPort = new SerialPort(port);
    serialPort.openPort();
    serialPort.setParams(baud, dataBits, stopBits, parityBits);
    
    new Thread(new Watchdog()).start();
  }

  @Override
  public void close() throws Exception {
    serialPort.closePort();
  }
  
  private void fireEvent(String message){
    for(SerialPortListener spl : listener){
      spl.receiveSerialMessage(message);
    }
  }

  public String getPort() {
    return port;
  }

  public int getBaud() {
    return baud;
  }

  public int getDataBits() {
    return dataBits;
  }

  public int getStopBits() {
    return stopBits;
  }

  public int getParityBits() {
    return parityBits;
  }
  
  public void addSerialPortListener(SerialPortListener spl){
    listener.add(spl);
  }
  
  public boolean removeSerialPortListener(SerialPortListener spl){
    return listener.remove(spl);
  }
  
  private class Watchdog implements Runnable{
    private StringBuilder sb = new StringBuilder();
    
    @Override
    public void run() {
      String buffer;
      while(serialPort.isOpened()){
        try {
          buffer= serialPort.readString(1);
          if (buffer != null && !"".equals(buffer)) {
            if("\n".equals(buffer)){
              fireEvent(sb.toString());
              sb.delete(0, sb.length());
            } else{
              sb.append(buffer);
            }
          }
        } catch (SerialPortException ex) {
          Logger.getLogger(SerialPortReader.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
      }
      }
    }
}
