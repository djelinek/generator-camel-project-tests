# Feedback after testing

## Generator stores global config

- global config stores default values which are used for prompts hints
- global config file (*.yo-rc-global.json*) is located in user home directory
- it is rewritten with each new generated Camel project
- it should be clear and there should be prompts with default generator hint values
- Clearing steps:
  - Execute 'yo' command
  - Then select 'Clear global config'

## Missing method - *this.config.save();*

- it allows to store 'yo-rc.json' file inside each generated project, this file contains last config for show this in prompts in next return
- it allows to stores different last config settings for each generated project
- <https://yeoman.io/authoring/storage.html>

## Global config history file

- *.yo-rc-global.json* file is created in user home directory
- contains last used prompts of latest generated project

## Running the Generator from the Command Line with wsdl2rest

- Generate project with prompts and from Command Line, there are two different project outputs --> **should be same**

Used Command Line options

> yo camel-project appname=MyApp camelVersion=2.22.2 camelDSL=spring package=com.myapp wsdl=http://localhost:3000/helloworldservice?wsdl outdirectory=src/main/java jaxrs=http://localhost:8081/rest jaxws=http://localhost:3000/helloworldservice

## Generate project with different Camel version

- Camel project could be succesfully run only with Camel version 2.22.2
