package bookmarks;

import bookmarks.domain.Entry;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

import bookmarks.io.StubIO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Stepdefs {
	StubIO io = new StubIO();
	Main main = new Main(io, ":memory:");

	@When("^command add is selected$")
	public void command_add_selected() throws Throwable {
		io.write("add");
	}

	@When("^command list is selected$")
	public void command_list_selected() throws Throwable {
		io.write("list");
	}

	@When("^command help is selected$")
	public void command_help_selected() throws Throwable {
		io.write("help");
	}

	@When("^type \"([^\"]*)\", title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleTypeAuthorISBNDescriptionCommentAndTagsAreGiven(String type, String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		io.write(type);
		io.write(title);
		io.write(author);
		io.write(isbn);
		io.write(description);
		io.write(comment);
		io.write(tags);
	}

	@Then("^system will respond with [\"'](.+)[\"']$")
	public void system_will_respond_with(String expectedOutput) throws Throwable {
		io.write("quit");
		main.run();
		assertTrue(io.getPrints().contains(expectedOutput));
	}

	@Given("^the book \"([^\"]*)\" by \"([^\"]*)\" has been added$")
	public void theBookByHasBeenAdded(String title, String author) throws Throwable {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Title", title);
		metadata.put("Author", author);
		main.entryDao.save(new Entry(new HashSet<>(), metadata));
	}
}
