package Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientChat extends Application {

    PrintWriter printWriter;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage principaleFen) throws Exception {

        //Titre de notre Fenetre principale
        principaleFen.setTitle("Acceuil");

        //Element conteneur donnant la taille de notre
        BorderPane borderPane = new BorderPane();

        //Connexion au serveur : Bouton
        Label labelHost = new Label("Host :");
        TextField textFieldHost = new TextField("localhost");

        //Connexion au serveur PORT : Bouton
        Label labelPort = new Label("Port :");
        TextField textFieldPort = new TextField("1234");

        //Bouton Connecter
        Button buttonConnecter = new Button("Connecter");

        //Organiser les éléments de manière horizontale
        HBox hBox = new HBox();
        hBox.setSpacing(15);
        hBox.setPadding(new Insets(15));
        hBox.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET,null,null)));
        hBox.getChildren().addAll(labelHost,textFieldHost,labelPort,textFieldPort,buttonConnecter);

        borderPane.setTop(hBox);

        VBox vBox = new VBox();
        hBox.setSpacing(15);
        hBox.setPadding(new Insets(15));
        ObservableList<String> listModel = FXCollections.observableArrayList();

        //Création d'une Liste des message
        ListView<String> listView = new ListView<String>(listModel);
        borderPane.setCenter(vBox);
        vBox.getChildren().add(listView);

        Label labelMessage = new Label("Message : ");
        TextField textFieldMessage = new TextField();
        textFieldMessage.setPrefSize(600,80);
        Button buttonEnvoyer = new Button("Envoyer : ");

        HBox hbox2 = new HBox();
        hbox2.setSpacing(15);
        hbox2.setPadding(new Insets(15));
        hbox2.getChildren().addAll(labelMessage,textFieldMessage,buttonEnvoyer);
        borderPane.setBottom(hbox2);

        //Espace d'affichage
        Scene scene = new Scene(borderPane,900,600);
        principaleFen.setScene(scene);
        principaleFen.show();

        //Gérer les évenements de notre application
        buttonConnecter.setOnAction((evt)->{
            String host = textFieldHost.getText();
            String portText = textFieldPort.getText();
            int port = Integer.parseInt(portText);

            //Etablir une connexion avec le serveur
            try {
                Socket socket = new Socket(host,port);
                InputStream inputStream = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(isr);
                printWriter = new PrintWriter(socket.getOutputStream(),true);

                //Lancement d'une Thread pour attendre les réponse
                new Thread(() ->{
                    while (true){
                            try {
                                String reponse = bufferedReader.readLine();
                                Platform.runLater(() -> {
                                    listModel.add(reponse);
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        buttonEnvoyer.setOnAction((evt) ->{
            String message = textFieldMessage.getText();
            printWriter.println(message);
        });
    }

}
