package pelatihan.jwp.simplechatboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SimpleChatBoardApplication {

  public static void main(String[] args) {
    SpringApplication.run(SimpleChatBoardApplication.class, args);
  }
}
