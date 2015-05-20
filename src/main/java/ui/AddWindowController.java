/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import mesnews.dao.AuteurDao;
import mesnews.db.NewsAbstractService;
import mesnews.db.NewsDBService;
import mesnews.model.Article;
import mesnews.model.Auteur;
import mesnews.model.News;
import mesnews.model.Photo;

/**
 *
 * @author Margarita
 */
public class AddWindowController implements Initializable {

    private News news;
    private boolean isArticle;
    private File photo;
    private int width;
    private int height;
    private String format;
    private List<Auteur> auteurs = AuteurDao.listAuteurs();

    //elements
    //panes
    @FXML
    private Pane articlePane;
    @FXML
    private Pane existentAuthorPane;
    @FXML
    private Pane addAuthorPane;
    @FXML
    private Pane panePhoto;

    //general pane elements
    @FXML
    private TextField titleText;
    @FXML
    private TextField sourceText;
    @FXML
    private Label titleTextError;
    @FXML
    private Label sourceTextError;
    @FXML
    private DatePicker dateText;

    //author pane elements
    @FXML
    private ComboBox authorCombobox;
    @FXML
    private RadioButton existentAuthorRB;
    @FXML
    private Label nomLabel;
    @FXML
    private Label prenomLabel;

    //article pane elements
    @FXML
    private RadioButton articleRB;
    @FXML
    private TextArea contenuTextArea;
    @FXML
    private CheckBox electroniqueCheckBox;
    @FXML
    private Label infoLabel;

    //photo pane elements
    @FXML
    private CheckBox coloredCheckBox;

    @FXML
    private void addButtonAction(ActionEvent event) {
        if (isArticle) {
            news = new Article();
        } else {
            news = new Photo();
        }
        //general pane
        if (validateFields()) {
            String titre = titleText.getText();
            news.setTitre(titre);
            news.setDate(dateText.getValue());
            try {
                news.setSource(new URL(sourceText.getText()));
            } catch (MalformedURLException ex) {
                Logger.getLogger(AddWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }

            news.setTitre(titre);

            //author
            if (existentAuthorRB.isSelected()) {
                Auteur author = (Auteur) authorCombobox.getSelectionModel().getSelectedItem();
                news.addAuteur(author);
            } //add author
            else {
                Auteur newAuthor = new Auteur(0, nomLabel.getText(), prenomLabel.getText());
                AuteurDao.add(newAuthor);
            }

            if (isArticle) {
                ((Article) news).setContenu(contenuTextArea.getText());
                ((Article) news).setSiElectronique(electroniqueCheckBox.isSelected());
            } else {
                ((Photo) news).setSiColoree(coloredCheckBox.isSelected());
                ((Photo) news).setHauteur(height);
                ((Photo) news).setLargeur(width);
                ((Photo) news).setFormat(format);
                try {

                   /* photo = ((Photo) news).loadPhotoFromURL(news.getSource());
                    int i = 0;*/

                    ((Photo) news).setImage(photo);
                } catch (IOException ex) {
                    Logger.getLogger(AddWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //todo switch services file db
            NewsDBService.INSTANCE.ajouter(news);

        }
    }

    @FXML
    private void uploadFileAction(ActionEvent event) throws IOException {

        //Set extension filter
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        photo = fileChooser.showOpenDialog(null);

        BufferedImage bimg = ImageIO.read(photo);
        width = bimg.getWidth();
        height = bimg.getHeight();
        format = Files.probeContentType(photo.toPath());
        String info = "Format: " + format + ", " + width + "*" + height;
        infoLabel.setText(info);
    }

    @FXML
    private void toggleAuthorPane(ActionEvent event) {
        boolean show = existentAuthorRB.isSelected();

        for (Node n : addAuthorPane.getChildren()) {
            if (n instanceof TextField) {
                ((TextField) n).setDisable(show);
            }
            if (n instanceof Label) {
                if (show) {
                    ((Label) n).setOpacity(0.4);
                } else {
                    ((Label) n).setOpacity(1);
                }
            }
            if (n instanceof TextField) {
                ((TextField) n).setDisable(show);
            }

        }

        for (Node n : existentAuthorPane.getChildren()) {
            if (n instanceof TextField) {
                ((TextField) n).setDisable(!show);
            }
            if (n instanceof Label) {
                if (!show) {
                    ((Label) n).setOpacity(0.4);
                } else {
                    ((Label) n).setOpacity(1);
                }
            }
            if (n instanceof ComboBox) {
                ((ComboBox) n).setDisable(!show);
            }
        }
    }

    @FXML
    private void toggleNewsPane(ActionEvent event) {
        boolean show = articleRB.isSelected();
        articlePane.getChildren().stream().map((n) -> {
            if (n instanceof TextArea) {
                ((TextArea) n).setDisable(!show);
            }
            return n;
        }).map((n) -> {
            if (n instanceof Label) {
                if (!show) {
                    ((Label) n).setOpacity(0.4);
                } else {
                    ((Label) n).setOpacity(1);
                }
            }
            return n;
        }).map((n) -> {
            if (n instanceof Button) {
                ((Button) n).setDisable(!show);
            }
            return n;
        }).filter((n) -> (n instanceof CheckBox)).forEach((n) -> {
            ((CheckBox) n).setDisable(!show);
        });

        panePhoto.getChildren().stream().map((n) -> {
            if (n instanceof TextField) {
                ((TextField) n).setDisable(show);
            }
            return n;
        }).map((n) -> {
            if (n instanceof CheckBox) {
                ((CheckBox) n).setDisable(show);
            }
            return n;
        }).map((n) -> {
            if (n instanceof Label) {
                if (show) {
                    ((Label) n).setOpacity(0.4);
                } else {
                    ((Label) n).setOpacity(1);
                }
            }
            return n;
        }).filter((n) -> (n instanceof Button)).forEach((n) -> {
            ((Button) n).setDisable(show);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // load combobox entries
        auteurs = AuteurDao.listAuteurs();
        List<String> auteursToString = new ArrayList<>();
        auteurs.stream().forEach((a) -> {
            auteursToString.add(a.toString());
        });

        authorCombobox.getItems().addAll(auteursToString);
    }

    public boolean validateFields() {
        boolean isValid = true;
        //hide all errors
        titleTextError.setOpacity(0);
        sourceTextError.setOpacity(0);

        if (titleText.getText().trim().isEmpty()) {
            titleTextError.setOpacity(1);
            isValid = false;
        }
        try {
            URL url = new URL(sourceText.getText());
        } catch (MalformedURLException ex) {
            sourceTextError.setOpacity(1);
            isValid = false;
        }
        return isValid;
    }

}
