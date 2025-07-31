package fr.simplex_software.workshop.notifications.i1.tests;

import fr.simplex_software.workshop.notifications.i1.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotifications
{
  private Notification notification = new Notification();

  @Test
  public void testPhoneValidatorShouldSucceed()
  {
    assertTrue(notification.phoneNumberValidator.apply("+33615229808", "FR"), "Salut !");
  }

  @Test
  public void testPhoneValidatorShouldFail()
  {
    assertFalse(notification.phoneNumberValidator.apply("+33615229808123", "FR"), "Salut");
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
    assertThrows(IllegalArgumentException.class, () ->
      notification.sendNotification("invalid", "FR", "Test message"));
  }
}
