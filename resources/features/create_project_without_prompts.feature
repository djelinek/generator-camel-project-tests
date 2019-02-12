@CamelProjectTests
Feature: Can be Camel project create without prompts?
  User wants to generate new Camel project without prompts (running generator from command line)

  Background: 
    Given Delete project folder - "example"
    Given Create folder for new camel project - "example"

  Scenario: Using default values
    When I generate a project without prompts - "yo camel-project appname=MyApp camelVersion=2.22.2 camelDSL=spring package=com.myapp"
    And I execute Maven with goals "install"
    Then The project is successfully built
