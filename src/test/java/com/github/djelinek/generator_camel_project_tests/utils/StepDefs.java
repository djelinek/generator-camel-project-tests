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
package com.github.djelinek.generator_camel_project_tests.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefs {
	
	private static final String GENERATOR_CAMEL_PROJECT_PATH = System.getProperty("user.dir") + "/target/generator-camel-project/";

	private Process process;
	private Process soapService;
	private Process genMessage;
	private File camelProject;
	
	private boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

	@Given("^Create folder for new camel project - \"([^\"]*)\"$")
	public void create_folder_for_new_camel_project(String projectName) throws IOException, InterruptedException {
		camelProject = new File(GENERATOR_CAMEL_PROJECT_PATH + projectName);
		assertTrue("The new camel project folder creation failed!", camelProject.mkdir());
	}

	@Given("^Delete project folder - \"([^\"]*)\"$")
	public void delete_project_folder_if_exists(String folder) throws IOException {
		String path = GENERATOR_CAMEL_PROJECT_PATH + folder;
		if(new File(path).exists()) {
			Utils.deleteDirectory(Paths.get(path));
		}
	}
	
	@Given("^Build wsdl2rest generator feature")
	public void build_wsdl2rest_generator_feature() throws IOException, InterruptedException {
		process = syncExecuteMaven(GENERATOR_CAMEL_PROJECT_PATH + "app/wsdl2rest/", "clean package");
		process.waitFor();
	}
	
	@Given("^Manual start of built-in node.js-powered SOAP service")
	public void manual_start_of_built_in_nodejs_powered_SOAP_service() throws Exception {
		String[] cmds = {"node", "manualstart.js"};
		soapService = Runtime.getRuntime().exec(cmds, null, new File(GENERATOR_CAMEL_PROJECT_PATH + "manual/"));
		waitUntilConsoleHasText(soapService, "server running");		
	}
	
	@Given("^Stop running SOAP service") 
	public void stop_running_soap_service() {
		soapService.destroy();
		genMessage.destroy();
	}

	@When("^I generate a project with default values - \"([^\"]*)\"$")
	public void i_generate_a_project_with_default_values(String arg) throws IOException, InterruptedException {
		process = executeShellCommand(arg);
		Utils.setProcessConsoleInput(process, true, "\n", "\n", "\n", "\n");
		process.waitFor();
	}
	
	@When("^I generate a project without prompts - \"([^\"]*)\"$")
	public void i_generate_a_project_without_prompts(String arg) throws IOException, InterruptedException {
		process = executeShellCommand(arg);
		process.waitFor();
	}

	@When("^I generate a project with user defined values - \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void i_generate_a_project_with_user_defined_values(String arg, String projectName, String camelVersion,
			String dslType, String packageName) throws IOException, InterruptedException {
		process = executeShellCommand(arg);
		Utils.setProcessConsoleInput(process, true, projectName, camelVersion, dslType, packageName);
		process.waitFor();
	}
	
	@When("^I generate a project with user defined values wsdl2rest - \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void i_generate_a_project_with_user_defined_values_wsdl2rest(String arg, String projectName, String camelVersion,
			String dslType, String packageName, String wsdl, String outDir, String jaxrs, String jaxws) throws IOException, InterruptedException {
		process = executeShellCommand(arg);
		Utils.setProcessConsoleInput(process, true, projectName, camelVersion, dslType, packageName, wsdl, outDir, jaxrs, jaxws);
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
	
	@Then("^The project is successfully running - \"([^\"]*)\"$")
	public void the_project_is_successfully_running(String message) throws Exception {
		try {			
			assertTrue("The generated camel project is not running properly", waitUntilConsoleHasText(process, message));
		} catch (Exception e) {
			if(e.getMessage().equals("Stream closed")) {
				fail("The generated camel project is not running properly - BUILD FAILED");
			} else {
				throw new Exception(e);
			}
		}	
	}
	
	@Then("^The service is successfully running - \"([^\"]*)\" and \"([^\"]*)\"$")
	public void the_service_is_successfully_running(String arg, String message) throws Exception {
		genMessage = syncExecuteMaven(camelProject.getAbsolutePath(), arg);
		waitUntilConsoleHasText(genMessage, message);
	}
	
	@Then("^The running service generates message - \"([^\"]*)\"$")
	public void the_running_service_generates_message(String message) throws Exception {
		process = Runtime.getRuntime().exec("curl -s POST http://localhost:8081/rest/hello/%22" + message + "%22");
		process.waitFor();
		String output = Utils.getProcessOutPut(process, true);
		assertTrue("The generated service is not generating messages properly", output.contains("Hello " + message));
	}

	private Process syncExecuteMaven(String projectLocation, String goals) throws IOException, InterruptedException {
		if (isWindows) {
			return Runtime.getRuntime().exec("cmd.exe /c mvn -f " + projectLocation + " " + goals);
		} else {
			return Runtime.getRuntime().exec("mvn -f " + projectLocation + " " + goals);
		}
	}

	private Process executeShellCommand(String arg) throws IOException {
		if (isWindows) {
			return Runtime.getRuntime().exec(String.format("cmd.exe /c %s", arg), null, camelProject);
		} else {
			return Runtime.getRuntime().exec(arg, null, camelProject);
		}
	}
	
	private boolean waitUntilConsoleHasText(Process process, String text) throws Exception {
		boolean isRunning = false;
		for (int i = 0; i < 10; i++) {
			if (Utils.checkAndlogProcessOutput(process, text)) {
				isRunning = true;
				break;
			}
			Thread.sleep(500);
		}
		return isRunning;
	}

}
