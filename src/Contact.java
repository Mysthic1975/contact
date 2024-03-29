public class Contact {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String street;
    protected String city;
    protected String postalCode;
    protected String phoneNumber;

    // Constructor used when creating a new contact (without id)
    public Contact(String firstName, String lastName, String street, String city, String postalCode, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    // Constructor used when retrieving a contact from the database (with id)
    public Contact(int id, String firstName, String lastName, String street, String city, String postalCode, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
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

    @SuppressWarnings("unused")
    public void display() {
        System.out.printf(
                "First Name: %s\nLast Name: %s\nStreet: %s\nCity: %s\nPostal Code: %s\nPhone Number: %s\n%n",
                firstName, lastName, street, city, postalCode, phoneNumber
        );
    }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
