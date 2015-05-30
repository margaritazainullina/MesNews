/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import mesnews.db.NewsAbstractService;
import mesnews.model.News;
import mesnews.model.Photo;
import org.apache.lucene.queryParser.ParseException;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Margarita
 */
public class MainWindowController implements Initializable {

    NewsAbstractService service = NewsAbstractService.INSTANCE;

    @FXML
    private Pagination paginator;
    @FXML
    private Label noEntriesLabel;

    private final List<News> news = new ArrayList<>();
    public static News currentNews;
    public static boolean isForUpdate;

    @FXML
    private ComboBox sortComboBox;
    @FXML
    private TextField searchTextBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateEntries();

        if (news.size() != 0) {
            paginator.setUserData(url);
        }
    }

    public VBox createPage(int pageIndex) {
        VBox box = new VBox();
        if (pageIndex < news.size()) {
            //set elemenst of combobox
            ObservableList<String> values = FXCollections.observableArrayList("Par date", "Par titre",
                    "Par auteur", "Par source", "Par keywords");
            sortComboBox.setItems(values);
            sortComboBox.setValue(values.get(0));

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
                        Parent root = new FXMLLoader().load(getClass().getResource("/ArticleInfo1.fxml"));
                        element.getChildren().addAll(root);
                        box.getChildren().add(element);
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return box;
    }

    @FXML
    private void insert(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AddWindow2.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Mettre a jour");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                updateEntries();
            }
        });
    }

    @FXML
    private void update(ActionEvent event) throws IOException {
        isForUpdate = true;

        Parent root = FXMLLoader.load(getClass().getResource("/AddWindow2.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Mettre a jour");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                updateEntries();
            }
        });
    }

    @FXML
    private void delete(ActionEvent event) {
        Action response = Dialogs.create()
                .owner(new Stage())
                .title("Confirmation")
                .masthead("Supprimer")
                .message("Voulez vous supprimer cette nouvelle?")
                .actions(Dialog.ACTION_YES, Dialog.ACTION_NO)
                .showConfirm();

        if (response == Dialog.ACTION_YES) {
            service.delete(currentNews);
            updateEntries();
        }

    }

    public void updateEntries() {
        news.clear();
        news.addAll(service.news);
        paginator.setPageCount(news.size());
        if (!news.isEmpty()) {
            paginator.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
        } else {
            noEntriesLabel.setOpacity(1);
        }
    }

    @FXML
    private void search(ActionEvent event) {
        String query = searchTextBox.getText().trim();
        if (!query.isEmpty()) {
            try {
                Set<News> result = service.search(query);
                if (result.isEmpty()) {
                    Dialogs.create()
                            .owner(new Stage())
                            .title("Recherche")
                            .masthead(null)
                            .message("Il n'y a aucun résultat pour votre recherche.")
                            .showInformation();
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
        updateEntries();
    }
}
