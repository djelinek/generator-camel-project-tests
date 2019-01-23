# generator-camel-project-tests

Automated tests for Yeoman-based Camel Project Generator (generator-camel-project)

## Installing the Camel Project generator

You must have yeoman installed first:
> npm install -g yo

The generator is located in the npm repository (<https://www.npmjs.com/package/generator-camel-project>):
> npm install --global generator-camel-project

## Test execution steps

Get the code:
> git clone <https://github.com/djelinek/generator-camel-project-tests.git>

Executing of all default test scenarios - ***CamelProjectTests***:
> mvn clean package

Executing of specific available test feature:
> mvn clean package -Dtest={test}