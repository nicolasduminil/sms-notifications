package fr.simplex_software.workshop.notifications.i5;

import com.google.i18n.phonenumbers.*;

import java.util.function.*;
import java.util.logging.*;

import static fr.simplex_software.workshop.notifications.i5.Condition.*;

public class Notification
{
  private static final Logger LOG = Logger.getLogger(Notification.class.getName());
  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  private Consumer<String> success = to -> sendSms(to, ">>> SMS sent to %s".formatted(to));
  private Consumer<String> failure = msg -> logError(msg);

  public BiFunction<String, String, Result<String>> phoneNumberValidator = (number, region) ->
  {
    return select(
      when(() -> new Success<>(number)),
      when(() -> number == null, () -> new Failure<>("### The phone number cannot be null.")),
      when(() -> number.length() == 0, () -> new Failure<>("### The phone number cannot not be empty.")),
      when(() ->
      {
        try
        {
          return !phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(number, region));
        }
        catch (NumberParseException e)
        {
          return false;
        }
      }, () -> new Failure<>("### The phone number %s is not for region %s".formatted(number, region))
    ));
  };

  public void sendNotification(String phoneNumber, String region, String message)
  {
    phoneNumberValidator.apply(phoneNumber, region).ifSuccess(success, failure);
  }


  private void logError(String message)
  {
    LOG.info("### Error: %s".formatted(message));
  }

  private void sendSms(String phoneNumber, String message)
  {
    new SmsService().send(phoneNumber, message);
  }
}
