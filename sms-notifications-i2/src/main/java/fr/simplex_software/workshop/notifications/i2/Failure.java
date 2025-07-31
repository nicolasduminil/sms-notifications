package fr.simplex_software.workshop.notifications.i2;

public class Failure implements Result
{
  private final String message;

  public Failure(String message)
  {
    this.message = message;
  }

  public String getMessage()
  {
    return message;
  }
}
