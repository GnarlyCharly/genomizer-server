package database.testSuite.unitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import unused.FileValidator;

public class FileNameValidatorTest {

	private FileValidator validator;

	@Before
	public void setUp() {

		validator = new FileValidator();
	}

	@Test
	public void shouldReturnTrueOnAcceptedChars() {

		boolean isOk = false;
		isOk = validator.isNameOk("aaa");
		assertTrue(isOk);
	}


	@Test
	public void shouldReturnFalseOnUnacceptedChars() {

		boolean isOk = false;
		isOk = validator.isNameOk("?!//&/*\n");
		assertFalse(isOk);
	}
}