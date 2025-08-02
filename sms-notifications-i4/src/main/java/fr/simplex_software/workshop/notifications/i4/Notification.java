package fr.simplex_software.workshop.notifications.i4;

import com.google.i18n.phonenumbers.*;

import java.util.function.*;
import java.util.logging.*;

public class Notification
{
  private static final Logger LOG = Logger.getLogger(Notification.class.getName());
  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  static BiFunction<String, String, Result> phoneNumberValidator = (number, region) ->
  {
    try
    {
      return number == null
        ? new Failure("### The phone number can not be null")
        : number.length() == 0
          ? new Failure("### The phone number can not be empty")
          : phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(number, region))
            ? new Success(number)
            : new Failure("### The phone number %s is not valid for region %s".formatted(number, region));
    }
    catch (NumberParseException e)
    {
      throw new RuntimeException(e);
    }
  };

  public void sendNotification (String phoneNumber, String region, String message)
  {
    phoneNumberValidator.apply(phoneNumber, region).ifSuccess(success, failure);
  }

  static Consumer<String> success = to -> sendSms(to, ">>> SMS sent to %s".formatted(to));

  static Consumer<String> failure = msg -> logError(msg);

  static void logError(String message)
  {
    LOG.info("### Error: %s".formatted(message));
  }

  static void sendSms(String phoneNumber, String message)
  {
    new SmsService().send(phoneNumber, message);
  }
}
