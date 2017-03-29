package practica7;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eric
 */
public class Practica7 {

    public static void main(String[] args) throws IOException {
        String server = "ftp.uv.es";
        String server2 = "ftp.m06uf3.esy.es";
        int port = 21;
        String user = "anonymous";
        String pass = "anonymous";
        String user2 = "u454499351.user";
        String pass2 = "user1234";
        String ruta = "prova_Eric.txt"; //Modificar NOM amb el vostre nom
        //PRIEMRA PROVA
        ClientFTP client = new ClientFTP(server, port, user, pass);
        for (String arg : client.listar()) {
            System.out.println(arg);
        }
        client.cerrarSesion();
        client.desconectarServidor();

        //SEGONA PROVA
        ClientFTP client2 = new ClientFTP(server2, port, user2, pass2);

        client2.setDirectorio("pub");
        for (String arg : client2.listar()) {
            System.out.println(arg);
        }

        System.out.println("Pujant fitxer " + ruta);
        client2.activarEnvio();
        client2.enviarFichero(ruta);

        for (String arg : client2.listar()) {
            System.out.println(arg);
        }
        
        String descargar = "BaixarFitxer.txt";
        client2.descarregaFitxer(descargar);
       
        client2.cerrarSesion();
        client2.desconectarServidor();
    }
}
