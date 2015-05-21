/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesnews.db;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import mesnews.model.News;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.RAMDirectory;

/**
 *
 * @author Margarita
 */
public abstract class NewsAbstractService {

    public static TreeSet<News> news;
    public static IndexWriter newsIndex;
    public static Searcher searcher;
    public static RAMDirectory idx;

    //getteurs and setteurs pour collection
    public static TreeSet<News> getNews() {
        return news;
    }

    public static void setNews(TreeSet<News> news) {
        NewsAbstractService.news = news;
    }

    //ajoute une nouvelle
    public static void ajouter(News n) {
        news.add(n);
    }

    public static void afficher() {
        if (news.isEmpty()) {
            System.out.println("La base est vide!");
        } else {
            System.out.println("Un total de " + news.size() + " entr�es");
        }
        news.stream().forEach((n) -> {
            System.out.println(n);
        });
    }

    public static Set<News> getNewsSortedByTitre() {
        Set newSet = new TreeSet(News.NewsComparator.TITRE);
        newSet.addAll(news);
        return newSet;
    }

    public static Set<News> getNewsSortedByAuteur() {
        Set newSet = new TreeSet(News.NewsComparator.AUTEUR);
        newSet.addAll(news);
        return newSet;
    }

    public static Set<News> getNewsSortedByDate() {
        Set newSet = new TreeSet(News.NewsComparator.DATE);
        newSet.addAll(news);
        return newSet;
    }

    public static Set<News> getNewsSortedBySource() {
        Set newSet = new TreeSet(News.NewsComparator.SOURCE);
        newSet.addAll(news);
        return newSet;
    }

    public static Set<News> getNewsSortedByKeywords() {
        Set newSet = new TreeSet(News.NewsComparator.KEYWORDS);
        newSet.addAll(news);
        return newSet;
    }

    public static void indexNews() throws IOException {
        idx = new RAMDirectory();
            
        //create overall index
        newsIndex = new IndexWriter(idx, new StandardAnalyzer(), true);
        for (News n : news) {
            newsIndex.addDocument(n.createDocument()); // Optimize and close the writer to finish building the index
        }
        newsIndex.optimize();
        newsIndex.close();

        for (News n : news) {
            RAMDirectory documentIdx = new RAMDirectory();
            //create index for each document
            IndexWriter documentIndexWriter = new IndexWriter(documentIdx, new StandardAnalyzer(), true);
            documentIndexWriter.addDocument(n.createDocument()); // Optimize and close the writer to finish building the index
            documentIndexWriter.optimize();
            documentIndexWriter.close();
            //set keywords for document
            IndexReader reader = IndexReader.open(documentIdx);
            TermFreqVector tfv;

            if (reader.getTermFreqVector(0, "content") != null) {
                tfv = reader.getTermFreqVector(0, "content");
            } else {
                //if photo
                tfv = reader.getTermFreqVector(0, "titre");
            }

            //create hashmap with terms and frequencies
            HashMap<String, Integer> frequencies = new HashMap<>();
            for (int j = 0; j < tfv.getTerms().length; j++) {
                frequencies.put(tfv.getTerms()[j], tfv.getTermFrequencies()[j]);
            }

            //sort by frequency
            List<Map.Entry<String, Integer>> list
                    = new LinkedList<>(frequencies.entrySet());
            Collections.sort(list, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> o2.getValue().compareTo(o1.getValue()));

            //set 10 or less keywords
            for (Iterator<Map.Entry<String, Integer>> iterator = list.iterator(); iterator.hasNext();) {
                String keyword = iterator.next().getKey();
                if (!(keyword.matches("[0-9]+") && keyword.length() > 2)) {
                    n.getKeyWords().add(keyword);
                }
                if (n.getKeyWords().size() >= 10) {
                    break;
                }
            }
        }

    }

    /**
     * Searches for the given string in the "content" field
     *
     * @param queryString
     * @return
     * @throws org.apache.lucene.queryParser.ParseException
     * @throws java.io.IOException
     */
    public static TreeSet<News> rechercher(String queryString)
            throws ParseException, IOException {
        // indexNews();
        TreeSet<News> result = new TreeSet<>();
        // Build a Query object
        Query query = QueryParser.parse(
                queryString, "titre", new StandardAnalyzer());

        // Search for the query
        Hits hits = searcher.search(query);

        // Examine the Hits object to see if there were any matches
        int hitCount = hits.length();
        if (hitCount == 0) {
            System.out.println(
                    "No matches were found for \"" + queryString + "\"");
        } else {
            System.out.println("Hits for \""
                    + queryString + "\" were found in quotes by:");

            // Iterate over the Documents in the Hits object
            for (int i = 0; i < hitCount; i++) {
                Document doc = hits.doc(i);
                String title = doc.get("title");
                result.add(getNewsByTitle(title));
            }
        }
        System.out.println();
        return result;
    }

    private static News getNewsByTitle(String title) {
        Iterator<News> it = news.iterator();

        while (it.hasNext()) {
            News current = it.next();
            if (current.getTitre().equals(title)) {
                return current;
            }
        }
        return null;
    }

    public static Set<News> search(String queryString) throws IOException, ParseException {
        Set<News> result = new HashSet<News>();

        // Build an IndexSearcher using the in-memory index
        searcher = new IndexSearcher(NewsDBService.idx);

        // Build a Query object
        Query query = QueryParser.parse(
                queryString, "content", new StandardAnalyzer());

        // Search for the query
        Hits hits = searcher.search(query);

        // Examine the Hits object to see if there were any matches
        int hitCount = hits.length();

        System.out.println("Hits for \""
                + queryString + "\" were found in quotes by:");

        // Iterate over the Documents in the Hits object
        for (int i = 0; i < hitCount; i++) {
            Document doc = hits.doc(i);
            result.add(getNewsByTitle(doc.get("title")));
            searcher.close();
        }
        return result;
    }
}
