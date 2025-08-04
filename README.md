# Java: From Imperative to Functional - A Complete Use Case

Java, as anybody knows, isn't a functional language. It doesn't allow for *functional
programming*. But once having said that, it's important to mention that there isn't
any general agreed definition of what the *functional programming* is.

In simple terms, the *functional programming* is a programming paradigm which 
consists in programming *with functions*. In the real world, *functions* are 
primarily mathematics concepts defining relations between a *domain* and a *codomain*.
But in traditional Java, they are methods. 

Well, when I'm saying that, in Java, functions are methods, what I mean is that, 
functions may be represented by methods, provided that they satisfy the following
conditions:

  - They don't mutate anything outside their scope, meaning that no internal mutation may be visible from outside.
  - They don't mutate their arguments.
  - They don't throw exceptions.
  - They return a value.
  - They always return the same result when called with the same arguments.

Methods satisfying the rules above are called *functional methods*. However, they
still cannot be considered as the equivalent of functions in functional programming.
As a matter of fact, what they're missing is the ability to be passed as arguments
or to be returned as result values. Consequently, *functional methods* cannot be
composed. One can compose *functional method* applications, but not *functional
methods* themselves, because they belong to classes.

Things have dramatically changed since 2014, with Java 8, which brought a powerful
new syntactic improvement: the *functional interfaces*. Starting from this moment, 
functions have become a first class Java citizen, thanks to the `java.util.function.Function`
class and to the *lambda expressions*. However, this major improvement doesn't 
make Java a functional programming language. Like its ancestors SmallTalk and C++,
it stays an *imperative* programming language, while becoming *functional friendly*.

*Imperative programming* is another programming paradigm where programs are composed
of elements that *do* things, as opposed to functional programs that are composed
of elements that *are* things. Doing something means an initial state, a set of
transitions and an end state. Hence, imperative programs consist in a series of
mutations, from the initial to the final state, separated by conditions testing.
As opposed to the imperative style, functional-style programs don't *do* things.
For example, a function implementing the addition of two integers, let's say 2 
and 3, doesn't *make* 5, but *is* 5. Consequently, each time you encounter 2 + 3
you can replace it by this function. 

Can we do that in Java ? Well, sometimes we can but, very often, it requires to
change the program outcome. If our function, that we want to use to replace the 
expression, doesn't have any other effect than returning the result, then we can,
but this isn't generally the case because we systematically need to mutate 
variables, print out something, write to databases, raise exceptions etc. These
are called *side effects*.

So, functional programming means writing programs without *side effects*. And 
while we certainly can do that in Java for simple cases as the one in the example,
the question is: "can we do it in Java enterprise grade applications ?"

This article tries to answer this question. And, for doing that, I considered a
simple, yet realistic, use case.

## A Simple Yet Realistic Use Case

The simple, yet realistic, case considered here, in order to illustrate my point,
is the one of an SMS notifications service. You may find the project here: https://github.com/nicolasduminil/sms-notifications.
Once you've cloned it, you'll find several implementations of the case, each one making
the object of a separated module in the Maven multi-module project. We start with
`sms-notification-initial` which is the most traditional imperative implementation
and, iteration by iteration, from `i1` to `i5`, we successively refactor it, until
getting the most functional style implementation.

The code below shows our initial, full imperative, implementation of the SMS 
notification service.

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
          throw new IllegalArgumentException("### Invalid phone number format: %s"
            .formatted(phoneNumber));
      }

      private static boolean isValid(String number, String defaultRegion)
      {
        try
        {
          Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil
           .parse(number, defaultRegion);
          return phoneNumberUtil.isValidNumber(phoneNumber);
        }
        catch (NumberParseException e)
        {
          return false;
        }
      }
      ...
    }

As you can see, our notification service uses the `com.google.i18n.phonenumbers.PhoneNumberUtil`
class to check the validity of the phone number and its associated region. Then,
it sends the SMS notification, on the behalf of the `SmsService` class, or it 
raises an `IllegalArgumentException`. The `SmsService` itself is just a fictive
one, which only logs a message.

You can test this simple service as shown below:

    $ git clone https://github.com/nicolasduminil/sms-notifications.git
    $ cd sms-notifications
    $ mvn test

There are a couple of JUnit tests in this module that will succeed.

The code above is purely imperative. The `sendNotification(...)` method doesn't
return anything and it mixes data processing with side effects, like sending an
SMS or throwing an exception. Additionally, it isn't possible to test the phone
number validation in isolation, without the mentioned side effects.

So, in order to improve this code, one of the first thing we can make is to 
separate validation from side effects.

## Separating side effects

In this first iteration, that you can find in the module `sms-notifications-i1`,
we separate the side effects from the validation process, as follows:

    public class Notification
    {
      private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

      public final BiFunction<String, String, Boolean> phoneNumberValidator = (number, region) -> {
        try
        {
          Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, region);
          return phoneNumberUtil.isValidNumber(phoneNumber);
        }
        catch (NumberParseException e)
        {
          return false;
        }
      };

      public void sendNotification(String phoneNumber, String region, String message)
      {
        if (phoneNumberValidator.apply (phoneNumber, region))
        {
          SmsService sms = new SmsService();
          sms.send(phoneNumber, message);
        }
        else
          throw new IllegalArgumentException("### Invalid phone number format: %s".formatted(phoneNumber));
      }
    }

Here, by isolating the phone number validation in the `phoneNumberValidator` 
function, we separate the validation operation from its side effects. Exceptions
are still thrown by the Google API and we cannot change anything here, but they
are caught and the appropriate result is returned.

If you look in the `TestNotifications` class of this module, you may see tests 
like:

    ...
    assertTrue(notification.phoneNumberValidator
      .apply("+33615229808", "FR"), "Salut !");
    ...
    assertFalse(notification.phoneNumberValidator
      .apply("+33615229808123", "FR"), "Salut !");
    ...

that weren't possible before. The 2nd test in the code above fails because the 
phone number passed as an input argument isn't valid in the given region (too 
long). But there is no difference between the case of an invalid phone number 
and the one of an invalid region. Or the one where the phone number is null or 
empty. To address this point, we can define a component able to handle in 
a more specific way the validation result.

## A more functional Result

This component may be found in the `sms-notifications-i2` module and its class 
diagram is shown below:

![class diagram](sms-notifications-i2/result.png "Class Diagram")

Yes, you cannot completely eliminate the try..catch when calling exception-throwing services. The try..catch will always exist somewhere in your codebase when interfacing with imperative APIs.

The Reality of Functional Programming in Java
Pure functional languages like Haskell don't have exceptions - they use types like Maybe or Either for error handling

Java's ecosystem is built on exceptions, so you must bridge between:

Imperative world: Libraries that throw exceptions

Functional world: Your code using Result, Optional, etc.

What You Can Achieve
The best you can do is:

Isolate the imperative exception handling to specific boundary methods

Convert exceptions to functional types (Result, Optional) at the boundary

Keep the core business logic purely functional

So your phoneNumberValidator will always need that try..catch somewhere, but you can:

Move it to a dedicated method (cleaner separation)

Keep the main validation logic functional

Minimize the "imperative surface area"

This is the pragmatic reality of functional programming in Java - you achieve functional style where possible while acknowledging that complete purity isn't feasible when working with exception-based APIs.

Your current approach is actually quite good - the try..catch is contained and the overall flow remains functional.