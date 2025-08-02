package fr.simplex_software.workshop.notifications.i5;

import java.util.function.*;

public interface Result<T>
{
  void ifSuccess(Consumer<T> success, Consumer<String> failure);

  default Result<T> success(T t)
  {
    return new Success(t);
  }
  default Result<T> failure(String message)
  {
    return new Failure<>(message);
  }
}
