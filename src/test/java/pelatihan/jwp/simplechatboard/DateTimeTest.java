package pelatihan.jwp.simplechatboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.junit.jupiter.api.Test;

public class DateTimeTest {

  @Test
  void testDateFormat() {
    var dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String timestamp = dateFormatter.format(Calendar.getInstance().getTime());
    System.out.println(timestamp);
  }
}
