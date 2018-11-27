package bookmarks;

import bookmarks.domain.Entry;
import bookmarks.domain.Tag;
import cucumber.api.java.After;
import cucumber.api.java.Before;
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

	@When("^command \"([^\"]*)\" is selected$")
	public void command_selected(String command) throws Throwable {
		selectCommand(command);
	}

	@When("^tag section command \"([^\"]*)\" is selected$")
	public void when_tag_section_command_is_selected(String command) throws Throwable {
		selectTagCommand(command);
	}

	@Then("^tag ID (\\d+) and confirmation \"([^\"]*)\" to delete is given$")
	public void tag_ID_and_confirmation_to_delete_is_given(int id, String conf) throws Throwable {
		assertEquals("ID of tag to delete: ", io.readOutput());
		io.writeInput(Integer.toString(id));

		assertTrue(io.readOutput().startsWith("tag #" + id));
		assertEquals("Are you sure you want to delete the tag [y/N]? ", io.readOutput());
		io.writeInput(conf);
	}

	private void selectCommand(String command) {
		assertEquals("> ", io.readOutput());
		io.writeInput(command);
	}

	private void selectTagCommand(String command) {
		assertEquals("tags> ", io.readOutput());
		io.writeInput(command);
	}

	@When("^book ID (\\d+) to view is given$")
	public void bookIDToViewIsGiven(int id) {
		assertEquals("ID of entry to view: ", io.readOutput());
		io.writeInput(Integer.toString(id));
	}

	@When("^book ID \"([^\"]*)\" to view is given$")
	public void bookIDToViewIsGiven(String id) throws Throwable {
		assertEquals("ID of entry to view: ", io.readOutput());
		io.writeInput(id);
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
	public void editTitleAuthorISBNDescriptionCommentAndTagsAreGiven(String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		giveEditInputs(new String[]{"Title", "Author", "ISBN", "Description", "Comment", "Tags"},
			new String[]{title, author, isbn, description, comment, tags});
	}

	private void giveEditInputs(String[] keys, String[] values) {
		assertEquals(keys.length, values.length);
		for (int i = 0; i < keys.length; i++) {
			assertTrue(io.readOutput().startsWith(keys[i]));
			io.writeInput(values[i]);
		}
	}

	@When("^type \"([^\"]*)\" is given$")
	public void typeIsGiven(String type) throws Throwable {
		assertEquals("Type: ", io.readOutput());
		io.writeInput(type);
	}

	@When("^title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleAuthorISBNDescriptionCommentAndTagsAreGiven(String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		giveInputs(new String[]{"Title", "Author", "ISBN", "Description", "Comment", "Tags"},
			new String[]{title, author, isbn, description, comment, tags});
	}

	@When("^title \"([^\"]*)\", author \"([^\"]*)\", link \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleAuthorLinkDescriptionCommentAndTagsAreGiven(String title, String author, String link, String description, String comment, String tags) throws Throwable {
		giveInputs(new String[]{"Title", "Author", "Link", "Description", "Comment", "Tags"},
			new String[]{title, author, link, description, comment, tags});
	}

	@When("^title \"([^\"]*)\", author \"([^\"]*)\", podcast name \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleAuthorPodcastNameDescriptionCommentAndTagsAreGiven(String title, String author, String podcastName, String description, String comment, String tags) throws Throwable {
		giveInputs(new String[]{"Title", "Author", "Podcast name", "Description", "Comment", "Tags"},
			new String[]{title, author, podcastName, description, comment, tags});
	}
//        And title "No beep", author "myself", image "Good guy greg", up text "Microwaving at midnight", bottom text "Opens it with one sec left" and comment "" are given
        
        @When("^title \"([^\"]*)\", author \"([^\"]*)\", image \"([^\"]*)\", up text \"([^\"]*)\", bottom text \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleAuthorMemeNameDescriptionCommentAndTagsAreGiven(String title, String author, String image, String up_text, String bottom_text, String comment, String tags) throws Throwable {
		giveInputs(new String[]{"Title", "Author", "Image", "Up text", "Bottom text", "Comment", "Tags"},
			new String[]{title, author, image, up_text, bottom_text, comment, tags});
	}
        
//        And title "Article", author "auto-thor", paper "The good one", description "Mildly wild", comments "Keep an eye on this" and tags "" are given
                @When("^title \"([^\"]*)\", author \"([^\"]*)\", paper \"([^\"]*)\", description \"([^\"]*)\", comments \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleAuthorArticleNameDescriptionCommentAndTagsAreGiven(String title, String author, String paper, String description, String comment, String tags) throws Throwable {
		giveInputs(new String[]{"Title", "Author", "Paper", "Description", "Comment", "Tags"},
			new String[]{title, author, paper, description, comment, tags});
	}
        
        @When("^title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comments \"([^\"]*)\" and tags \"([^\"]*)\" are given$")
	public void titleAuthorBookDescriptionCommentAndTagsAreGiven(String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		giveInputs(new String[]{"Title", "Author", "ISBN", "Description", "Comment", "Tags"},
			new String[]{title, author, isbn, description, comment, tags});
	}
        
        

	private void giveInputs(String[] keys, String[] values) {
		assertEquals(keys.length, values.length);
		for (int i = 0; i < keys.length; i++) {
			assertEquals(keys[i] + ": ", io.readOutput());
			io.writeInput(values[i]);
		}
	}

	@Then("^book entry ID (\\d+) has title \"([^\"]*)\", author \"([^\"]*)\", isbn \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\"$")
	public void bookEntryIDHasTitleAuthorIsbnDescriptionCommentAndTags(int id, String title, String author, String isbn, String description, String comment, String tags) throws Throwable {
		Entry entry = checkCommonMeta("book", id, title, author, description, comment, tags);
		assertEquals(isbn, entry.getMetadata().get("ISBN"));
	}

	@Then("^video entry ID (\\d+) has title \"([^\"]*)\", author \"([^\"]*)\", link \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\"$")
	public void videoEntryIDHasTitleAuthorLinkDescriptionCommentAndTags(int id, String title, String author, String link, String description, String comment, String tags) throws Throwable {
		Entry entry = checkCommonMeta("video", id, title, author, description, comment, tags);
		assertEquals(link, entry.getMetadata().get("Link"));
	}

	@Then("^blog entry ID (\\d+) has title \"([^\"]*)\", author \"([^\"]*)\", link \"([^\"]*)\", description \"([^\"]*)\", comment \"([^\"]*)\" and tags \"([^\"]*)\"$")
	public void blogEntryIDHasTitleAuthorLinkDescriptionCommentAndTags(int id, String title, String author, String link, String description, String comment, String tags) throws Throwable {
		Entry entry = checkCommonMeta("blog", id, title, author, description, comment, tags);
		assertEquals(link, entry.getMetadata().get("Link"));
	}
        
        @Then("^meme entry ID (\\d+) has title \"([^\"]*)\", author \"([^\"]*)\", image \"([^\"]*)\", up text \"([^\"]*)\", bottom text \"([^\"]*)\" and comment \"([^\"]*)\"$")
	public void memeEntryIDHasTitleAuthorLinkDescriptionCommentAndTags(int id, String title, String author, String image, String up_text, String bottom_text, String comment) throws Throwable {
		Entry entry = checkCommonMeta("meme", id, title, author, up_text, bottom_text, comment);
		assertEquals(image, entry.getMetadata().get("Image"));
	}

	private Entry checkCommonMeta(String type, int id, String title, String author, String description, String comment, String tags) throws Throwable {
		Entry entry = main.entryDao.findOne(id);
		Map<String, String> metadata = entry.getMetadata();

		assertEquals(type, metadata.get("type"));
		assertEquals(title, metadata.get("Title"));
		assertEquals(author, metadata.get("Author"));
		assertEquals(description, metadata.get("Description"));
		assertEquals(comment, metadata.get("Comment"));

		Set<String> tagNamesOfEntry = entry.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
		String[] tagList = tags.split(",");
		for (String tag : tagList) {
			if (!tag.isEmpty()) {
				assertTrue(tagNamesOfEntry.contains(tag.trim()));
			}
		}
		return entry;
	}

	@When("^search query \"([^\"]*)\" is given$")
	public void searchQueryIsGiven(String query) throws Throwable {
		assertEquals("Term to search: ", io.readOutput());
		io.writeInput(query);
	}
	@When("^tag find query \"([^\"]*)\" is given$")
	public void tagFindQueryIsGiven(String query) throws Throwable {
		assertEquals("Entries with tag (use %query% for substring of tag): ", io.readOutput());
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
	@Then("^system will respond with the tag help page$")
	public void systemWillRespondWithTheTagHelpPage() throws Throwable {
		assertEquals("find - find entries with tag", io.readOutput());
		assertEquals("delete - delete an existing tag", io.readOutput());
		assertEquals("list   - list all tags", io.readOutput());
		assertEquals("return - return back to home", io.readOutput());
		assertEquals("help   - print this screen", io.readOutput());
	}

	@When("CTRL\\+D is pressed")
	public void ctrlDIsPressed() {
		// Actual CTRL+D is obviously only applicable in ConsoleIO, so we emulate it by sending null to the app.
		io.writeInput(null);
	}
}
