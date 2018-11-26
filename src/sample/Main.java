package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();

    @Override
    public void start(final Stage stage) {
        ImageDatabase imageDatabase=ImageDatabase.getInstance();
        stage.setTitle("Pineapple process image");

        final Pane rootGroup = new VBox(0);

        final FileChooser fileChooser = new FileChooser();

        final javafx.scene.control.Button openButton = new Button("Select Image:");
        final Button processButton = new Button("Transform Image");

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File inputFile = fileChooser.showOpenDialog(stage);
                        if (inputFile != null) {
                            try {

                                BufferedImage bufferedImage=ImageIO.read(inputFile);
                                Image image= SwingFXUtils.toFXImage(bufferedImage, null);
                                ImageView imageView = new ImageView();
                                imageView.setImage(image);
                                rootGroup.getChildren().add(imageView);
                                InputImage inputImage = new InputImage();
                                inputImage.setBufferedImage(bufferedImage);
                                BufferedImage transformedImage=inputImage.transformInputImage();
                                Image imageTrans =SwingFXUtils.toFXImage(transformedImage,null);

                                ImageView secondview =new ImageView();
                                secondview.setImage(imageTrans);
                                rootGroup.getChildren().add(secondview);

                                File saveFile =new File(inputFile.getParent()+"/out_"+inputFile.getName());
                                ImageIO.write(transformedImage,"jpg",saveFile);


                            } catch (IOException ex) {
                                System.out.println(ex.getLocalizedMessage());
                            }
                        }
                    }
                });


        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openButton, 0, 1);

        inputGridPane.setHgap(0);
        inputGridPane.setVgap(0);
        inputGridPane.getChildren().addAll(openButton);


        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPrefWidth(600);
        rootGroup.setPrefHeight(800);
        rootGroup.setPadding(new Insets(10,300,300,10));

        stage.setScene(new Scene(rootGroup));
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);

        System.exit(0);

    }
}
