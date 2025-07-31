package fr.simplex_software.workshop.notifications.initial;

import com.google.i18n.phonenumbers.*;

public class Notification
{
  private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

  public void sendNotification(String phoneNumber, String region, String message)
  {
    if (isValid (phoneNumber, region))
    {
      SmsService sms = new SmsService();
      sms.send(phoneNumber, message);
    }
    else
      throw new IllegalArgumentException("### Invalid phone number format: %s".formatted(phoneNumber));
  }

  private static boolean isValid(String number, String defaultRegion)
  {
    try
    {
      Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, defaultRegion);
      return phoneNumberUtil.isValidNumber(phoneNumber);
    }
    catch (NumberParseException e)
    {
      return false;
    }
  }
}
