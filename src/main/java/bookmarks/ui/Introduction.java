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

	public void run() {
		// Maximum length of prints (80 characters):
		//.print("                                                                                ");
		io.print("Welcome to bookmarks v0.1.0!\n" +
				  "Bookmarks can keep a list of things you want to read, listen, watch or simply\n" +
				  "remember later.\n\n" +
				  "This is an introduction/demo. The introduction will pause between some\n" +
				  "paragraphs. Press enter whenever `**` is printed to unpause. You can\n" +
				  "also press CTRL+D or type cancel at any pause to stop the introduction.\n");
		if (readPause()) return;
		io.print("\nBookmarks is a command-line application, which means all input is text.\n" +
				 "Input is split into two main types: commands and questions.\n");
		if (readPause()) return;
		io.print("Command prompts want a command, such as `add` or `list`. Commands are always a\n" +
				 "single word and command prompts end with a greater-than sign.  The main command\n" +
				 "prompt is just `> `, whereas subsections like tags and help will have something\n" +
				 "like `tags> ` as the prompt.\n");
		if (readPause()) return;
		promptDemo();
		if (readPause()) return;
		io.print("Questions ask for things like entry IDs, search queries, or input data.\n" +
				 "Question prompts end with a colon.\n" +
				 "Sometimes questions will have a default value.  The default value is indicated\n" +
				 "with square brackets.  If you provide no value to a question with a default\n" +
				 "value, the default value will be used as your answer.\n");
		if (readPause()) return;
		questionDemo();
		if (readPause()) return;
		io.print("Also, some questions are simple yes/no questions. In those cases, the square\n" +
				 "brackets contain the options with the default option as a capital letter.\n" +
				 "In other words, [y/N] means a yes/no question that defaults to No, and [Y/n]\n" +
				 "means a yes/no question that defaults to Yes.\n");
		if (readPause()) return;
		yesNoQuestionDemo();
		io.print("Since commands are always a single word, you can also type the answer to the\n" +
				 "first question after the command.  For example, when using `view`, you can\n" +
				 "either type `view` and then `5` when prompted, or simply `view 5` all at once.\n" +
				 "To save a few keystrokes, you can type the first letter of a command instead of\n" +
				 "the whole command\n");
		if (readPause()) return;
		shortcutDemo();
		if (readPause()) return;
		io.print("\nHere's the list of commands:\n");
		app.printHelp();
		io.print("\nYou can type \"help\" to bring up the list again at any time. The help command\n" +
				 "will also let you view more detailed descriptions of commands.\n");
		if (readPause()) return;

		io.print("Introduction complete. Press enter to go to application.\n");
		io.readLine("");
	}

}
