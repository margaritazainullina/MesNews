/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import mesnews.dao.PhotoDao;
import mesnews.model.Photo;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Margarita
 */
public class PhotoPreviewWindowController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private ImageView photoImage;
    @FXML
    private Label infoLabel;
    @FXML
    private Label keywordsLabel;

    private Photo photo;

    @FXML
    private void publishButtonAction(ActionEvent event) {
        // TODO add to storage
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO get from storage
        photo = PhotoDao.listPhotos().get(0);

        titleLabel.setText(photo.getTitre());
        authorLabel.setText("Auteur: " + photo.getAutorsString());
        dateLabel.setText(photo.getDate().toString());
        
        try {
           photoImage.setImage(new Image(new FileInputStream(photo.getImage())));
        } catch (IOException ex) {
            Logger.getLogger(PhotoPreviewWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

        infoLabel.setText(photo.info());
        keywordsLabel.setText(photo.getKeyWordsString());
    }

}
