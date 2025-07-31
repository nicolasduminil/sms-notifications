package fr.simplex_software.workshop.notifications.i4.tests;

import fr.simplex_software.workshop.notifications.i4.*;
import org.junit.jupiter.api.*;

import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotification
{
  private TestLogHandler logHandler;
  private Logger logger;

  @BeforeEach
  public void setUp()
  {
    logger = Logger.getLogger(Notification.class.getName());
    logHandler = new TestLogHandler();
    logger.addHandler(logHandler);
    logger.setLevel(Level.ALL);
  }

  @AfterEach
  public void tearDown()
  {
    logger.removeHandler(logHandler);
  }

  @Test
  public void testSendNotificationShouldSucceed()
  {
    Notification notification = new Notification();
    assertDoesNotThrow(() ->
      notification.sendNotification("+33615229808", "FR", "Test message"));
  }

  @Test
  public void testSendNotificationShouldFail()
  {
    Notification notification = new Notification();
    notification.sendNotification("+33615229808123", "FR", "Test message");
    assertTrue(logHandler.hasLoggedMessage("### Error: ### The phone number +33615229808123 is not valid for region FR"));
  }

  @Test
  public void testSendNotificationWithNullNumber()
  {
    Notification notification = new Notification();
    notification.sendNotification(null, "FR", "Test message");
    assertTrue(logHandler.hasLoggedMessage("### Error: ### The phone number can not be null"));
  }

  @Test
  public void testSendNotificationWithEmptyNumber()
  {
    Notification notification = new Notification();
    notification.sendNotification("", "FR", "Test message");
    assertTrue(logHandler.hasLoggedMessage("### Error: ### The phone number can not be empty"));
  }

  @Test
  public void testResultIfSuccessShouldSucceed()
  {
    Result<String> result = new Success<>("+33615229808");
    StringBuilder output = new StringBuilder();
    result.ifSuccess(
      phone -> output.append("SMS sent to ").append(phone),
      error -> output.append("Error: ").append(error)
    );
    assertEquals("SMS sent to +33615229808", output.toString());
  }

  @Test
  public void testResultIfSuccessShouldFail()
  {
    Result<String> result = new Failure<>("Invalid number");
    StringBuilder output = new StringBuilder();
    result.ifSuccess(
      phone -> output.append("SMS sent to ").append(phone),
      error -> output.append("Error: ").append(error)
    );
    assertEquals("Error: Invalid number", output.toString());
  }

  private static class TestLogHandler extends Handler
  {
    private StringBuilder logMessages = new StringBuilder();

    @Override
    public void publish(LogRecord record)
    {
      logMessages.append(record.getMessage()).append("\n");
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {}

    public boolean hasLoggedMessage(String message)
    {
      return logMessages.toString().contains(message);
    }
  }
}
