package bookmarks.ui;

import bookmarks.io.AbstractIO;
import bookmarks.io.IO;

import java.util.Collections;
import java.util.Random;

public class Introduction {
	App app;
	IO io;

	public Introduction(App app, IO io) {
		this.app = app;
		this.io = io;
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignored) {}
	}

	private void lagWrite(String s) {
		lagWrite(s, 150, 200);
	}

	private void lagWrite(String s, int baseTime, int var) {
		Random r = new Random(System.currentTimeMillis());
		sleep(baseTime + r.nextInt(var));
		for (char c : s.toCharArray()) {
			System.out.print(c);
			sleep(baseTime + r.nextInt(var));
		}
	}

	private boolean readPause() {
		String comm = io.readLine("**");

		if (comm.equals(AbstractIO.EndOfTransmission)) {
			return true;
		}
		switch (comm.toLowerCase()) {
			case "back":
			case "return":
			case "exit":
			case "cancel":
				return true;
			default:
				return false;
		}
	}

	private void lineText(String text) {
		if (text.length() % 2 == 1) {
			throw new IllegalArgumentException("Text must have even number of characters");
		}
		int dashCount = (80 - text.length()) / 2;
		String dashes = String.join("", Collections.nCopies(dashCount, "-"));
		io.print(dashes + text + dashes);
	}

	private void promptDemo() {
		lineText("DEMO");
		System.out.print("tags> ");
		lagWrite("list\n");
		io.print("tag #1: sci-fi");
		io.print("tag #2: mystery");
		lineText("DEMO END");
	}

	private void questionDemo() {
		lineText("DEMO");
		System.out.print("I'm a question [and this is the default value]: ");
		lagWrite("this is your answer\n", 50, 100);
		io.print("Answer saved");
		lineText("DEMO END");
	}

	private void yesNoQuestionDemo() {
		lineText("DEMO");
		System.out.print("Do you want to confirm this action [y/N]: ");
		lagWrite("y\n", 400, 200);
		io.print("Action confirmed");
		lineText("DEMO END");
	}

	private void shortcutDemo() {
		lineText("DEMO");

		System.out.print("> ");
		sleep(500);
		lagWrite("view\n");
		System.out.print("The ID of entry to view: ");
		sleep(500);
		lagWrite("5\n");
		System.out.println("\nEntry ID 5: \"The Subtle Knife\" by Philip Pullman");

		lineText("");

		System.out.print("> ");
		sleep(500);
		lagWrite("view 5\n");
		System.out.println("\nEntry ID 5: \"The Subtle Knife\" by Philip Pullman");

		lineText("DEMO END");
	}

	private void exportDemo() {
		lineText("DEMO");

		System.out.print("> ");
		sleep(500);
		lagWrite("list\n");

		System.out.print("List only unread entries [y/N]? ");
		sleep(500);
		lagWrite("\n");

		System.out.println("Entries:");
		System.out.println("1. \"B\" by A");
		System.out.println("2. \"A\" by B");
		System.out.println("3. \"A\" by A");
		System.out.println("4. \"B\" by B");
		System.out.print("\n");

		System.out.print("> ");
		sleep(500);
		lagWrite("export\n");

		System.out.print("File to export to: ");
		sleep(500);
		lagWrite("books.txt\n");
		System.out.println("Export successful");

		lineText("DEMO END");
	}

	private void sortDemo() {
		lineText("DEMO");
		System.out.print("> ");
		sleep(500);
		lagWrite("list\n");

		System.out.print("List only unread entries [y/N]? ");
		sleep(500);
		lagWrite("\n");

		System.out.println("Entries:");
		System.out.println("1. \"B\" by A");
		System.out.println("2. \"A\" by B");
		System.out.println("3. \"A\" by A");
		System.out.println("4. \"B\" by B");
		System.out.print("\n");
		
		System.out.print("> ");
		sleep(500);
		lagWrite("sort Author Title\n");

		System.out.println("1st parameter to sort by: Author");
		System.out.println("2nd parameter to sort by: Title");
		System.out.print("3rd parameter to sort by: ");

		sleep(500);
		lagWrite("\n");

		System.out.println("Entries:");
		System.out.println("3. \"A\" by A");
		System.out.println("1. \"B\" by A");
		System.out.println("1. \"A\" by B");
		System.out.println("4. \"B\" by B");
		System.out.print("\n");
		
		lineText("DEMO END");
	}

	public void run() {
		// Maximum length of prints (80 characters):
		//.print("                                                                                ");
		io.print("Welcome to bookmarks v0.1.0!");
		io.print("Bookmarks can keep a list of things you want to read, listen, watch or simply");
		io.print("remember later.");
		io.print("");
		io.print("This is an introduction/demo. The introduction will pause between some");
		io.print("paragraphs. Press enter whenever `**` is printed to unpause. You can");
		io.print("also press CTRL+D or type cancel at any pause to stop the introduction.");
		if (readPause()) return;
		io.print("");

		io.print("Bookmarks is a command-line application, which means ALL input is text.");
		io.print("Input is split into two main types: commands and questions.");
		if (readPause()) return;
		io.print("Command prompts want a command, such as `add` or `list`. Commands are always a");
		io.print("single word and command prompts end with a greater-than sign.  The main command");
		io.print("prompt is just `> `, whereas subsections like tags and help will have something");
		io.print("like `tags> ` as the prompt.");
		if (readPause()) return;
		promptDemo();
		if (readPause()) return;
		io.print("Questions ask for things like entry IDs, search queries, or input data.");
		io.print("Question prompts end with a colon.");
		io.print("Sometimes questions will have a default value.  The default value is indicated");
		io.print("with square brackets.  If you provide no value to a question with a default");
		io.print("value, the default value will be used as your answer.");
		if (readPause()) return;
		questionDemo();
		if (readPause()) return;
		io.print("Also, some questions are simple yes/no questions. In those cases, the square");
		io.print("brackets contain the options with the default option as a capital letter.");
		io.print("In other words, [y/N] means a yes/no question that defaults to No, and [Y/n]");
		io.print("means a yes/no question that defaults to Yes.");
		if (readPause()) return;
		yesNoQuestionDemo();

		io.print("Since commands are always a single word, you can also type the answer to the");
		io.print("first question after the command.  For example, when using `view`, you can");
		io.print("either type `view` and then `5` when prompted, or simply `view 5` ALL at once.");
		io.print("To save a few keystrokes, you can type the first letter of a command instead of");
		io.print("the whole command");
		if (readPause()) return;
		shortcutDemo();
		if (readPause()) return;
		
		io.print("The program keeps track of the last shown list. You can set this list with");
		io.print("commands 'list', 'search' and 'show'. Two commands exist that operate on this");
		io.print("list: 'export' and 'sort'. Export prints the list into a .txt file");
		if (readPause()) return;
		exportDemo();
		if (readPause()) return;

		io.print("Sort sorts the list, by the given properties, into alphabetical order.");
		io.print("If some entries in the list do not have some property, they appear before");
		io.print("entries that do. You can also sort by 'Id', in which case the sort is numeric.");
		if (readPause()) return;
		sortDemo();
		if (readPause()) return;

		io.print("");
		io.print("Here's the list of commands:");
		app.printHelp();
		io.print("");
		io.print("You can type \"help\" to bring up the list again at any time. The help command");
		io.print("will also let you view more detailed descriptions of commands.");
		if (readPause()) return;

		io.print("Introduction complete. Press enter to go to application.");
		io.readLine("");
	}

}
