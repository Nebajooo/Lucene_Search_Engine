package com.example.demo;


import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.protobuf.TextFormat.ParseException;

@RestController
public class SearchController {

    @Autowired
    private LuceneSearchService luceneSearchService;

    @GetMapping("/search")
    public List<LuceneSearchService.SearchResult> search(@RequestParam String query) throws ParseException {
        try {
            // Specify the field name and query string
            String fieldName = "content";

            // Call the queryParserSearch method of LuceneSearchService
            System.out.println("QUERY: " + query);
            return luceneSearchService.queryParserSearch(fieldName, query);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed (e.g., return an error response)
            return null;
        }
    }
}
