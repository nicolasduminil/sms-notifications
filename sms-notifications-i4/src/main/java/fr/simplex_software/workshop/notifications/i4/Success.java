package fr.simplex_software.workshop.notifications.i4;

import java.util.function.*;

public class Success<T> implements Result<T>
{
  private final T t;

  public Success(T t)
  {
    this.t = t;
  }

  @Override
  public void ifSuccess(Consumer<T> success, Consumer<String> failure)
  {
    success.accept(t);
  }
}
