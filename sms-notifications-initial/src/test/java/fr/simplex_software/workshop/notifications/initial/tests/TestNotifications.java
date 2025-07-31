package fr.simplex_software.workshop.notifications.initial.tests;

import fr.simplex_software.workshop.notifications.initial.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotifications
{
  @Test
  public void testNotificationShouldSucceed()
  {
    assertDoesNotThrow(() ->
      new Notification().sendNotification("+33615229808", "FR", "Salut !"));
  }

  @Test
  public void testNotificationShouldFail()
  {
    assertThrows(IllegalArgumentException.class, () ->
      new Notification().sendNotification("+33615229808123", "FR", "Salut !"));
  }
}
