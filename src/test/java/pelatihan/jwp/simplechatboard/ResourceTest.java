package pelatihan.jwp.simplechatboard;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pelatihan.jwp.simplechatboard.servlets.ChatServlet;

public class ResourceTest {

  @Test
  @SneakyThrows
  void getMessageFromResource() {

    String csvUrlString = Objects.requireNonNull(ChatServlet.class.getResource("/data/chat.csv"))
        .getPath().substring(1);
    Path csvPath = Path.of(csvUrlString);

    List<String> listMessage = new ArrayList<>();
    @Cleanup Scanner sc = new Scanner(csvPath);

    while (sc.hasNextLine()) {
      listMessage.add(sc.nextLine());
    }

    System.out.println(listMessage);
  }

  @Test
  void getUrlOfResourceFolder() {
    String filePath = Objects.requireNonNull(ChatServlet.class.getResource("/data/chat.csv"))
        .getPath().substring(1);
    System.out.println(filePath);
  }
}
