package fr.simplex_software.workshop.notifications.i3;

import com.google.i18n.phonenumbers.*;

import java.util.function.*;
import java.util.logging.*;

public class Notification
{
  private static final Logger LOG = Logger.getLogger(Notification.class.getName());
  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  public static BiFunction<String, String, Result> phoneNumberValidator = (number, region) ->
  {
    try
    {
      return number == null
        ? new Failure("### The phone number can not be null")
        : number.length() == 0
          ? new Failure("### The phone number can not be empty")
          : phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(number, region))
            ? new Success()
            : new Failure("### The phone number %s is not valid for region %s".formatted(number, region));
    }
    catch (NumberParseException e)
    {
      return new Failure ("### The phone number %s is not valid for region %s".formatted(number, region));
    }
  };

  public Runnable sendNotification(String phoneNumber, String region, String message)
  {
    Result result = phoneNumberValidator.apply(phoneNumber, region);
    return (result instanceof Success)
      ? () -> sendSms(phoneNumber, message)
      : () -> logError(((Failure) result).getMessage());
  }

  private void sendSms(String phoneNumber, String message)
  {
    new SmsService().send(phoneNumber, message);
  }

  private static void logError(String message)
  {
    LOG.info("### Error: %s".formatted(message));
  }
}
