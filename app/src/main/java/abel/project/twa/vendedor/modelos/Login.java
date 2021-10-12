package abel.project.twa.vendedor.modelos;


public class Login {

    private String authenticated;
    private String message;

    public Login(String authenticated, String message) {
        this.authenticated = authenticated;
        this.message = message;
    }

    public String isLogged() {
        return authenticated;
    }

    public String getMsj() {
        return message;
    }
}
