package fr.simplex_software.workshop.notifications.i2.tests;

import fr.simplex_software.workshop.notifications.i2.*;
import org.junit.jupiter.api.*;

import java.util.logging.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotifications
{
  private Notification notification = new Notification();

  @Test
  public void testPhoneNumberValidatorShouldSucceed()
  {
    Result result = Notification.phoneNumberValidator.apply("+33615229808", "FR");
    assertInstanceOf(Success.class, result);
  }

  @Test
  public void testPhoneNumberValidatorShouldFail()
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
    assertDoesNotThrow(() ->
      notification.sendNotification("+33615229808", "FR", "Test message"));
  }

  @Test
  public void testSendNotificationShouldFail()
  {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
      notification.sendNotification("+33615229808123", "FR", "Test message"));
    assertEquals("### Invalid phone number format: +33615229808123", exception.getMessage());
  }

  @Test
  public void testSendNotificationWithNullNumber()
  {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
      notification.sendNotification(null, "FR", "Test message"));
    assertEquals("### Invalid phone number format: null", exception.getMessage());
  }

  @Test
  public void testSendNotificationWithEmptyNumber()
  {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
      notification.sendNotification("", "FR", "Test message"));
    assertEquals("### Invalid phone number format: ", exception.getMessage());
  }
}
