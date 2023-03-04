# Shorten mono-repo


## Setup Requirements

Install [`OpenJDK`](https://openjdk.java.net/): We use version 11.0.1 in production.

```sh
$ brew cask install java
```


## IDE

[IntelliJ IDEA](https://www.jetbrains.com/idea/) is highly recommended. This project does not mandate any particular IDE though, so feel free to use whatever works best for you.

We have provided [`.editorconfig`](https://editorconfig.org/) file at project root. Use it with your IDE to establish some basic configuration.


## Running

### Command Line

```sh
$ ./gradlew clean
$ ./gradlew build
$ ./gradlew bootRun # run as spring boot application
$ ./gradlew test
$ ./gradlew bootJar # build fat jar
```

### IDE

All the gradle tasks can be run from IDE as well. Additionally, when run through main function, the app will automatically reload when any code changes are compiled.


## Code Conventions

1. Use 4 spaces for indentation.

1. Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/reference/coding-conventions.html)


## Reading

* [Kotlin Reference](https://kotlinlang.org/docs/reference/) - Easy to follow tutorial cum reference on Kotlin

* [Spring in Action](https://www.manning.com/books/spring-in-action-fifth-edition) - Good introductory book on Spring

* [Spring Documentation](https://docs.spring.io/spring/docs/current/spring-framework-reference/index.html) - Highly detailed. Not for beginners though.
