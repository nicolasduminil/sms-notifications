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

  /*@Override
  public String toString()
  {
    return String.format("(%s,%s)", t, u);
  }

  public Tuple<U, T> swap()
  {
    return new Tuple<>(u, t);
  }

  public static <T> Tuple<T, T> swapIf(Tuple<T, T> t, Function<T, Function<T, Boolean>> p)
  {
    return p.apply(t.t).apply(t.u) ? t.swap() : t;
  }*/
}
