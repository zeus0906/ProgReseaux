package Server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class MyServerJeux extends Thread {
    private int nbrClient;
    private int nbrSecret;
    private boolean fin;
    private String gagnant;
    public static void main(String[] args) {
        new MyServerJeux().start();
    }

    @Override
    public void run() {
        try{
            ServerSocket ss = new ServerSocket(1235);
            nbrSecret = new Random().nextInt(1000);
            System.out.println("Connexion au serveur ....");
            while (true){
                Socket socket = ss.accept();
                ++nbrClient;
                new Dialoguer(socket,nbrClient).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Dialoguer extends Thread {
        private Socket socketClient;
        private int nbrClient;

        public Dialoguer(Socket s, int num){
            this.socketClient = s;
            this.nbrClient= num;
        }

        @Override
        public void run() {
            try {
                InputStream is = socketClient.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                OutputStream os = socketClient.getOutputStream();
                PrintWriter pw = new PrintWriter(os,true);
                String IP = socketClient.getRemoteSocketAddress().toString();
                System.out.println("Connexion du client numero " +nbrClient+ " avec l'adresse " +IP);
                pw.println("Bienvenue Client numero " +nbrClient);
                pw.println("Devinez le nombre secret.......");

                while (true){
                    String req = br.readLine();
                    int nombre = 0;
                    boolean correctFormatNumber = false;
                    try{
                        nombre = Integer.parseInt(req);
                        correctFormatNumber = true;
                    }catch (NumberFormatException e){
                        correctFormatNumber = false;
                    }

                    if(correctFormatNumber){
                        System.out.println("Le client " +nbrClient+ " a envoye la requete: " +nombre);
                        if (fin == false){
                            if (nombre>nbrSecret){
                                pw.println("Votre nombre est superieur au nombre secret");
                            }else if(nombre<nbrSecret){
                                pw.println("Votre nombre est inferieur au nombre secret");
                            }else{
                                pw.println("Bravo, vous avez gagnez");
                                gagnant = IP;
                                System.out.println("le gagnant du jeux est "+nbrClient+" avec l'adresse ip "+gagnant);
                                fin = true;
                            }
                        }else{
                            pw.println("le jeu est terminé et le gagnant est "+nbrClient+" avec l'adresse ip"+gagnant);
                        }
                    }else{
                        pw.println("le nombre envoyé n'est pas correct");
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
