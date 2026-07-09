package pojo;

public class LoginRequestPojo {
    private String login;
    private String password;

    public LoginRequestPojo() {
    }

    public LoginRequestPojo(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}