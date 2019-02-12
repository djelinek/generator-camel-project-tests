@WSDL2RestTests
Feature: Can be Camel project create with WSDL2REST option?
  User wants to generate new Camel project based on an available WSDL file for a JAX-WS service
  
  Background: 
    Given Delete project folder - "wsdlexample"
    Given Create folder for new camel project - "wsdlexample"
    Given Build wsdl2rest generator feature
    Given Manual start of built-in node.js-powered SOAP service
  
  Scenario Outline: Creating Camel wsdl2rest project with - <template> DSL
    When I generate a project with user defined values wsdl2rest - "yo camel-project --wsdl2rest" and "myproject" and "2.22.2" and "<template>" and "com.example.test" and "http://localhost:3000/helloworldservice?wsdl" and "src/main/java" and "http://localhost:8081/rest" and "http://localhost:3000/helloworldservice" 
    And I execute Maven with goals "install"
    Then The project is successfully built
    And The service is successfully running - "<run>" and "<started>"
    Then The running service generates message - "<message>"
    And Stop running SOAP service

    Examples: 
      | template    | run             | started               | message      |
      | spring      | camel:run       | Starting Camel ...    | spring!      |
      | blueprint   | camel:run       | Starting Camel ...    | blueprint!   |
      | spring-boot | spring-boot:run | started and consuming | spring-boot! |