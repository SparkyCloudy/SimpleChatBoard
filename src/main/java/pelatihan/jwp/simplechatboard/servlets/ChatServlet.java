package pelatihan.jwp.simplechatboard.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import lombok.Cleanup;
import lombok.SneakyThrows;

@WebServlet(urlPatterns = "/chat")
public class ChatServlet extends HttpServlet {

  @Override
  @SneakyThrows
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    String room = req.getParameter("room");
    boolean reverseOrder = Boolean.parseBoolean(req.getParameter("reverse"));
    List<String> listMessage = readChatMessages(room);
    if (reverseOrder) {
      Collections.reverse(listMessage);
    }
    String chatHtml = generateChatHtml(listMessage);
    String averageChar = checkAverageCharacter(listMessage);

    // get the html file path
    String htmlUrlString = Objects.requireNonNull(
        ChatServlet.class.getResource("/html/chat_board.html")).getPath().substring(1);
    Path htmlPath = Path.of(htmlUrlString);

    // get the file stream, read it as string, and replace chat tag string
    String html = Files.readString(htmlPath)
        .replace("<!-- Chat messages will be appended here -->", chatHtml)
        .replace("<!-- Average Value -->", averageChar);

    resp.getWriter().println(html);
  }

  @Override
  @SneakyThrows
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    String roomNum = req.getParameter("room");
    var dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String dataRow =
        dateFormatter.format(Calendar.getInstance().getTime()) + "," + req.getParameter("name")
            + "," + req.getParameter("message");

    // store it on the csv file
    writeChatMessage(dataRow, roomNum);

    resp.sendRedirect(req.getContextPath() + "/chat");
  }

  private String checkAverageCharacter(List<String> listMessage) {
    // Get only the last 10 messages
    int listSize = listMessage.size();
    int fromIndex = listSize > 10 ? listMessage.size() - 10 : 0;
    int validChatCount = 0;
    int totalCharacters = 0;

    for (String message : listMessage.subList(fromIndex, listSize)) {
      String[] messages = message.split(",", 3);
      if (messages.length >= 3) {
        totalCharacters += messages[2].replace(" ", "").length();
        validChatCount++;
      }
    }

    // Calculate the average character count
    double average = validChatCount > 0 ? (double) totalCharacters / validChatCount : 0;
    return String.valueOf((int) average);
  }

  @SneakyThrows
  private void writeChatMessage(String dataRow, String roomNum) {
    String filePath = Objects.requireNonNull(
        ChatServlet.class.getResource("/data/chat_" + roomNum + ".csv")).getPath().substring(1);
    Path path = Path.of(filePath);

    @Cleanup BufferedWriter bufferedWriter = Files.newBufferedWriter(path,
        StandardOpenOption.APPEND);
    bufferedWriter.append(dataRow);
    bufferedWriter.newLine();
  }

  @SneakyThrows
  private List<String> readChatMessages(String room) {
    if (Objects.isNull(room)) {
      room = "1";
    }

    String csvUrlString = Objects.requireNonNull(
            ChatServlet.class.getResource("/data/chat_" + room.toLowerCase() + ".csv")).getPath()
        .substring(1);
    Path csvPath = Path.of(csvUrlString);

    List<String> listMessage = new ArrayList<>();
    @Cleanup Scanner sc = new Scanner(csvPath);

    // read every row
    while (sc.hasNextLine()) {
      listMessage.add(sc.nextLine());
    }

    return listMessage;
  }

  private String generateChatHtml(List<String> listMessage) {
    StringBuilder szChatBuilder = new StringBuilder();

    // get only 10 latest messages
    int listSize = listMessage.size();
    int fromIndex = listSize >= 10 ? listMessage.size() - 10 : 0;

    for (String message : listMessage.subList(fromIndex, listSize)) {
      String[] parts = message.split(",", 3);
      if (parts.length == 3) {
        szChatBuilder.append("<div class='chat-message'>").append("<span class='chat-timestamp'>")
            .append(parts[0]).append("</span> ").append("<span class='chat-name'>")
            .append("<strong>").append(parts[1]).append("</strong>").append("</span>: ")
            .append("<span class='chat-text'>").append(parts[2]).append("</span>").append("</div>");
      }
    }

    return szChatBuilder.toString();
  }
}