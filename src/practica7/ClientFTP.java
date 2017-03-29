package practica7;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Eric
 */
public class ClientFTP {

    private String server;
    private int port;
    private String user;
    private String pass;
    private FTPClient ftp;
    private boolean login;

    public ClientFTP(String server, int port, String user, String pass) throws IOException {
        this.server = server;
        this.port = port;
        this.user = user;
        this.pass = pass;

        ftp = new FTPClient();

        conectar();

    }

    private void conectar() throws IOException {
        //realitzar conexió amb connect(), ip i port
        ftp.connect(server, port);
        //realitzar login amb user i password.
        this.login = ftp.login(user, pass);
        //Comprobació de connexió amb valor de retorn de login
        if (login) {
            System.out.println("Connexió realitzada correctament!");
        } else {
            System.out.println("No s'ha pogut conectar... revisa la configuració!");
        }

        //Comprobació de valor de connexió amb els metode getReplyCode() i isPositiveCompletion(reply)
        if (ftp.isConnected()) {
            System.out.println("Valor de la conexion: " + ftp.getReplyCode());
        }
    }

    //cambiar a directori rebut per parametre amb changeWorkingDirectory()
    public void setDirectorio(String directorio) throws IOException {
        ftp.changeWorkingDirectory(directorio);
    }

    //obtenir el llistat de fitxers i directoris amb listFiles() i printWorkingDirectory()
    public List<String> listar() throws IOException {
        List<String> lista = new ArrayList<>(); //Creem un arrayList
        if (this.login) { //Si s'ha fet el Login
            System.out.println("Listando ficheros....");
            //Recorrem els fitxers i els anem afegint a la llista
            for (FTPFile fitxer : ftp.listFiles()) {
                lista.add(fitxer.getName());
            }
        } else {
            System.out.println("No logeat...");
        }
        return lista;
    }

    //activar enviamente en mode binari amb setFileType()
    public void activarEnvio() throws IOException {
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
    }

    //pujar fitxer al servidor. BufferedInputStream, FileInputStream, enterLocalPassiveMode() i storeFile()
    public void enviarFichero(String ruta) throws FileNotFoundException, IOException {
        //Posem el ftp en passiu
        ftp.enterLocalPassiveMode();
        //I pujem el fitxer
        FileInputStream file = new FileInputStream(ruta);
        BufferedInputStream buffer = new BufferedInputStream(file);
        ftp.storeFile(ruta, buffer);
    }

    //tancar la sessió
    public void cerrarSesion() throws IOException {
        ftp.logout();
    }

    //desconectar del servidor
    public void desconectarServidor() throws IOException {
        ftp.disconnect();
    }
    /**
     * Metode que utilitzem per descarregar un fitxer del servidor FTP
     * @param nom 
     */
    public void descarregaFitxer(String nom) {
        try {
            //Seteem el directori
            this.setDirectorio("/priv");
            //Busquem tots els fitxers dins del directorio del servidor
            for (FTPFile file : ftp.listFiles()) {
                //Si el fitxer es igual al nom llavors
                if(file.getName().equals(nom)){
                    //Posem el server en format passiu
                    ftp.enterLocalPassiveMode();   
                    //Creem un fitxer que sera igual al fitxer que busquem
                    File fitxer = new File(file.getName());
                    //Fem l'output per obrir conexio
                    FileOutputStream fos = new FileOutputStream(fitxer);
                    OutputStream os = new BufferedOutputStream(fos);
                    //Recbem els fitxer del directori.
                    ftp.retrieveFile(nom, os);
                    os.close();
                    System.out.println("Descarrega del fitxer: " + nom);
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ClientFTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
