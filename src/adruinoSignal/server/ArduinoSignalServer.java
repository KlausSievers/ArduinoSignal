/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adruinoSignal.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import network.Server;

/**
 *
 * @author Klaus
 */
public class ArduinoSignalServer {

  private static final Logger logger = Logger.getLogger(ArduinoSignalServer.class.getName());
  
  private static final int ADRUINO_PORT = 25125;
  private static final int LEVEL2_PORT = 25156;

  private final Server level2Server;

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    new ArduinoSignalServer();
  }

  public ArduinoSignalServer() {
    this.level2Server = new Level2Server(LEVEL2_PORT);
    new AdruinoServer(ADRUINO_PORT);
  }

  private class AdruinoServer extends Server {

    public AdruinoServer(int pPortNr) {
      super(pPortNr);
    }

    @Override
    public void processNewConnection(String pClientIP, int pClientPort) {
      logger.log(Level.INFO, "Neues Adruino {0}/:{1}", new Object[]{pClientIP, pClientPort});
    }

    @Override
    public void processClosedConnection(String pClientIP, int pClientPort) {
      logger.log(Level.INFO, "Adruino geschlossen {0}/:{1}", new Object[]{pClientIP, pClientPort});
    }

    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
      logger.log(Level.INFO, "Nachricht von Adruino angekommen {0}/:{1}->{2}", new Object[]{pClientIP, pClientPort, pMessage});
      level2Server.sendToAll(pMessage);
    }
  }

  private class Level2Server extends Server {

    public Level2Server(int pPortNr) {
      super(pPortNr);
    }

    @Override
    public void processNewConnection(String pClientIP, int pClientPort) {
      logger.log(Level.INFO, "Neuer Client {0}/:{1}", new Object[]{pClientIP, pClientPort});
    }

    @Override
    public void processClosedConnection(String pClientIP, int pClientPort) {
      logger.log(Level.INFO, "Client geschlossen {0}/:{1}", new Object[]{pClientIP, pClientPort});
    }

    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
      logger.log(Level.INFO, "Nachricht von Client angekommen {0}/:{1}->{2}", new Object[]{pClientIP, pClientPort, pMessage});
    }

  }

}
