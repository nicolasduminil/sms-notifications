package fr.simplex_software.workshop.notifications.i2;

import com.google.i18n.phonenumbers.*;

import java.util.function.*;

public class Notification
{
  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  public static BiFunction<String, String, Result> phoneNumberValidator = (number, region) ->
  {
    if (number == null)
      return new Failure("### The phone number can not be null");
    else if (number.length() == 0)
      return new Failure("### The phone number can not be empty");
    else
      try
      {
        if (phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(number, region)))
          return new Success();
        else
          return new Failure("### The phone number %s is not valid for region %s".formatted(number, region));
      }
      catch (NumberParseException e)
      {
        return new Failure("### Unexpected exception while parsing the phone number %s".formatted(number));
      }
  };

  public void sendNotification(String phoneNumber, String region, String message)
  {
    Result result = phoneNumberValidator.apply (phoneNumber, region);
    if (result instanceof Success)
    {
      SmsService sms = new SmsService();
      sms.send(phoneNumber, message);
    }
    else
      throw new IllegalArgumentException("### Invalid phone number format: %s".formatted(phoneNumber));
  }
}
