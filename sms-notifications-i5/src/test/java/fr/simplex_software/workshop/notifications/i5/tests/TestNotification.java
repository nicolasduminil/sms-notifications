package fr.simplex_software.workshop.notifications.i5.tests;

import fr.simplex_software.workshop.notifications.i5.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestNotification
{
  @Test
  void testPhoneNumberValidatorShouldSucceed()
  {
    Notification notification = new Notification();
    Result<String> result = notification.phoneNumberValidator.apply("+33123456789", "FR");
    assertInstanceOf(Success.class, result);
  }

  @Test
  void testPhoneNumberValidatorShouldFailNullNumber()
  {
    Notification notification = new Notification();
    Result<String> result = notification.phoneNumberValidator.apply(null, "FR");
    assertInstanceOf(Failure.class, result);
  }

  @Test
  void testPhoneNumberValidatorShouldFailEmptyNumber()
  {
    Notification notification = new Notification();
    Result<String> result = notification.phoneNumberValidator.apply("", "FR");
    assertInstanceOf(Failure.class, result);
  }
}