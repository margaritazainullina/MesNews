package ui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mesnews.db.NewsAbstractService;
import mesnews.db.NewsDBService;
import mesnews.db.NewsFileService;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialogs;

/**
 * FXML Controller class
 *
 * @author Margarita
 */
public class DataSourceChooserController implements Initializable {

    String pathToFile;

    NewsAbstractService service;
    @FXML
    public RadioButton dbRB;
    @FXML
    public RadioButton fileRB;
    @FXML
    public RadioButton openRB;
    @FXML
    public RadioButton createRB;
    @FXML
    public Button okButton;
    @FXML
    public ProgressIndicator indicator;
    @FXML
    public Pane filePane;
    @FXML
    public Pane openPane;
    @FXML
    public Pane createPane;
    @FXML
    public Label pathLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void connect(ActionEvent event) throws IOException, InterruptedException {
        indicator.setOpacity(1);
        //connect to db
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (dbRB.isSelected()) {
                    try {
                        NewsAbstractService.initWithConcreteService(NewsDBService.INSTANCE);
                        service = NewsAbstractService.INSTANCE;
                        NewsDBService.load();
                    } catch (Exception ex) {
                        Dialogs.create()
                                .owner(new Stage())
                                .title("Erreur")
                                .masthead("Erreur de connection de la source de donnees")
                                .message(ex.getLocalizedMessage())
                                .showError();

                        Logger.getLogger(DataSourceChooserController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        service.indexNews();
                    } catch (IOException ex) {

                        Dialogs.create()
                                .owner(new Stage())
                                .title("Erreur")
                                .masthead("Erreur d'indexion des nouvelles")
                                .message(ex.getLocalizedMessage())
                                .showError();

                        Logger.getLogger(DataSourceChooserController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } //connect to file
                else {
                    try {
                        NewsAbstractService.initWithConcreteService(NewsFileService.INSTANCE);
                        service = NewsAbstractService.INSTANCE;
                        if (openRB.isSelected()) {
                            NewsFileService.loadFromFile(pathToFile);
                            service.indexNews();
                        } else {
                            //just create a new file
                            File f = new File(pathToFile);
                            NewsFileService.INSTANCE.setNews(new TreeSet<>());
                            f.createNewFile();
                        }
                    } catch (Exception ex) {
                        Dialogs.create()
                                .owner(new Stage())
                                .title("Erreur")
                                .masthead("Erreur de ouvration de fichier")
                                .message(ex.getLocalizedMessage())
                                .showError();
                        Logger.getLogger(DataSourceChooserController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        }
        );

        t.start();
        t.join();

        Stage stage = (Stage) dbRB.getScene().getWindow();
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.DECORATED);
        newStage.setTitle("Nouvelles");
        try {
            newStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/MainWindow.fxml"))));
        } catch (IOException ex) {
            Logger.getLogger(DataSourceChooserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        newStage.show();
        stage.close();
    }

    @FXML
    private void toggleDBFile(ActionEvent event) {
        boolean show = fileRB.isSelected();
        for (Node n : filePane.getChildren()) {
            if (n instanceof Pane) {
                if (!show) {
                    ((Pane) n).setDisable(true);
                } else {
                    ((Pane) n).setDisable(false);
                }
            }
        }
    }

    @FXML
    private void toggleOpenSave(ActionEvent event) {
        fileRB.setSelected(true);
        boolean show = openRB.isSelected();
        for (Node n : openPane.getChildren()) {
            if (n instanceof Button) {
                if (!show) {
                    ((Button) n).setDisable(true);
                } else {
                    ((Button) n).setDisable(false);
                }
            }
        }
        for (Node n : createPane.getChildren()) {
            if (n instanceof Button) {
                if (show) {
                    ((Button) n).setDisable(true);
                } else {
                    ((Button) n).setDisable(false);
                }
            }
            if (n instanceof Label) {
                if (show) {
                    ((Label) n).setDisable(true);
                } else {
                    ((Label) n).setDisable(false);
                }
            }
            if (n instanceof TextField) {
                if (show) {
                    ((TextField) n).setDisable(true);
                } else {
                    ((TextField) n).setDisable(false);
                }
            }

        }
    }

    @FXML
    private void openButton(ActionEvent event) {
        //Set extension filter
        FileChooser fileChooser = new FileChooser();

        //Show open file dialog
        File f = fileChooser.showOpenDialog(null);
        pathToFile = f.getAbsolutePath();
        pathLabel.setText("Ficier: " + pathToFile);
        openRB.setSelected(true);
    }

    @FXML
    private void createButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            pathToFile = file.getAbsolutePath();
        }
        pathLabel.setText("Ficier: " + pathToFile);
        createRB.setSelected(true);
    }
}
