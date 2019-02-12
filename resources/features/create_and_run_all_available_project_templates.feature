@CamelProjectRunTests
Feature: Can be Camel project create with all available Camel DSL types, built and run?
  User wants to generate, built and run new Camel project with specific Camel DSL type

  Background: 
    Given Delete project folder - "example"
    Given Create folder for new camel project - "example"

  ### With prompts
  Scenario Outline: Creating Camel project type - <template> DSL
    When I generate a project with user defined values - "yo camel-project" and "myproject" and "2.22.2" and "<template>" and "com.example.test"
    And I execute Maven with goals "install"
    Then The project is successfully built
    And I execute Maven with goals "<run>"
    Then The project is successfully running - "Hello"

    Examples: 
      | template    | run             |
      | blueprint   | camel:run       |
      | spring      | camel:run       |
      | java        | exec:java       |
      | spring-boot | spring-boot:run |

  ### Without prompts
  Scenario Outline: Creating Camel project type - <template> DSL
    When I generate a project without prompts - "yo camel-project appname=myproject camelVersion=2.22.2 camelDSL=<template> package=com.example.test"
    And I execute Maven with goals "install"
    Then The project is successfully built
    And I execute Maven with goals "<run>"
    Then The project is successfully running - "Hello"

    Examples: 
      | template    | run             |
      | blueprint   | camel:run       |
      | spring      | camel:run       |
      | java        | exec:java       |
      | spring-boot | spring-boot:run |
