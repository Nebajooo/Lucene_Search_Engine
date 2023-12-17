package com.example.demo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LuceneSearchService {

    private final String indexDir = "C:\\Users\\dell\\Desktop\\lucenecode\\indexdir";

    public List<SearchResult> queryParserSearch(String fieldName, String queryString) throws IOException, ParseException {
        List<SearchResult> searchResults = new ArrayList<>();

        try (IndexReader ireader = DirectoryReader.open(FSDirectory.open(new File(indexDir).toPath()))) {
            IndexSearcher indexSearcher = new IndexSearcher(ireader);

            QueryParser queryParser = new QueryParser(fieldName, new StandardAnalyzer());
            Query query = queryParser.parse(queryString);
            TopDocs hits = indexSearcher.search(query, 10);

            for (int i = 0; i < hits.scoreDocs.length; i++) {
                int docId = hits.scoreDocs[i].doc;
                Document doc = indexSearcher.doc(docId);

                // Create a SearchResult object and add it to the list
                SearchResult result = new SearchResult(
                        docId,
                        doc.get("title"),
                        doc.get("author")
                        // Add other fields as needed
                );
                searchResults.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searchResults;
    }

 
    public static class SearchResult {
        private final int documentId;
        private final String title;
        private final String author;

        public SearchResult(int documentId, String title, String author) {
            this.documentId = documentId;
            this.title = title;
            this.author = author;
        }

        // Getters for fields
        public int getDocumentId() {
            return documentId;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }
    }
}
