@CamelProjectTests
Feature: Can be camel project create with prompts?
  User wants to generate new camel project with prompts

  Scenario: Using default values  
		Given Create folder for new camel project - "myproject"
		And I generate a project with default values - "yo camel-project"
		And I execute Maven with goals "install"
		Then The project is successfully built
