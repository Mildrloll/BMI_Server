import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Application {
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane paneForText = new BorderPane();
        paneForText.setPadding(new Insets(5, 5, 5, 5));
        paneForText.setStyle("-fx-border-color: green");
        paneForText.setLeft(new Label("Enter a height: "));
        //paneForText.setLeft(new Label("Enter a weight: "));
        TextField tf = new TextField();
        TextField tf2 = new TextField();
        tf.setPromptText("Enter height");
        tf2.setPromptText("Enter weight");
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        tf2.setAlignment(Pos.BOTTOM_RIGHT);
        paneForText.setCenter(tf);
        paneForText.setBottom(tf2);
        BorderPane mainPane = new BorderPane();
        BorderPane secPane = new BorderPane();
        TextArea ta = new TextArea();
        TextArea ta2 = new TextArea();
        mainPane.setCenter(new ScrollPane(ta));
        secPane.setCenter(new ScrollPane(ta2));
        mainPane.setTop(paneForText);
        //secPane.setTop(paneForWeight);
        Scene scene = new Scene(mainPane, 450, 200);
        //Scene scene1 = new Scene(secPane, 450, 200);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        //primaryStage.setScene(scene1);
        primaryStage.show();
        tf.setOnAction(e -> {
            try {
                double height = Double.parseDouble(tf.getText().trim());
                toServer.writeDouble(height);
                toServer.flush();
                ta.appendText("Height is " + height + "\n");
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        tf2.setOnAction(e -> {
            try {
                double weight = Double.parseDouble(tf2.getText().trim());
                toServer.writeDouble(weight);
                toServer.flush();
                double bmi = fromServer.readDouble();
                ta.appendText("Weight is " + weight + "\n");
                ta.appendText("BMI received from server is " + bmi + '\n');
            } catch (IOException ex) {
                System.err.println(ex);
            }
        });
        try {
            Socket socket = new Socket("localhost", 8000);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ta.appendText(ex.toString() + '\n');
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
