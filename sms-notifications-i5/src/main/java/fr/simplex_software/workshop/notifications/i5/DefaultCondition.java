package fr.simplex_software.workshop.notifications.i5;

import java.util.function.*;

public class DefaultCondition<T> extends Condition<T>
{
  public DefaultCondition(Supplier<Boolean> condition, Supplier<Result<T>> result)
  {
    super(condition, result);
  }
}
