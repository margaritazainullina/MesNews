/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.db;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import mesnews.model.News;

/**
 *
 * @author Margarita
 */
public abstract class NewsAbstractService {

    protected TreeSet<News> news;

    //getteurs and setteurs pour collection
    public TreeSet<News> getNews() {
        return news;
    }

    public void setNews(TreeSet<News> news) {
        this.news = news;
    }

    //ajoute une nouvelle
    public void ajoute(News n) {
        news.add(n);
    }

    public void afficher() {
        if (news.isEmpty()) {
            System.out.println("La base est vide!");
        } else {
            System.out.println("Un total de " + news.size() + " entr�es");
        }
        for (News n : news) {
            System.out.println(n);
        }
    }

    public TreeSet<News> rechercher(String nom) {
        TreeSet<News> results = new TreeSet<>();
        String[] cles = nom.split("\\W+");
        for (News n : news) {
            for (String cle : cles) {
                if (n.getTitre().contains(cle)) {
                    results.add(n);
                }
            }
        }
        return results;
    }

    public Set<News> getNewsSortedByTitre() {
        Set newSet = new TreeSet(News.NewsComparator.TITRE);
        newSet.addAll(news);
        return newSet;
    }

    public Set<News> getNewsSortedByAuteur() {
        Set newSet = new TreeSet(News.NewsComparator.AUTEUR);
        newSet.addAll(news);
        return newSet;
    }

    public Set<News> getNewsSortedByDate() {
        Set newSet = new TreeSet(News.NewsComparator.DATE);
        newSet.addAll(news);
        return newSet;
    }

    public Set<News> getNewsSortedBySource() {
        Set newSet = new TreeSet(News.NewsComparator.SOURCE);
        newSet.addAll(news);
        return newSet;
    }

    public Set<News> getNewsSortedByKeywords() {
        Set newSet = new TreeSet(News.NewsComparator.KEYWORDS);
        newSet.addAll(news);
        return newSet;
    }
}
