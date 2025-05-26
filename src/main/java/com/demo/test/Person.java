package com.demo.test;
import java.util.HashMap;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.time.Month;
import java.time.LocalDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Person {
	private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthDate;
    private HashMap<Date, Integer> demeritPoints;
    private boolean isSuspended;

    public boolean addPerson() {
        // condition 1: personID should be eactly 10 characters long,
        // and first 2 characters between 2 and 9
        if ((personID.length() != 10) || (personID.charAt(0) < 2) || (personID.charAt(0) > 9)
                || (personID.charAt(1) < 2) && (personID.charAt(1) > 9)) {
            return false;
        }
        // Loop from index 3 to 8, counting special characters, if count <2, return
        // false
        int specialCount = 0;
        for (int i = 3; i < 8; i++) {
            if ((!Character.isLetterOrDigit(personID.charAt(i))) && (!Character.isWhitespace(personID.charAt(i)))) {
                specialCount += 1;
            }
        }
        if (specialCount < 2) {
            return false;
        }
        // Then use .isLowerCase() and .isUpperCase on last 2 chars to complete
        // condition 1 check.
        if ((!Character.isUpperCase(personID.charAt(8))) ||
                (!Character.isUpperCase(personID.charAt(8)))) {
            return false;
        }
        // If it gets this far, return trwd and write to txt file
        String filePath = "people.txt";
        String newPerson = "" + personID + ", " + firstName + ", " + lastName + ", " + address + ", " + birthDate
                + "\n";
        // Create new writer object
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(newPerson);
            System.out.println("Text appended successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return true;
    }

    public boolean updatePersonalDetails(
            String newID,
            String newFirstName,
            String newLastName,
            String newAddress,
            String newBirthDate) {
        // Condition 1: If a person is under 18 their address cannotn be changed
        // Get year from date
        LocalDate birthdayDate = LocalDate.parse(birthDate);
        int birthYear = birthdayDate.getYear();

        // Calculste current year
        int currentYear = 2025;

        // Check >=18
        if ((currentYear - birthYear) < 18) {
            newFirstName=this.firstName;
        }

        // Condition 2: If a person's birthday is going to be changed,
        // then no other personal detail (i.e, person's ID, firstName, lastName,
        // address)
        // can be changed
        if (newBirthDate != this.birthDate) {
            newFirstName=this.firstName;
            newLastName=this.lastName;
            newAddress=this.address;
        }

        //Condition 3: If the first character/digit of a person's ID is an even number, 
        //then their ID cannot be changed.
        //retrieve first digit as int
        char firstChar=this.personID.charAt(0);
        int firstDigit=firstChar-'0';

        if(firstDigit%2==0){
            newID=this.personID;
        }

        // If all conditions pass, return true and assign all the new updated details
        this.firstName=newFirstName;
        this.lastName=newLastName;
        this.address=newAddress;
        this.birthDate=newBirthDate;
        return true;

    }

    public String addDemeritPoints(){
        //comndition 3 - If the person is under 21, the isSuspended variable should be set to true 
        //if the total demerit points within two years exceed 6.
        // If the person is over 21, the isSuspended variable should be set to true 
        //if the total demerit points within two years exceed 12.

        //Get birthYear
        LocalDate birthdayDate = LocalDate.parse(birthDate);
        int birthYear = birthdayDate.getYear();

        //current year
        int currentYear=2025;

        //calculate total demerit points within last 2 years
        //counter to track demerits
        int demerits=0; 

        //get current time as date object
        LocalDate currentDate=LocalDate.now();

        //Loop through hashmap demeritPoints, adding to counter if date in 2 year range 
        for(Date key: demeritPoints.keySet()){
            long timeDifferenceMilli=currentDate.getTime()-
        }

        if((currentYear-birthYear)<21){

        }

        return "Success";
    }
}
}
