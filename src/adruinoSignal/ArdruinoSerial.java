/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adruinoSignal;

import adruinoSignal.serial.SerialPortListener;
import adruinoSignal.serial.SerialPortReader;
import adruinoSignal.tools.ConfigParser;
import adruinoSignal.tools.FileLogger;
import adruinoSignal.tools.Matcher;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;
import jssc.SerialPortList;
import network.Client;
import org.jdom2.JDOMException;

/**
 *
 * @author Klaus
 */
public class ArdruinoSerial extends Client implements SerialPortListener {

  private static final String DEFAULT_CONFIG_PATH = "etc/config.xml"; //uebergebt das als Parameter, hier nur Default
  private Logger logger = Logger.getLogger(ArdruinoSerial.class.getName());

  private SerialPortReader reader;
  private Matcher matcher;

  public ArdruinoSerial(String host, int tcpPort, String serialPort, int baud) {
    super(host, tcpPort);
    
     logger.log(Level.INFO, "TCP Connection opened to {0}/:{1}", new Object[]{host, tcpPort});
    
    String[] portNames = SerialPortList.getPortNames();
    for (int i = 0; i < portNames.length; i++) {
     logger.log(Level.FINE, portNames[i]);
    }

    try {
      reader = new SerialPortReader(serialPort, baud);
      reader.addSerialPortListener(this);
      reader.open();
      logger.log(Level.INFO, "Serial Port {0} opened with BaudRate {1}", new Object[]{serialPort, baud});
    } catch (SerialPortException ex) {
      logger.log(Level.SEVERE, ex.getMessage(), ex);
    }

  }

  public Matcher getMatcher() {
    return matcher;
  }

  public void setMatcher(Matcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public void receiveSerialMessage(String msg) {
    logger.log(Level.INFO, "Received from Serial: {0}", msg);
    if (matcher != null) {
      String toSend = matcher.getMsg(msg);
      if (toSend != null) {
        this.send(toSend);
        logger.log(Level.INFO, "Send to Server {0}", toSend);
      }
    } else {
      super.send(msg);
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {
      String configPath = DEFAULT_CONFIG_PATH;
      if (args.length > 0) {
        configPath = args[0];
      }

      ConfigParser config = new ConfigParser(configPath);

      try {
        FileLogger.setup(config.getLogLevel(), config.getLogPath());
      } catch (IOException ex) {
        Logger.getLogger(ArdruinoSerial.class.getName()).log(Level.INFO, "Logger konnte nicht initialisiert werden.", ex);
      }
      
      ArdruinoSerial ardruinoSerial = new ArdruinoSerial(config.getHost(), config.getTcpPort(), config.getSerialPort(), config.getBaud());
      ardruinoSerial.setMatcher(config.getMatcher());

    } catch (JDOMException | IOException ex) {
      Logger.getLogger(ArdruinoSerial.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void processMessage(String string) {
    //bisher ist keine Kommunikation Server->Client vorgesehen
    //spaeter ggf. Befehle ans Andruino verschicken
  }

}
