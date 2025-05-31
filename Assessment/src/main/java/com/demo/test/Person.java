package mainClasses;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthDate;
    private HashMap<Date, Integer> demeritPoints;
    private boolean isSuspended;

    //setters and getters
    // personID
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    // firstName
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // lastName
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // birthDate (in format dd-MM-yyyy)
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    // demeritPoints
    public HashMap<Date, Integer> getDemeritPoints() {
        return demeritPoints;
    }

    public void setDemeritPoints(HashMap<Date, Integer> demeritPoints) {
        this.demeritPoints = demeritPoints;
    }

    // isSuspended
    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }


    public boolean addPerson() {
        // condition 1: personID should be eactly 10 characters long,
        // and first 2 characters between 2 and 9
        if ((personID.length() != 10) || (personID.charAt(0) < '2') || (personID.charAt(0) > '9')
                || (personID.charAt(1) < '2' || personID.charAt(1) > '9')) {
            return false;
        }

        // Loop from index 3 to 8, counting special characters, if count <2, return
        // false
        int specialCount = 0;
        for (int i = 2; i <= 8; i++) {
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
                (!Character.isUpperCase(personID.charAt(9)))) {
            return false;
        }
        // If it gets this far, return trwd and write to txt file
        String filePath = "people.txt";
        String newPerson = personID + ", " + firstName + ", " + lastName + ", " + address + ", " + birthDate
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(this.birthDate, formatter);
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();

        if (age < 18 && !newAddress.equals(this.address)) {
            return false;
        }



        // Condition 2: If a person's birthday is going to be changed,
        // then no other personal detail (i.e, person's ID, firstName, lastName,
        // address)
        // can be changed
        if (!newBirthDate.equals(this.birthDate) &&
                (!newID.equals(this.personID) ||
                        !newFirstName.equals(this.firstName) ||
                        !newLastName.equals(this.lastName) ||
                        !newAddress.equals(this.address))) {
            return false;
        }

        //Condition 3: If the first character/digit of a person's ID is an even number,
        //then their ID cannot be changed.
        //retrieve first digit as int
        int firstDigit=this.personID.charAt(0) - '0';

        if(firstDigit%2==0 && !newID.equals(this.personID)){
            return false;
        }

        // If reach then all checks passed so update memory
        String oldID = this.personID;
        // store for file search

        this.personID = newID;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.address = newAddress;
        this.birthDate = newBirthDate;

        String updatedLine = String.join(", ",
                this.personID, this.firstName, this.lastName, this.address, this.birthDate);

        // pdate the person's line in people.txt according to original personID
        String filePath = "people.txt";
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length > 0 && parts[0].equals(oldID)) { //check against oldID
                    updatedLines.add(updatedLine);
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (Exception e) {
            System.err.println("Update failed: " + e.getMessage());
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String updated : updatedLines) {
                writer.write(updated);
                writer.newLine();
            }

        } catch (Exception e) {
            System.err.println("Update failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    public String addDemeritPoints(String offenseDateStr, int points){
        //comndition 3 - If the person is under 21, the isSuspended variable should be set to true
        //if the total demerit points within two years exceed 6.
        // If the person is over 21, the isSuspended variable should be set to true
        //if the total demerit points within two years exceed 12.
        /*
        Condition 1: The format of the date of the offense should follow the following format: DD-MM-YYYY. Example: 15-11-1990
        Condition 2: The demerit points must be a whole number between 1-6.
        Condition 3: If the person is under 21,
            the isSuspended variable should be set to true if the total demerit points within two years exceed 6.
            If the person is over 21, the isSuspended variable should be set to true if the total demerit points within two years exceed 12.
         */

        // cond1: Date format must be DD-MM-YYYY
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        format.setLenient(false);  // fomat cannot deviate from dd-MM-yyyy

        Date offenseDate;
        try {
            offenseDate = format.parse(offenseDateStr);
        } catch (ParseException e) {
            return "Failed"; // invalid date format
        }

        //cond2: demerit points must be a whole number between 1-6
        if (points < 1 || points > 6) {
            return "Failed";
        }
        // Add to in-memory map
        if (demeritPoints == null) {
            demeritPoints = new HashMap<>();
        }
        demeritPoints.put(offenseDate, points);

        //cond3: suspension checks
        LocalDate currentDate = LocalDate.now();
        LocalDate birthLocalDate;
        try {
            Date birth = format.parse(birthDate);
            birthLocalDate = birth.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            return "Failed"; // Ensures rightb irthday format
        }

        int age = currentDate.getYear() - birthLocalDate.getYear();
        if (currentDate.getDayOfYear() < birthLocalDate.getDayOfYear()) {
            age--; // if hasn't had birthday this year
        }

        // Add to in-memory map
        if (demeritPoints == null) {
            demeritPoints = new HashMap<>();
        }
        demeritPoints.put(offenseDate, points);

        // check demerPs in past 2 years
        int totalPointsInLast2Years = 0;
        for (Date date : demeritPoints.keySet()) {
            LocalDate offenseLocalDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (!offenseLocalDate.isBefore(currentDate.minusYears(2))) {
                totalPointsInLast2Years += demeritPoints.get(date);
            }
        }

        // Check suspension
        if ((age < 21 && totalPointsInLast2Years > 6) ||
                (age >= 21 && totalPointsInLast2Years > 12)) {
            isSuspended = true;
        }

        // Store demerit in TXT file
        String filePath = "demerits.txt";
        String record = personID + ", " + offenseDateStr + ", " + points + "\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(record);
            return "Success";
        } catch (IOException e) {
            System.err.println("Failed to write to demerits.txt: " + e.getMessage());
            return "Failed";
        }
    }
}