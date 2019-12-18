package helpers;

public class User {
    private String firstname;
    private String secondname;
    private String email;
    private String login;
    private String password;

    public User(){}

    public User(String firstname, String secondname, String email, String login, String password) {
        this.firstname = firstname;
        this.secondname = secondname;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
