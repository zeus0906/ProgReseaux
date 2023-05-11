package Server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat extends Thread {
    private int nbrClient;
    private List<Conversation> clients = new ArrayList<ServerChat.Conversation>();
    public static void main(String[] args) {
       new ServerChat().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Connexion au serveur ....");
            while (true){
                Socket socket = ss.accept();
                ++nbrClient;
                Conversation conversation = new Conversation(socket,nbrClient);
                clients.add(conversation);
                conversation.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Conversation extends Thread {
        protected Socket socket;
        protected int nbrClient;

        public Conversation(Socket s, int num){
            this.socket= s;
            this.nbrClient= num;
        }


        public void BroadcastMessage(String message, Socket socketClient, int numClient) throws IOException {
            for(Conversation client : clients){
                if(client.socket != socketClient) {
                    if(client.nbrClient == numClient || numClient == -1){
                        PrintWriter print = new PrintWriter(client.socket.getOutputStream(), true);
                        print.println(message);
                    }
                }
            }

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
                    if(req.contains("=>")) {
                        String[] requestParams = req.split("=>");
                        if(requestParams.length ==2);
                        String message = requestParams[1];
                        String numClient = requestParams[0];
                        BroadcastMessage(message,socket, Integer.parseInt(numClient));
                    }else {
                        BroadcastMessage(req,socket,-1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
