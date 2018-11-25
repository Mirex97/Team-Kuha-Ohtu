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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

	@Given("^the book \"([^\"]*)\" by \"([^\"]*)\" has been added$")
	public void theBookByHasBeenAdded(String title, String author) throws Throwable {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("type", "book");
		metadata.put("Title", title);
		metadata.put("Author", author);
		main.entryDao.save(new Entry(new HashSet<>(), metadata));
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
		
		Set<Tag> tagsOfEntry = entry.getTags();
		Set<String> tagNamesOfEntry = new HashSet<>();
		for (Tag entryTag : tagsOfEntry) {
			tagNamesOfEntry.add(entryTag.getName());
		}
		
		String[] tagList = tags.split(",");
		System.out.println(tagsOfEntry);
		System.out.println(tags);
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
}
