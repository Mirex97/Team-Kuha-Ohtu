package bookmarks;

import bookmarks.domain.Entry;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

import bookmarks.io.StubIO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.*;

public class Stepdefs {
	StubIO io;
	Main main;
	ExecutorService exec;
	Future future;

	@Before
	public void setup() {
		io = new StubIO();
		main = new Main(io, ":memory:");
		exec = Executors.newSingleThreadExecutor();
		future = exec.submit(main::run);
		assertEquals("bookmarks v0.1.0", io.readOutput());
		for (int i = 0; i < 9; i++) {
			io.readOutput();
		}
	}

	@After
	public void cleanup() {
		io.writeInput("quit");
		try {
			future.get(1, TimeUnit.SECONDS);
			exec.shutdownNow();
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			future.cancel(true);
			exec.shutdownNow();
		}
	}

	@When("^command add is selected$")
	public void command_add_selected() throws Throwable {
		assertEquals("> ", io.readOutput());
		io.writeInput("add");
	}

	@When("^command list is selected$")
	public void command_list_selected() throws Throwable {
		assertEquals("> ", io.readOutput());
		io.writeInput("list");
	}

	@When("^command help is selected$")
	public void command_help_selected() throws Throwable {
		assertEquals("> ", io.readOutput());
		io.writeInput("help");
	}

	@When("^command view is selected$")
	public void commandViewIsSelected() {
		assertEquals("> ", io.readOutput());
		io.writeInput("view");
	}

	@And("^book ID (\\d+) is given$")
	public void bookIDIsGiven(int id) {
		assertEquals("ID of entry to view: ", io.readOutput());
		io.writeInput(Integer.toString(id));
	}
	
	@When("^command edit is selected$")
	public void command_edit_selected() throws Throwable {
		io.write("edit");
	}
	
	@When("^input \"([^\"]*)\" is given$")
	public void input_is_given(String input) throws Throwable {
		io.write(input);
	}
	
	@When("^edit title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void edit_inputs_are_given(String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		io.write(title);
		io.write(author);
		io.write(isbn);
		io.write(description);
		io.write(comment);
		io.write(tags);
	}

	@When("^type \"([^\"]*)\", title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleTypeAuthorISBNDescriptionCommentAndTagsAreGiven(String type, String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		assertEquals("Type: ", io.readOutput());
		io.writeInput(type);
		assertEquals("Title: ", io.readOutput());
		io.writeInput(title);
		assertEquals("Author: ", io.readOutput());
		io.writeInput(author);
		assertEquals("ISBN: ", io.readOutput());
		io.writeInput(isbn);
		assertEquals("Description: ", io.readOutput());
		io.writeInput(description);
		assertEquals("Comment: ", io.readOutput());
		io.writeInput(comment);
		assertEquals("Tags: ", io.readOutput());
		io.writeInput(tags);
	}

	@Then("^system will respond with \"(.+)\"$")
	public void systemWillRespondWithDQ(String expectedOutput) throws Throwable {
		assertEquals(expectedOutput, io.readOutput());
	}

	@Then("^system will respond with '(.+)'$")
	public void systemWillRespondWithSQ(String expectedOutput) throws Throwable {
		assertEquals(expectedOutput, io.readOutput());
	}

	@Given("^the book \"([^\"]*)\" by \"([^\"]*)\" has been added$")
	public void theBookByHasBeenAdded(String title, String author) throws Throwable {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("type", "book");
		metadata.put("Title", title);
		metadata.put("Author", author);
		main.entryDao.save(new Entry(new HashSet<>(), metadata));
	}
}
