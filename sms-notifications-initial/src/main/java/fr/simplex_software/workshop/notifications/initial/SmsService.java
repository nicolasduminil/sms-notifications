package fr.simplex_software.workshop.notifications.initial;

import java.util.logging.*;

public class SmsService
{
  private static final Logger LOG = Logger.getLogger(SmsService.class.getName());

  public void send(String phoneNumber, String message)
  {
    LOG.info(">>> Sending SMS to %s: %s".formatted(phoneNumber, message));
  }
}
