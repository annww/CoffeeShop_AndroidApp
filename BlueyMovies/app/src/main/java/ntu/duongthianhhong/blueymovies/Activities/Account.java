package ntu.duongthianhhong.blueymovies.Activities;
public class Account {
    private String name;
    private String email;
    private String password;

    public Account() {
    }

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
