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

So, functional programming means writing programs without *side effects*. Can we
do it in Java ? This is the question to which I'm trying to answer in this article.
And, for doing that, I considered a simple, yet realistic, use case.

## A Simple Yet Realistic Use Case

