package adruinoSignal.tools;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Klaus
 */
public class Matcher {

  private String start;
  private String divider;

  public Matcher(String start, String divider) {
    this.start = start;
    this.divider = divider;
  }

  public String getMsg(String msg) {
    if (msg.startsWith(start)) {
      String content = msg.split(start)[1];
      String[] keyValue = content.split(divider);

      if (keyValue.length == 2) {
        return keyValue[0] + "=" + keyValue[1];
      } else {
        throw new IllegalArgumentException();
      }
    } else{
      return null;
    }
  }

}
