package bookmarks.ui;

import org.junit.Test;

import static org.junit.Assert.*;

public class InputValidatorTest {
	@Test
	public void validateIsbnCorrectChecksum() {
		assertTrue(InputValidator.validateISBN("978-0-590-54178-7"));
		assertTrue(InputValidator.validateISBN("978 0 590 54178 7"));
		assertTrue(InputValidator.validateISBN("9780590541787"));
		assertFalse(InputValidator.validateISBN("978-0-590-54178-8"));

		assertTrue(InputValidator.validateISBN("0-590-54178-1"));
		assertTrue(InputValidator.validateISBN("0 590 54178 1"));
		assertTrue(InputValidator.validateISBN("0590541781"));
		assertFalse(InputValidator.validateISBN("0-590-54178-5"));
	}

	@Test
	public void validateIsbnCorrectLength() {
		InputValidator test = new InputValidator(); //to improve code coverage
		assertTrue(InputValidator.validateISBN("978-0-590-54178-7"));
		assertTrue(InputValidator.validateISBN("0-590-54178-1"));
		assertFalse(InputValidator.validateISBN("978-0-590-5418-7"));
		assertFalse(InputValidator.validateISBN("978"));
		assertFalse(InputValidator.validateISBN(""));
		assertFalse(InputValidator.validateISBN(null));
	}

	@Test
	public void validateIsbnStartsWithCorrectPrefix() {
		assertTrue(InputValidator.validateISBN("978-0-590-54178-7"));

		assertTrue(InputValidator.validateISBN("979-0-590-54178-6"));

		assertFalse(InputValidator.validateISBN("988-0-590-54178-4"));
	}

	@Test
	public void validateIsbnValidCharacters() {
		assertTrue(InputValidator.validateISBN("978-0-590-54178-7"));
		assertTrue(InputValidator.validateISBN("0-590-54179-x"));

		assertFalse(InputValidator.validateISBN("978-0-59j-54178-x"));
		assertFalse(InputValidator.validateISBN("0-590-5se79-x"));
		assertFalse(InputValidator.validateISBN("testtesttest1"));
	}

	@Test
	public void validateLinkRecognizesCorrectLinks() {
		assertTrue(InputValidator.validateLink("test.com"));
		assertTrue(InputValidator.validateLink("www.test.com"));
		assertTrue(InputValidator.validateLink("http://test.com/page/title"));
		assertTrue(InputValidator.validateLink("https://test.com"));
		assertTrue(InputValidator.validateLink("https://www.subdomain.test.com"));
		assertTrue(InputValidator.validateLink("subdomain.test.com/page/"));

		assertFalse(InputValidator.validateLink("ht/test.com"));
		assertFalse(InputValidator.validateLink("htt p://test.com"));
		assertFalse(InputValidator.validateLink("https:/.test.com"));
		assertFalse(InputValidator.validateLink("https//.test.com"));
		assertFalse(InputValidator.validateLink("https://.test.com"));
	}

	@Test
	public void validateLinkLengthTest() {
		assertFalse(InputValidator.validateLink("http://test.c"));
		assertFalse(InputValidator.validateLink(""));
	}
}
