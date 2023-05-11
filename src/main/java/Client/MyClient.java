package Client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) throws Exception {

        System.out.println("je me connecte au serveur");
        Socket socket = new Socket("localhost",1234);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        Scanner sc = new Scanner(System.in);
        System.out.println("veillez entrer un nombre : ");
        int nb = sc.nextInt();

        System.out.println("le nombre "+nb+" a été envoyer au serveur");
        os.write(nb);
        System.out.println("J'attend la reponse du serveur");
        int rep = is.read();
        System.out.println("le nombre envoyer par le serveur est " +rep);

    }
}
