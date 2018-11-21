package bookmarks;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import bookmarks.dao.*;
import bookmarks.domain.*;
import bookmarks.io.StubIO;

public class Stepdefs {
	Main main;
	StubIO io;

	List<String> inputLines = new ArrayList<>();

	@Given("^command add is selected$")
	public void command_add_selected() throws Throwable {
		inputLines.add("add");
	}

	@Given("^command list is selected$")
	public void command_list_selected() throws Throwable {
		inputLines.add("list");
	}

	@Given("^command help is selected$")
	public void command_help_selected() throws Throwable {
		inputLines.add("help");
	}

	@Given("^new application is run$")
	public void new_application_is_run() {
		io = new StubIO(inputLines);
		main = new Main(io);
		main.run();
	}

	@When("^title \"([^\"]*)\" and type \"([^\"]*)\" and author \"([^\"]*)\" and description \"([^\"]*)\" and comment \\\"([^\\\"]*)\\\" are given$")
	public void a_username_and_password_are_entered(String title, String type, String author, String description, String comment) throws Throwable {
		inputLines.add(title);
		inputLines.add(type);
		inputLines.add(author);
		inputLines.add(description);
		inputLines.add(comment);

		io = new StubIO(inputLines);
		main = new Main(io);
		main.run();
	}

	@When("^application is run$")
	public void application_is_run() {

		io = new StubIO(inputLines);
		main = new Main(io);
		main.run();
	}

	@Then("^system will respond with \"([^\"]*)\"$")
	public void system_will_respond_with(String expectedOutput) throws Throwable {
		assertTrue(io.getPrints().contains(expectedOutput));
	}
}
