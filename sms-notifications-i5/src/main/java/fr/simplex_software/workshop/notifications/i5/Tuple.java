package fr.simplex_software.workshop.notifications.i5;

import java.util.*;

public class Tuple<T, U>
{
  private final T first;
  private final U second;

  public Tuple(T first, U second)
  {
    this.first = Objects.requireNonNull(first);
    this.second = Objects.requireNonNull(second);
  }

  public T getFirst()
  {
    return first;
  }

  public U getSecond()
  {
    return second;
  }
}
