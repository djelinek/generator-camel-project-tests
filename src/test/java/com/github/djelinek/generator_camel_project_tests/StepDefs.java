/*

 * Copyright (C) 2018 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.djelinek.generator_camel_project_tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefs {

	private static final String GENERATOR_CAMEL_PROJECT_PATH = "/target/generator-camel-project/";

	private Process process;
	private File camelProject;

	private boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

	@Given("^Create folder for new camel project - \\\"([^\\\"]*)\\\"")
	public void create_folder_for_new_camel_project(String projectName) throws IOException, InterruptedException {
		camelProject = new File(System.getProperty("user.dir") + GENERATOR_CAMEL_PROJECT_PATH + projectName);
		assertTrue("The new camel project folder creation failed!", camelProject.mkdir());
	}

	@When("^I generate a project with default values - \"([^\"]*)\"$")
	public void i_generate_a_project_with_default_values(String arg) throws IOException, InterruptedException {
		if (isWindows) {
		    process = Runtime.getRuntime()
		      .exec(String.format("cmd.exe /c %s", arg), null, camelProject);
		} else {
			process = Runtime.getRuntime().exec(arg, null, camelProject);
		}
		Utils.setProcessConsoleInput(process, true, "\n", "\n" ,"\n", "\n");
		process.waitFor();
	}
	
	@When("^I execute Maven with goals \"([^\"]*)\"$")
	public void i_execute_Maven_with_goals(String arg) throws Exception {
		process = syncExecuteMaven(camelProject.getAbsolutePath(), arg);
	}
	
	@Then("^The project is successfully built$")
	public void the_project_is_successfully_built() throws Exception {
		String log = Utils.getProcessOutPut(process, true);
		assertTrue("Something went wrong during the build of the project", log.contains("BUILD SUCCESS"));
	}
	
	private Process syncExecuteMaven(String projectLocation, String goals) throws IOException, InterruptedException {
		if (isWindows) {
			return Runtime.getRuntime().exec("cmd.exe /c mvn -f " + projectLocation + " " + goals);
		} else {
			return Runtime.getRuntime().exec("mvn -f " + projectLocation + " " + goals);
		}
	}

}
