/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mesnews.db.NewsAbstractService;
import mesnews.db.NewsDBService;
import mesnews.model.News;
import mesnews.model.Photo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.ArrayUtil;

/**
 *
 * @author Margarita
 */
public class MainWindowController implements Initializable {

    @FXML
    private Pagination paginator;

    private List<News> news = new ArrayList<>();
    public static News currentNews;

    @FXML
    private ComboBox sortComboBox;
    @FXML
    private CheckBox electroniqueCheckBox;
    @FXML
    private TextField searchTextBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadAll();
        paginator.setUserData(url);
    }

    public VBox createPage(int pageIndex) {
        //set elemenst of combobox
        ObservableList<String> values = FXCollections.observableArrayList("Par date", "Par titre",
                "Par auteur", "Par source", "Par keywords");
        sortComboBox.setItems(values);
        sortComboBox.setValue(values.get(0));

        VBox box = new VBox();
        int page = pageIndex;
        for (int i = page; i < page + 1; i++) {
            VBox element = new VBox();
            currentNews = news.get(i);
            //load fragments - Panes with Photo or Qrticle info
            if (currentNews instanceof Photo) {
                try {
                    Parent root = new FXMLLoader().load(getClass().getResource("/PhotoInfo.fxml"));
                    element.getChildren().addAll(root);
                    box.getChildren().add(element);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    Parent root = new FXMLLoader().load(getClass().getResource("/ArticleInfo.fxml"));
                    element.getChildren().addAll(root);
                    box.getChildren().add(element);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return box;
    }

    public void loadAll() {
        NewsDBService.load();
        try {
            NewsDBService.indexNews();
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        news.clear();
        news.addAll(NewsDBService.INSTANCE.news);
        paginator.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
        paginator.setPageCount(news.size());
    }

    @FXML
    private void search(ActionEvent event) {
        String query = searchTextBox.getText().trim();
        if (!query.isEmpty()) {
            try {
                Set<News> result = NewsDBService.search(query);
                if (result.isEmpty()) {
                    Stage dialog = new Stage();
                    dialog.initStyle(StageStyle.UTILITY);
                    Scene scene = new Scene(new Group(new Text(40, 40, "Il n'y a aucun résultat pour votre recherche.")));
                    dialog.setScene(scene);
                    dialog.show();
                } else {
                    news.clear();
                    news.addAll(result);
                    news.sort(News.NewsComparator.DATE);

                    paginator.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
                    paginator.setPageCount(news.size());
                }
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void sort(ActionEvent event) {
        switch (sortComboBox.getValue().toString()) {
            case "Par date": {
                news.sort(News.NewsComparator.DATE);
                break;
            }
            case "Par titre": {
                news.sort(News.NewsComparator.TITRE);
                break;
            }
            case "Par auteur": {
                news.sort(News.NewsComparator.AUTEUR);
                break;
            }
            case "Par source": {
                news.sort(News.NewsComparator.SOURCE);
                break;
            }
            case "Par keywords": {
                news.sort(News.NewsComparator.KEYWORDS);
                break;
            }
        }
        paginator.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
        paginator.setPageCount(news.size());
    }

    @FXML
    private void cancel(ActionEvent event) {
        searchTextBox.setText("");
        loadAll();
    }
}
