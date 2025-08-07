package fr.simplex_software.workshop.notifications.i3.tests;

import fr.simplex_software.workshop.notifications.i3.*;
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
  public void testPhoneNumberValidatorShouldSucceed()
  {
    Result result = Notification.phoneNumberValidator.apply("+33615229808", "FR");
    assertInstanceOf(Success.class, result);
  }

  @Test
  public void testPhoneNumberShouldFail()
  {
    Result result = Notification.phoneNumberValidator.apply("+33615229808123", "FR");
    assertInstanceOf(Failure.class, result);
    assertEquals("### The phone number +33615229808123 is not valid for region FR",
      ((Failure) result).getMessage());
  }

  @Test
  public void testPhoneNumberValidatorWithNullNumber()
  {
    Result result = Notification.phoneNumberValidator.apply(null, "FR");
    assertInstanceOf(Failure.class, result);
    assertEquals("### The phone number can not be null",
      ((Failure) result).getMessage());
  }

  @Test
  public void testPhoneNumberValidatorWithEmptyNumber()
  {
    Result result = Notification.phoneNumberValidator.apply("", "FR");
    assertInstanceOf(Failure.class, result);
    assertEquals("### The phone number can not be empty",
      ((Failure) result).getMessage());
  }

  @Test
  public void testSendNotificationShouldSucceed()
  {
    Notification notification = new Notification();
    Runnable action = notification.sendNotification("+33615229808", "FR", "Test message");
    assertNotNull(action);
    assertDoesNotThrow(() -> action.run());
  }

  @Test
  public void testSendNotificationShouldFail()
  {
    Notification notification = new Notification();
    Runnable action = notification.sendNotification("+33615229808123", "FR", "Test message");
    assertNotNull(action);
    action.run();
    assertTrue(logHandler.hasLoggedMessage("### Error: ### The phone number +33615229808123 is not valid for region FR"));
  }

  @Test
  public void testSendNotificationWithNullNumber()
  {
    Notification notification = new Notification();
    Runnable action = notification.sendNotification(null, "FR", "Test message");
    assertNotNull(action);
    action.run();
    assertTrue(logHandler.hasLoggedMessage("### Error: ### The phone number can not be null"));
  }

  @Test
  public void testSendNotificationWithEmptyNumber()
  {
    Notification notification = new Notification();
    Runnable action = notification.sendNotification("", "FR", "Test message");
    assertNotNull(action);
    action.run();
    assertTrue(logHandler.hasLoggedMessage("### Error: ### The phone number can not be empty"));
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
