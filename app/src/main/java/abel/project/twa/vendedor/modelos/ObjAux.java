package abel.project.twa.vendedor.modelos;

public class ObjAux {

    String registrado;
    String message;

    public ObjAux(String respuesta, String mensaje) {
        this.registrado = respuesta;
        this.message = mensaje;
    }

    public String getRespuesta() {
        return registrado;
    }

    public String getMensaje() {
        return message;
    }
}


