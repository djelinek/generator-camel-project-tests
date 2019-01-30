@CamelProjectTests
Feature: Can be Camel project create with prompts?
  User wants to generate new Camel project with prompts

  Background: 
    Given Delete project folder - "example"
    Given Create folder for new camel project - "example"

  Scenario: Using default values
    When I generate a project with default values - "yo camel-project"
    And I execute Maven with goals "install"
    Then The project is successfully built

  Scenario: Using user defined values
    When I generate a project with user defined values - "yo camel-project" and "myproject" and "2.21.1" and "spring" and "com.example.test"
    And I execute Maven with goals "install"
    Then The project is successfully built
