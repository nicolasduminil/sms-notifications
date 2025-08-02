package fr.simplex_software.workshop.notifications.i5;

import java.util.function.*;

public class Condition<T> extends Tuple<Supplier<Boolean>, Supplier<Result<T>>>
{
  public Condition(Supplier<Boolean> condition, Supplier<Result<T>> result)
  {
    super(condition, result);
  }

  public static <T> Condition<T> mIf(Supplier<Boolean> condition, Supplier<Result<T>> value)
  {
    return new Condition<>(condition, value);
  }

  public static <T> DefaultCondition<T> mIf(Supplier<Result<T>> value)
  {
    return new DefaultCondition<>(() -> true, value);
  }

  @SafeVarargs
  public static <T> Result<T> match(DefaultCondition<T> defaultCondition, Condition<T>... matchers)
  {
    for (Condition<T> aCondition : matchers)
      if (aCondition.getFirst().get()) return aCondition.getSecond().get();
    return defaultCondition.getSecond().get();
  }
}
