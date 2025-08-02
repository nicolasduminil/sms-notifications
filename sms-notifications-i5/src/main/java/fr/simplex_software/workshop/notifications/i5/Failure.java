package fr.simplex_software.workshop.notifications.i5;

import java.util.function.*;

public class Failure<T> implements Result<T>
{
  private final String message;

  public Failure (String message)
  {
    this.message = message;
  }

  @Override
  public void ifSuccess(Consumer<T> success, Consumer<String> failure)
  {
    failure.accept(message);
  }
}
