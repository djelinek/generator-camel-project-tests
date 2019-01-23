package com.github.djelinek.generator_camel_project_tests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "resources/features", plugin = {"pretty"}, tags = {"@CamelProjectTests"})
public class CamelProjectTests {
	
}
