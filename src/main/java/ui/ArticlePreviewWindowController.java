/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import mesnews.dao.ArticleDao;
import mesnews.model.Article;

/**
 *
 * @author Margarita
 */
public class ArticlePreviewWindowController implements Initializable {
    
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private TextArea textArea;
    @FXML
    private Label infoLabel;
    @FXML
    private Label keywordsLabel;
    
    private Article article;
    
    @FXML
    private void publishButtonAction(ActionEvent event) {
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO get from storage
        article = ArticleDao.listArticles().get(0);
        
        titleLabel.setText(article.getTitre());
        authorLabel.setText("Auteur: " + article.getAutorsString());
        dateLabel.setText(article.getDate().toString());
        textArea.setText(article.getContenu());
        
        infoLabel.setText(article.info());
        keywordsLabel.setText(article.getKeyWordsString());
    }
    
}
