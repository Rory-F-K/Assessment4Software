package com.demo.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
class TestClass1 {
	@BeforeEach
	void setup() throws IOException {
		String initialData = "23#A%*ZBXY, John, Doe, 123 Main St, 15-06-2010\n";
		Files.write(Paths.get("people.txt"), initialData.getBytes());
	}

	@Test
	void testAddPerson_ValidData_ShouldReturnTrue() {
		Person p = new Person();
		p.setPersonID("23@#45XYZW");
		p.setFirstName("John");
		p.setLastName("Doe");
		p.setAddress("123 Street");
		p.setBirthDate("15-08-2000");

		boolean result = p.addPerson();
		assertTrue(result);
	}

	@Test
	void testUpdatePersonalDetails_Under18_ShouldNotUpdateAddress() {
		Person p = new Person();
		p.setPersonID("25!@9ZABCD");
		p.setFirstName("Alice");
		p.setLastName("Smith");
		p.setAddress("Old Address");
		p.setBirthDate("15-06-2010"); // age < 18

		boolean updated = p.updatePersonalDetails(
				"25!@9ZABCD", "Alice", "Smith", "New Address", "15-06-2010");

		assertFalse(updated);  // Expect false because address change not allowed
		assertEquals("Old Address", p.getAddress()); // Address remains unchanged
	}


	@Test
	void testAddDemeritPoints_ValidUnder21ExceedsLimit_ShouldSuspend() {
		Person p = new Person();
		p.setPersonID("29@#7ZXYAB");
		p.setFirstName("Tom");
		p.setLastName("White");
		p.setAddress("456 Road");
		p.setBirthDate("01-01-2008"); // under 21

		p.setDemeritPoints(new HashMap<>());

		// Add 3 offenses within 2 years
		p.addDemeritPoints("01-06-2023", 3);
		p.addDemeritPoints("01-07-2023", 2);
		String result = p.addDemeritPoints("01-08-2023", 2); // Total = 7

		assertEquals("Success", result);
		assertTrue(p.isSuspended());
	}
}
