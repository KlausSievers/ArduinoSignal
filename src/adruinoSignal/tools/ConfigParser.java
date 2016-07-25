/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adruinoSignal.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Klaus
 */
public class ConfigParser {

  private Document doc;

  public ConfigParser(String path) throws JDOMException, IOException {
    File f = new File(path);
    SAXBuilder saxBuilder = new SAXBuilder();
    doc = saxBuilder.build(f);
  }

  public Level getLogLevel() {
    Element root = doc.getRootElement();
    return Level.parse(root.getChild("logger").getAttributeValue("level"));
  }

  public String getLogPath() {
    Element root = doc.getRootElement();
    return root.getChild("logger").getAttributeValue("file");
  }

  public String getHost() {
    Element root = doc.getRootElement();
    return root.getChild("socket").getAttributeValue("host");
  }

  public int getTcpPort() {
    Element root = doc.getRootElement();
    return Integer.parseInt(root.getChild("socket").getAttributeValue("port"));
  }

  public String getSerialPort() {
    Element root = doc.getRootElement();
    return root.getChild("serial").getAttributeValue("port");
  }

  public int getBaud() {
    Element root = doc.getRootElement();
    return Integer.parseInt(root.getChild("serial").getAttributeValue("baud"));
  }
  
  public Matcher getMatcher(){
     Element root = doc.getRootElement();
     Element matcherEle = root.getChild("serial").getChild("matcher");
     
     String start = matcherEle.getAttributeValue("start");
     String divider = matcherEle.getAttributeValue("divider");
     
     Matcher matcher = new Matcher(start, divider);
     
     return matcher;
  }
}
