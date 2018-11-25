package bookmarks;

import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

import bookmarks.io.StubIO;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Stepdefs {
	StubIO io;
	Main main;
	ExecutorService exec;
	Future future;

	@Before
	public void setup() throws Throwable {
		io = new StubIO();
		main = new Main(io, ":memory:");
		exec = Executors.newSingleThreadExecutor();
		future = exec.submit(main::run);
		assertEquals("bookmarks v0.1.0", io.readOutput());
		systemWillRespondWithTheHelpPage();
	}

	@After
	public void cleanup() throws Throwable {
		assertEquals("Failed to shut down task cleanly", "> ", io.readOutput());
		io.writeInput("quit");
		assertEquals("Failed to shut down task cleanly", "Bye!", io.readOutput());
		future.get(3, TimeUnit.SECONDS);
		exec.shutdownNow();
	}

	@Given("^the book \"([^\"]*)\" by \"([^\"]*)\" has been added$")
	public void theBookByHasBeenAdded(String title, String author) throws Throwable {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("type", "book");
		metadata.put("Title", title);
		metadata.put("Author", author);
		main.entryDao.save(new Entry(new HashSet<>(), metadata));
	}

	@Given("^the book \"([^\"]*)\" by \"([^\"]*)\" with ISBN \"([^\"]*)\", description \"([^\"]*)\" and tags \"([^\"]*)\" has been added$")
	public void theBookByWithISBNDescriptionAndTagsHasBeenAdded(String title, String author, String isbn, String description, String tags) throws Throwable {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("type", "book");
		metadata.put("Title", title);
		metadata.put("Author", author);
		metadata.put("ISBN", isbn);
		metadata.put("Description", description);
		Set<Tag> tagSet = Arrays.stream(tags.split(","))
			.map(t -> new Tag("tag", t.trim()))
			.collect(Collectors.toSet());
		main.entryDao.save(new Entry(tagSet, metadata));
	}

	@When("^command add is selected$")
	public void command_add_selected() throws Throwable {
		selectCommand("add");
	}

	@When("^command list is selected$")
	public void command_list_selected() throws Throwable {
		selectCommand("list");
	}

	@When("^command help is selected$")
	public void command_help_selected() throws Throwable {
		selectCommand("help");
	}

	@When("^command view is selected$")
	public void commandViewIsSelected() {
		selectCommand("view");
	}

	@When("^command edit is selected$")
	public void command_edit_selected() throws Throwable {
		selectCommand("edit");
	}

	@When("^command search is selected$")
	public void commandSearchIsSelected() {
		selectCommand("search");
	}

	@When("^command delete is selected$")
	public void command_delete_selected() throws Throwable {
		selectCommand("delete");
	}

	private void selectCommand(String command) {
		assertEquals("> ", io.readOutput());
		io.writeInput(command);
	}

	@And("^book ID (\\d+) to view is given$")
	public void bookIDToViewIsGiven(int id) {
		assertEquals("ID of entry to view: ", io.readOutput());
		io.writeInput(Integer.toString(id));
	}

	@When("^book ID (\\d+) to edit is given$")
	public void bookIDToEditIsGiven(int id) {
		assertEquals("ID of entry to edit: ", io.readOutput());
		io.writeInput(Integer.toString(id));
	}

	@When("^book ID (\\d+) and confirmation \"([^\"]*)\" to delete is given$")
	public void bookIDToDeleteIsGiven(int id, String confirmation) {
		assertEquals("ID of entry to delete: ", io.readOutput());
		io.writeInput(Integer.toString(id));

		assertTrue(io.readOutput().startsWith("Entry " + id));
		assertEquals("Are you sure you want to delete the entry [y/N]? ", io.readOutput());
		io.writeInput(confirmation);
	}

	@When("^edit title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void edit_inputs_are_given(String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		assertTrue(io.readOutput().startsWith("Title"));
		io.writeInput(title);
		assertTrue(io.readOutput().startsWith("Author"));
		io.writeInput(author);
		assertTrue(io.readOutput().startsWith("ISBN"));
		io.writeInput(isbn);
		assertTrue(io.readOutput().startsWith("Description"));
		io.writeInput(description);
		assertTrue(io.readOutput().startsWith("Comment"));
		io.writeInput(comment);
		assertTrue(io.readOutput().startsWith("Tags"));
		io.writeInput(tags);
	}

	@When("^type \"([^\"]*)\" is given$")
	public void typeIsGiven(String type) throws Throwable {
		assertEquals("Type: ", io.readOutput());
		io.writeInput(type);
	}

	@When("^title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleAuthorISBNDescriptionCommentAndTagsAreGiven(String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
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

	@Then("^entry ID (\\d+) has title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\"$")
	public void entryIDHasTitleAuthorIsbnDescriptionCommentAndTags(int id, String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		Entry entry = main.entryDao.findOne(id);
		Map<String, String> metadata = entry.getMetadata();

		assertEquals(title, metadata.get("Title"));
		assertEquals(author, metadata.get("Author"));
		assertEquals(isbn, metadata.get("ISBN"));
		assertEquals(description, metadata.get("Description"));
		assertEquals(comment, metadata.get("Comment"));

		Set<String> tagNamesOfEntry = entry.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
		String[] tagList = tags.split(",");
		for (String tag : tagList) {
			assertTrue(tagNamesOfEntry.contains(tag.trim()));
		}
	}

	@When("^search query \"([^\"]*)\" is given$")
	public void searchQueryIsGiven(String query) throws Throwable {
		assertEquals("Term to search: ", io.readOutput());
		io.writeInput(query);
	}

	@Then("^system will respond with \"(.+)\"$")
	public void systemWillRespondWithDQ(String expectedOutput) throws Throwable {
		assertEquals(expectedOutput, io.readOutput());
	}

	@Then("^system will respond with '(.+)'$")
	public void systemWillRespondWithSQ(String expectedOutput) throws Throwable {
		assertEquals(expectedOutput, io.readOutput());
	}

	@Then("^system will respond with the main help page$")
	public void systemWillRespondWithTheHelpPage() throws Throwable {
		assertEquals("add    - add a new entry", io.readOutput());
		assertEquals("edit   - edit an existing entry", io.readOutput());
		assertEquals("search - search for an entry", io.readOutput());
		assertEquals("delete - delete an existing entry", io.readOutput());
		assertEquals("view   - view an existing entry", io.readOutput());
		assertEquals("list   - list all entries", io.readOutput());
		assertEquals("tags   - takes you to tag section", io.readOutput());
		assertEquals("quit   - exits the program", io.readOutput());
		assertEquals("help   - print this screen", io.readOutput());
	}

	@When("CTRL\\+D is pressed")
	public void ctrlDIsPressed() {
		io.writeInput(null);
	}
}
