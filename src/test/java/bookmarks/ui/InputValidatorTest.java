package bookmarks.ui;

import bookmarks.domain.InputValidators;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputValidatorTest {
	@Test
	public void validateISBNCorrectChecksum() {
		assertTrue(InputValidators.ISBN.validate("978-0-590-54178-7"));
		assertTrue(InputValidators.ISBN.validate("978 0 590 54178 7"));
		assertTrue(InputValidators.ISBN.validate("9780590541787"));
		assertFalse(InputValidators.ISBN.validate("978-0-590-54178-8"));

		assertTrue(InputValidators.ISBN.validate("0-590-54178-1"));
		assertTrue(InputValidators.ISBN.validate("0 590 54178 1"));
		assertTrue(InputValidators.ISBN.validate("0590541781"));
		assertFalse(InputValidators.ISBN.validate("0-590-54178-5"));
	}

	@Test
	public void validateISBNCorrectLength() {
		InputValidators test = new InputValidators(); //to improve code coverage
		assertTrue(InputValidators.ISBN.validate("978-0-590-54178-7"));
		assertTrue(InputValidators.ISBN.validate("0-590-54178-1"));
		assertFalse(InputValidators.ISBN.validate("978-0-590-5418-7"));
		assertFalse(InputValidators.ISBN.validate("978"));
		assertFalse(InputValidators.ISBN.validate(""));
		assertFalse(InputValidators.ISBN.validate(null));
	}

	@Test
	public void validateISBNStartsWithCorrectPrefix() {
		assertTrue(InputValidators.ISBN.validate("978-0-590-54178-7"));

		assertTrue(InputValidators.ISBN.validate("979-0-590-54178-6"));

		assertFalse(InputValidators.ISBN.validate("988-0-590-54178-4"));
	}

	@Test
	public void validateISBNValidCharacters() {
		assertTrue(InputValidators.ISBN.validate("978-0-590-54178-7"));
		assertTrue(InputValidators.ISBN.validate("0-590-54179-x"));

		assertFalse(InputValidators.ISBN.validate("978-0-59j-54178-x"));
		assertFalse(InputValidators.ISBN.validate("0-590-5se79-x"));
		assertFalse(InputValidators.ISBN.validate("testtesttest1"));
	}

	@Test
	public void validateLinkRecognizesCorrectLinks() {
		assertTrue(InputValidators.LINK.validate("test.com"));
		assertTrue(InputValidators.LINK.validate("www.test.com"));
		assertTrue(InputValidators.LINK.validate("http://test.com/page/title"));
		assertTrue(InputValidators.LINK.validate("https://test.com"));
		assertTrue(InputValidators.LINK.validate("https://www.subdomain.test.com"));
		assertTrue(InputValidators.LINK.validate("subdomain.test.com/page/"));

		assertFalse(InputValidators.LINK.validate("ht/test.com"));
		assertFalse(InputValidators.LINK.validate("htt p://test.com"));
		assertFalse(InputValidators.LINK.validate("https:/.test.com"));
		assertFalse(InputValidators.LINK.validate("https//.test.com"));
		assertFalse(InputValidators.LINK.validate("https://.test.com"));
	}

	@Test
	public void validateLinkLengthTest() {
		assertFalse(InputValidators.LINK.validate("http://test.c"));
		assertFalse(InputValidators.LINK.validate(""));
	}
}
