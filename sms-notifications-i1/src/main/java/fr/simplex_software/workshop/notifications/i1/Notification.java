package fr.simplex_software.workshop.notifications.i1;

import com.google.i18n.phonenumbers.*;

import java.util.function.*;

public class Notification
{
  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
  
  public final BiFunction<String, String, Boolean> phoneNumberValidator = (number, region) -> {
    try
    {
      Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, region);
      return phoneNumberUtil.isValidNumber(phoneNumber);
    }
    catch (NumberParseException e)
    {
      return false;
    }
  };

  public void sendNotification(String phoneNumber, String region, String message)
  {
    if (phoneNumberValidator.apply (phoneNumber, region))
    {
      SmsService sms = new SmsService();
      sms.send(phoneNumber, message);
    }
    else
      throw new IllegalArgumentException("### Invalid phone number format: %s".formatted(phoneNumber));
  }
}
