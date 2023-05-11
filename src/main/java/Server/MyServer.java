package Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(1234);
        System.out.println("J'attend une connexion....");
        Socket s = ss.accept();
        System.out.println("la connexion d'un client" +s.getRemoteSocketAddress());
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();
        System.out.println("j'atend que le client m'envoie une donnée");
        int nb = is.read();
        System.out.println("le nombre envoyé par le client est " +nb);
        int res = nb * 4;
        System.out.println("le resultat du calcul est " +res);
        os.write(res);
        s.close();
    }
}
