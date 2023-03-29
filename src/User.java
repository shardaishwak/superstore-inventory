import java.io.FileWriter;
import java.util.ArrayList;

public class User {
    private String ID;
    private String name;
    private String email;
    private String password;
    private String role;

    public User(String ID, String name, String email, String password, String role) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;

        this.role = role;
    }

    public User(String name, String email, String password, String role) {
        // remove space
        this(Utilities.generateUUID(), name, email, password, role);
    }


    // Connected to Authentication
    public String toString() {
        return this.ID+"~"+this.name+"~"+this.email+"~"+this.role;

    }
    public String toStringWithPassword() {
        return this.ID+"~"+this.name+"~"+this.email+"~"+this.password+"~"+this.role;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }















    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return this.role;
    }
}
