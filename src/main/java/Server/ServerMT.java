package Server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT extends Thread {
    private int nbrClient;
    public static void main(String[] args) {
       new ServerMT().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Connexion au serveur ....");
            while (true){
                Socket socket = ss.accept();
                ++nbrClient;
                new Conversation(socket,nbrClient).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Conversation extends Thread {
        private Socket socket;
        private int nbrClient;

        public Conversation(Socket s, int num){
            this.socket= s;
            this.nbrClient= num;
        }

        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os,true);
                String IP = socket.getRemoteSocketAddress().toString();
                System.out.println("Connexion du client numero " +nbrClient+ " avec l'adresse " +IP);
                pw.println("Bienvenue Client numero " +nbrClient);

                while (true){
                    String req = br.readLine();
                    System.out.println("le client "+nbrClient+ "vous a envoyé la requete "+req);
                    pw.println(req.length());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
