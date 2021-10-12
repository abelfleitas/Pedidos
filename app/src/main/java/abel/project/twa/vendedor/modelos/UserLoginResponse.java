package abel.project.twa.vendedor.modelos;

public class UserLoginResponse {

    private Login data;

    public UserLoginResponse(Login pdata) {
        this.data = pdata;
    }

    public Login getObjectLogin() {
        return data;
    }

}
