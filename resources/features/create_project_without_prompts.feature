@CamelProjectTests
Feature: Can be Camel project create without prompts?
  User wants to generate new Camel project without prompts (running generator from command line)

	Background:
		Given Create folder for new camel project - "example"
		
	Scenario: Using default values  
		When I generate a project with default values - "yo camel-project appname=MyApp camelVersion=2.19.1 camelDSL=spring package=com.myapp"
		And I execute Maven with goals "install"
		Then The project is successfully built
		And Delete project folder - "example"
