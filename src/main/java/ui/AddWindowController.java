/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import mesnews.dao.PhotoDao;
import mesnews.db.NewsAbstractService;
import mesnews.model.Article;
import mesnews.model.Auteur;
import mesnews.model.News;
import mesnews.model.Photo;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Margarita
 */
public class AddWindowController implements Initializable {

    NewsAbstractService service = NewsAbstractService.INSTANCE;

    private News news;
    private boolean isArticle;
    private boolean showAddAuthor;
    private File photo;
    private int width;
    private int height;
    private String format;
    private List<Auteur> auteurs = service.getAllAuthors();
    private HashSet<Auteur> newsAuteurs;
    private List<CheckMenuItem> auteursToString;

    //elements
    //panes
    @FXML
    private Pane articlePane;
    @FXML
    private BorderPane existentAuthorPane;
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
    private TextField nomLabel;
    @FXML
    private TextField prenomLabel;
    @FXML
    private Label nomTextError;
    @FXML
    private Label prenomTextError;
    @FXML
    private Label authorTextError;

    //article pane elements
    @FXML
    private RadioButton articleRB;
    @FXML
    private TextArea contenuTextArea;
    @FXML
    private CheckBox electroniqueCheckBox;
    @FXML
    private Label infoLabel;
    @FXML
    private Label articleTextError;

    //photo pane elements
    @FXML
    private RadioButton photoRB;
    @FXML
    private CheckBox coloredCheckBox;
    @FXML
    private Label photoImageError;

    @FXML
    private Button addButton;

    MenuButton choices = null;

    public void loadInfoFromFields() {
        int index = 0;
        newsAuteurs = new HashSet<>();
        for (CheckMenuItem item : auteursToString) {
            if (item.selectedProperty().getValue()) {
                newsAuteurs.add(auteurs.get(index));
            }
            index++;
        }
        news.setAuteurs(newsAuteurs);

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

            if (articleRB.isSelected()) {
                ((Article) news).setContenu(contenuTextArea.getText());
                ((Article) news).setSiElectronique(electroniqueCheckBox.isSelected());
            } else {
                ((Photo) news).setSiColoree(coloredCheckBox.isSelected());
                ((Photo) news).setHauteur(height);
                ((Photo) news).setLargeur(width);
                ((Photo) news).setFormat(format);
                try {
                    ((Photo) news).setImage(photo);
                } catch (IOException ex) {
                    Logger.getLogger(AddWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    @FXML
    private void addButtonAction(ActionEvent event) {

        News oldNews = null;

        if (news != null) {
            try {
                oldNews = news.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(AddWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (articleRB.isSelected()) {
                news = new Article();
            } else {
                news = new Photo();
            }
        }

        loadInfoFromFields();
        //todo switch services file db
        String message = "";
        try {
            if (MainWindowController.isForUpdate) {
                service.update(oldNews, news);
                message = "Nouvelle a été mis a jour";
            } else {
                service.add(news);
                message = "Nouvelle a été ajouté";
            }
            Stage stage = (Stage) addAuthorPane.getScene().getWindow();
            Dialogs.create()
                    .owner(new Stage())
                    .title("Information")
                    .masthead(null)
                    .message(message)
                    .showInformation();
            clearFields();
            stage.close();
        } catch (Exception e) {
            Stage stage = (Stage) addAuthorPane.getScene().getWindow();
            Dialogs.create()
                    .owner(new Stage())
                    .title("Erreur!")
                    .masthead(null)
                    .message(message)
                    .showInformation();
            clearFields();
            stage.close();

        }

    }

    @FXML
    private void addAuthor(ActionEvent event) {
        if (validateAuthorFields()) {
            Auteur newAuthor = new Auteur(0, nomLabel.getText(), prenomLabel.getText());
            //AuteurDao.saveAuteur(newAuthor);
            service.storeAuthor(newAuthor);

            Stage stage = (Stage) addAuthorPane.getScene().getWindow();
            Dialogs.create()
                    .owner(new Stage())
                    .title("Information")
                    .masthead(null)
                    .message("Auteur a été ajouté")
                    .showInformation();
            
            toggleAuthorPane(null);
            createCombobox();
        }
    }

    @FXML
    private void newsPreview(ActionEvent event ) {
        loadInfoFromFields();
        Parent root = null;
        try {
            if (news instanceof Article) {
                root = FXMLLoader.load(getClass().getResource("/ArticlePreviewWindow.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("/PhotoPreviewWindow.fxml"));

            }
        } catch (IOException ex) {
            Logger.getLogger(AddWindowController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Mettre a jour");
        stage.setScene(new Scene(root));

        stage.show();
    }

    @FXML
    private void uploadFileAction(ActionEvent event) throws IOException {

        //Set extension filter
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        photo = fileChooser.showOpenDialog(null);
        infoLabel.setText(getImageInfo(photo));
    }

    public String getImageInfo(File photo) {
        String info = "";
        try {
            BufferedImage bimg = ImageIO.read(photo);
            width = bimg.getWidth();
            height = bimg.getHeight();
            format="";
            format = Files.probeContentType(photo.toPath());
            info = "Format: " + format + ", " + width + "*" + height;

        } catch (IOException ex) {
            Logger.getLogger(AddWindowController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return info;
    }

    @FXML
    private void uploadUrlAction(ActionEvent event) {
        try {
            if (news == null) {
                news = new Photo();
            }
            news.setSource(new URL(sourceText.getText()));
            photo = ((Photo) news).loadPhotoFromURL(news.getSource());
            BufferedImage bimg = ImageIO.read(photo);
            width = bimg.getWidth();
            height = bimg.getHeight();
            format = Files.probeContentType(photo.toPath());
            String info = "Format: " + format + ", " + width + "*" + height;
            infoLabel.setText(info);
            ((Photo) news).setImage(photo);
            ((Photo) news).setLargeur(width);
            ((Photo) news).setHauteur(height);
            ((Photo) news).setFormat(format);

        } catch (MalformedURLException ex) {
            Logger.getLogger(AddWindowController.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddWindowController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void toggleAuthorPane(ActionEvent event) {
        for (Node n : addAuthorPane.getChildren()) {
            if (n instanceof Label) {
                if (showAddAuthor) {
                    ((Label) n).setOpacity(1);
                } else {
                    ((Label) n).setOpacity(0);
                }
            }
            if (n instanceof TextField) {
                if (showAddAuthor) {
                    ((TextField) n).setOpacity(1);
                } else {
                    ((TextField) n).setOpacity(0);
                }
            }
            if (n instanceof Button) {
                if (showAddAuthor) {
                    ((Button) n).setOpacity(1);
                } else {
                    ((Button) n).setOpacity(0);
                }
            }
        }
        showAddAuthor = !showAddAuthor;
        nomTextError.setOpacity(0);
        prenomTextError.setOpacity(0);
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

        photoImageError.setOpacity(0);
        articleTextError.setOpacity(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //if was launched for updating of an entry
        if (MainWindowController.isForUpdate) {
            news = MainWindowController.currentNews;
            addButton.setText("Mettre a jour");
            if (news instanceof Photo) {
                photo = ((Photo) news).getImage();
            }
            showAllFields();
        } else {
            dateText.setValue(LocalDate.now());
        }
        createCombobox();
    }

    public void showAllFields() {
        titleText.setText(news.getTitre());
        sourceText.setText(news.getSource().toString());
        dateText.setValue(news.getDate());
        if (news instanceof Article) {
            isArticle = true;
            contenuTextArea.setText(((Article) news).getContenu());
            articleRB.setSelected(true);
            photoRB.setDisable(true);
        } else {
            isArticle = false;
            photoRB.setSelected(true);
            articleRB.setDisable(true);
            toggleNewsPane(null);
            if (((Photo) news).isSiColoree()) {
                coloredCheckBox.setSelected(true);
            }
            infoLabel.setText(getImageInfo(((Photo) news).getImage()));
            dateText.setValue(news.getDate());
        }
    }

    public void createCombobox() {
        // load combobox entries
        auteurs = service.getAllAuthors();
        auteursToString = new ArrayList<>();
        auteurs.stream().forEach((a) -> {
            auteursToString.add(new CheckMenuItem(a.toString()));
        });
        //create combobox with multiple choie
        choices = new MenuButton("Auteurs");
        auteursToString.add(new CheckMenuItem("Ajouter..."));
        choices.getItems().addAll(auteursToString);

        // Keep track of selected items  
        ListView<String> selectedItems = new ListView<>();

        auteursToString.stream().map((item) -> {
            if ((news != null) && (news.getAutorsString().contains(item.getText()))) {
                item.setSelected(true);
                selectedItems.getItems().add(item.getText());
            }
            return item;
        }).forEach((item) -> {
            item.selectedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) -> {
                if (item.getText().equals("Ajouter...")) {
                    //don't check in combobox
                    item.setSelected(false);
                    //show pane
                    toggleAuthorPane(null);
                } else if (isNowSelected) {
                    selectedItems.getItems().add(item.getText());
                } else {
                    selectedItems.getItems().remove(item.getText());
                }
            });
        });

        existentAuthorPane.setTop(choices);
        existentAuthorPane.setCenter(selectedItems);

    }

    public boolean validateAuthorFields() {
        //if hidden - don't validate
        if (prenomLabel.getOpacity() == 0) {
            return true;
        }
        boolean isValid = true;
        nomTextError.setOpacity(0);
        prenomTextError.setOpacity(0);

        if (nomLabel.getText().trim().isEmpty()) {
            nomTextError.setOpacity(1);
            isValid = false;
        }
        if (prenomLabel.getText().trim().isEmpty()) {
            prenomTextError.setOpacity(1);
            isValid = false;
        }
        return isValid;
    }

    public boolean validateFields() {
        boolean isValid = true;
        if (validateAuthorFields()) {
            //hide all errors
            titleTextError.setOpacity(0);
            sourceTextError.setOpacity(0);
            articleTextError.setOpacity(0);
            photoImageError.setOpacity(0);
            authorTextError.setOpacity(0);

            if (titleText.getText().trim().isEmpty()) {
                titleTextError.setOpacity(1);
                isValid = false;
            }
            if (news.getAuteurs().isEmpty()) {
                authorTextError.setOpacity(1);
                isValid = false;
            }
            try {
                URL url = new URL(sourceText.getText());
            } catch (MalformedURLException ex) {
                sourceTextError.setOpacity(1);
                isValid = false;
            }

            if (articleRB.isSelected()) {
                if (contenuTextArea.getText().trim().isEmpty()) {
                    articleTextError.setOpacity(1);
                    isValid = false;
                }
            } else if (((Photo) news).getImage() == null) {
                photoImageError.setOpacity(1);
                isValid = false;
            }
        }
        return isValid;
    }

    public void clearFields() {
        titleText.setText("");
        sourceText.setText("");
        nomLabel.setText("");
        infoLabel.setText("");
        prenomLabel.setText("");
        contenuTextArea.setText("");
        createCombobox();
    }
}
