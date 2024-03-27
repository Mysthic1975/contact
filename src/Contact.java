public class Contact {
    protected String firstName;
    protected String lastName;
    protected String street;
    protected String city;
    protected String postalCode;
    protected String phoneNumber;

    public Contact(String firstName, String lastName, String street, String city, String postalCode, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void display() {
        System.out.printf(
                "First Name: %s\nLast Name: %s\nStreet: %s\nCity: %s\nPostal Code: %s\nPhone Number: %s\n%n",
                firstName, lastName, street, city, postalCode, phoneNumber
        );
    }
}
