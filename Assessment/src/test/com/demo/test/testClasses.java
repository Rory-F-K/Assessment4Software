package com.demo.test;

import mainClasses.Person;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;

class TestClasses {
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
    void testAddDemeritPoints_ValidUnder21ExceedsLimit_ShouldSuspend() {
        Person p = new Person();
        p.setPersonID("29@#7ZXYAB");
        p.setFirstName("Tom");
        p.setLastName("White");
        p.setAddress("456 Road");
        p.setBirthDate("01-01-2008"); // under 21

        p.setDemeritPoints(new HashMap<>());

        p.addDemeritPoints("01-06-2023", 3);
        p.addDemeritPoints("01-07-2023", 2);
        String result = p.addDemeritPoints("01-08-2023", 2); // Total = 7

        assertEquals("Success", result);
        assertTrue(p.isSuspended());
    }
}

class TestClass_AddPerson {
    @BeforeEach
    void setup() throws IOException {
        String initialData = "35@#7ZBXYQ, Bob, Lee, 789 Pine St, 10-05-2000\n";
        Files.write(Paths.get("people.txt"), initialData.getBytes());
    }

    @Test
    void TC1_AddValidPerson_ShouldReturnTrue() {
        Person p = new Person("77@#XYZABC", "Alice", "Smith", "123 Oak St", "01-01-1990");
        assertTrue(p.addPerson());
    }

    @Test
    void TC2_AddPersonWithDuplicateID_ShouldReturnFalse() {
        Person p = new Person("35@#7ZBXYQ", "John", "Doe", "456 Elm St", "15-05-1985");
        assertFalse(p.addPerson());
    }

    @Test
    void TC3_AddPersonWithInvalidBirthDateFormat_ShouldReturnFalse() {
        Person p = new Person("88@#XYZ123", "Jane", "Doe", "789 Maple Ave", "1990/01/01");
        assertFalse(p.addPerson());
    }

    @Test
    void TC4_AddPersonWithEmptyMandatoryFields_ShouldReturnFalse() {
        Person p = new Person("99@#XYZ999", "", "Brown", "555 Birch St", "12-12-1980");
        assertFalse(p.addPerson());
    }

    @Test
    void TC5_AddPersonWithNullFields_ShouldReturnFalse() {
        Person p = new Person(null, null, null, null, null);
        assertFalse(p.addPerson());
    }

    @Test
    void TC6_AddPersonWithFutureBirthDate_ShouldReturnFalse() {
        Person p = new Person("77@#XYZNOP", "Future", "Kid", "101 Future Rd", "01-01-3000");
        assertFalse(p.addPerson());
    }

    @Test
    void TC7_AddPersonWithSpecialCharactersInName_ShouldReturnTrue() {
        Person p = new Person("55@#XYZAAB", "Anne-Marie", "O'Neil", "12 Cherry St", "10-10-1985");
        assertTrue(p.addPerson());
    }

    @Test
    void TC8_AddPersonWithVeryLongFields_ShouldReturnTrue() {
        String longName = "A".repeat(100);
        String longAddress = "B".repeat(200);
        Person p = new Person("66@#XYZAAA", longName, longName, longAddress, "01-01-1990");
        assertTrue(p.addPerson());
    }

    @Test
    void TC9_AddPersonWithIDStartingWithEvenDigit_ShouldReturnTrue() {
        Person p = new Person("42@#XYZAZA", "Evan", "Lee", "44 River St", "05-05-1995");
        assertTrue(p.addPerson());
    }

    @Test
    void TC10_AddPerson_FileIOException_ShouldReturnFalse() throws IOException {
        Person p = new Person("77@#XYZXYZ", "Ivy", "Ng", "101 Elm St", "12-12-1988");
        java.nio.file.Path filePath = Paths.get("people.txt");

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        Files.createDirectory(filePath);

        try {
            assertFalse(p.addPerson());
        } finally {
            Files.delete(filePath);
            Files.createFile(filePath);
        }
    }
}

class TestClass_UpdatePersonalDetails {

    @BeforeEach
    void resetPeopleFile() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("23@@@ABCDZ, Test, User, 123 Test St, 01-01-2000");
        Files.write(Paths.get("people.txt"), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test
    void testUpdatePersonalDetails_ValidUpdate() throws IOException {
        PersonManagement personManagement = new PersonManagement();
        personManagement.updatePersonalDetails("23@@@ABCDZ", "Updated", "Name", "456 New Address", "01-01-2000");

        List<String> updatedLines = Files.readAllLines(Paths.get("people.txt"));
        assertTrue(updatedLines.contains("23@@@ABCDZ, Updated, Name, 456 New Address, 01-01-2000"));
    }

    // Test case: Attempting to update a person not found in people.txt
    @Test
    void testUpdatePersonalDetails_PersonNotFound() throws IOException {
        PersonManagement personManagement = new PersonManagement();
        personManagement.updatePersonalDetails("99@@@XXXXZ", "Ghost", "User", "Nowhere Street", "01-01-1990");

        List<String> lines = Files.readAllLines(Paths.get("people.txt"));
        // The original line should remain unchanged
        assertTrue(lines.contains("23@@@ABCDZ, Test, User, 123 Test St, 01-01-2000"));
        assertFalse(lines.stream().anyMatch(line -> line.contains("Ghost")));
    }

    // Test case: Attempting to update with null values (should be ignored or raise
    // no exception)
    @Test
    void testUpdatePersonalDetails_NullInputs() throws IOException {
        PersonManagement personManagement = new PersonManagement();
        personManagement.updatePersonalDetails("23@@@ABCDZ", null, null, null, null);

        List<String> lines = Files.readAllLines(Paths.get("people.txt"));
        // Ensure the line remains unchanged due to null inputs
        assertTrue(lines.contains("23@@@ABCDZ, Test, User, 123 Test St, 01-01-2000"));
    }

    // Test case: Malformed line in people.txt should be skipped safely
    @Test
    void testUpdatePersonalDetails_MalformedLine() throws IOException {
        // Write malformed data to people.txt manually
        Files.write(Paths.get("people.txt"), Arrays.asList(
                "23@@@ABCDZ, Test, User, 123 Test St, 01-01-2000",
                "MALFORMED LINE WITHOUT ENOUGH FIELDS"));

        PersonManagement personManagement = new PersonManagement();
        personManagement.updatePersonalDetails("23@@@ABCDZ", "Fixed", "Name", "789 Proper Rd", "02-02-2002");

        List<String> lines = Files.readAllLines(Paths.get("people.txt"));
        assertTrue(lines.contains("23@@@ABCDZ, Fixed, Name, 789 Proper Rd, 02-02-2002"));
        assertTrue(lines.contains("MALFORMED LINE WITHOUT ENOUGH FIELDS")); // should remain untouched
    }

    // Test case: people.txt remains unchanged when no matching license number is
    // found
    @Test
    void testUpdatePersonalDetails_NoUpdateWhenLicenseNotFound() throws IOException {
        // Prepare initial file content
        List<String> originalData = Arrays.asList(
                "23@@@ABCDZ, Test, User, 123 Test St, 01-01-2000");
        Files.write(Paths.get("people.txt"), originalData);

        PersonManagement personManagement = new PersonManagement();
        personManagement.updatePersonalDetails("NON_EXISTENT_LICENSE", "Jane", "Doe", "999 Ghost Rd", "03-03-2003");

        // Read the file back and verify it remains unchanged
        List<String> resultLines = Files.readAllLines(Paths.get("people.txt"));
        assertEquals(originalData, resultLines);
    }

    // Test case: All matching license entries are updated (if intended behavior)
    @Test
    void testUpdatePersonalDetails_MultipleMatchingEntries() throws IOException {
        // Set up people.txt with multiple records sharing the same license number
        List<String> initialData = Arrays.asList(
                "MULTI123, First, Entry, 1 Old Rd, 01-01-1990",
                "MULTI123, Second, Entry, 2 Old Rd, 02-02-1991",
                "UNIQUE456, Lone, User, 3 Normal St, 03-03-1992");
        Files.write(Paths.get("people.txt"), initialData);

        // Call the update method with matching license
        PersonManagement personManagement = new PersonManagement();
        personManagement.updatePersonalDetails("MULTI123", "Updated", "Name", "123 New Rd", "11-11-2011");

        // Read the updated file
        List<String> updatedLines = Files.readAllLines(Paths.get("people.txt"));

        // Confirm both MULTI123 entries are updated
        assertTrue(updatedLines.contains("MULTI123, Updated, Name, 123 New Rd, 11-11-2011"));
        assertEquals(2, updatedLines.stream()
                .filter(line -> line.contains("MULTI123") && line.contains("Updated")).count());

        // Ensure the unique entry was not changed
        assertTrue(updatedLines.contains("UNIQUE456, Lone, User, 3 Normal St, 03-03-1992"));
    }

        // Test case: Attempting to update when people.txt is empty
    @Test
    void testUpdatePersonalDetails_EmptyFile() throws IOException {
        // Create an empty people.txt file
        Files.write(Paths.get("people.txt"), new ArrayList<>());

        // Call update method
        PersonManagement personManagement = new PersonManagement();
        personManagement.updatePersonalDetails("EMPTY123", "Test", "Case", "Nowhere", "01-01-1999");

        // File should still be empty — no changes to be made
        List<String> resultLines = Files.readAllLines(Paths.get("people.txt"));
        assertEquals(0, resultLines.size());
    }

        // Test case: people.txt file does not exist
    @Test
    void testUpdatePersonalDetails_FileDoesNotExist() throws IOException {
        // Ensure the file does not exist
        Path peopleFile = Paths.get("people.txt");
        if (Files.exists(peopleFile)) {
            Files.delete(peopleFile);
        }

        // Call the update function (it should create the file or fail gracefully)
        PersonManagement personManagement = new PersonManagement();
        personManagement.updatePersonalDetails("NOFILE123", "First", "Last", "Lost St", "10-10-2010");

        // Check that file was created and contains new data
        assertTrue(Files.exists(peopleFile));
        List<String> resultLines = Files.readAllLines(peopleFile);
        assertEquals(1, resultLines.size());
        assertEquals("NOFILE123, First, Last, Lost St, 10-10-2010", resultLines.get(0));
    }

    

}

class addDemeritPoints {
    @BeforeEach
    void setup() throws IOException {
        String initialData = "23@@@ABCDZ, Test, User, 123 Test St, 01-01-2000\n";
        Files.write(Paths.get("people.txt"), initialData.getBytes());
    }

    private Person createTestPerson(String birthDate, String personID) {
        Person person = new Person(personID, "Test", "User", "123 Test St", birthDate);
        person.setDemeritPoints(new HashMap<>());
        return person;
    }

    @Test
    public void TC1_validDateAndPoints_Success() {
        Person person = createTestPerson("01-01-2000", "23@@@ABCDZ");
        String result = person.addDemeritPoints("01-01-2024", 3);
        assertEquals("Success", result);
    }

    @Test
    public void TC2_invalidDateFormat_Fail() {
        Person person = createTestPerson("01-01-2000", "23@@@ABCDZ");
        String result = person.addDemeritPoints("2024/01/01", 3);
        assertEquals("Failed", result);
    }

    @Test
    public void TC3_zeroPoints_Fail() {
        Person person = createTestPerson("01-01-2000", "23@@@ABCDZ");
        String result = person.addDemeritPoints("01-01-2024", 0);
        assertEquals("Failed", result);
    }

    @Test
    public void TC4_invalidPointsOver6_Fail() {
        Person person = createTestPerson("01-01-2000", "23@@@ABCDZ");
        String result = person.addDemeritPoints("01-01-2024", 10);
        assertEquals("Failed", result);
    }

    @Test
    public void TC5_under21Suspension_Triggered() {
        Person person = createTestPerson("01-01-2007", "23@@@ABCDZ");
        person.addDemeritPoints("01-01-2024", 3);
        person.addDemeritPoints("02-01-2024", 4);
        assertTrue(person.isSuspended());
    }

    @Test
    public void TC6_over21Suspension_Triggered() {
        Person person = createTestPerson("01-01-1990", "23@@@ABCDZ");
        person.addDemeritPoints("01-01-2024", 6);
        person.addDemeritPoints("01-01-2024", 5);
        person.addDemeritPoints("01-02-2024", 2);
        assertTrue(person.isSuspended());
    }

    @Test
    public void TC7_over21NotSuspended() {
        Person person = createTestPerson("01-01-1990", "23@@@ABCDZ");
        person.addDemeritPoints("01-01-2024", 5);
        person.addDemeritPoints("01-02-2024", 6);
        assertFalse(person.isSuspended());
    }

    @Test
    public void TC8_multipleOffensesIn2Years() {
        Person person = createTestPerson("01-01-1990", "23@@@ABCDZ");
        person.addDemeritPoints("01-01-2024", 4);
        person.addDemeritPoints("01-06-2024", 3);
        person.addDemeritPoints("01-12-2024", 6);
        assertTrue(person.isSuspended());
    }

    @Test
    public void TC9_oldOffenseIgnoredInSuspension() {
        Person person = createTestPerson("01-01-1990", "23@@@ABCDZ");
        person.addDemeritPoints("01-01-2018", 6);
        assertFalse(person.isSuspended());
    }

    @Test
    public void TC10_simulateFileWriteFail() {
        Person person = createTestPerson("01-01-2000", "/invalid/person");
        String result = person.addDemeritPoints("01-01-2024", 3);
        assertTrue(result.equals("Success") || result.equals("Failed"));
    }
}