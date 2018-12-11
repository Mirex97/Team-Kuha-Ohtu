package bookmarks.ui;

import bookmarks.io.ConsoleIO;
import bookmarks.io.IO;
import bookmarks.io.StubIO;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppTest {

	static App app;
	static StubIO io;

	@BeforeClass
	public static void before() {
		io = new StubIO();
		app = new App(io, ":memory:");
	}

	@After
	public void tearDown() {
		app.clearPrevList();
		io.clearOutput();
	}

	@Test
	public void sortTest() {
		List<String> orders = new ArrayList<String>();
		app.sort(orders);
		assertEquals("Did not match!", "Nothing to sort. Try using `list`, `search` or `view` first", io.readOutput());
	}

	private void cycleOutput(boolean print) {
		String text = io.readOutput();
		while (!text.isEmpty()) {
			if (print) {
				System.out.println(text);
			}
			try {
				text = io.readOutput();
			} catch (Exception e) {
				break;
			}
		}
	}

	@Test
	public void exportTest() {
		app.setOfflineMode(true);
		app.export();
		assertEquals("Nothing to export. Try using `list`, `search` or `view` first", io.readOutput());
		io.writeInput("video");
		io.writeInput("https://www.youtube.com/watch?v=TzeBrDU-JaY");
		io.writeInput("title");
		io.writeInput("wat");
		io.writeInput("miau");
		io.writeInput("quick, meh");
		io.writeInput("test");
		app.add();
		cycleOutput(false);
		io.writeInput("n");
		app.list();
		cycleOutput(false);
		io.writeInput("List Export");
		app.export();
		io.readOutput();
		assertEquals("Did not work!", "Export successful", io.readOutput());
		
	}     
}
