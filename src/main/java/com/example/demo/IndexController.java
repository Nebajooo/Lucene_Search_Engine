package com.example.demo;

import java.io.IOException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class IndexController {
	private final LuceneIndexerService luceneIndexerService;

	public IndexController(LuceneIndexerService luceneIndexerService) {
		this.luceneIndexerService = luceneIndexerService;
	}

	@GetMapping("/indexData")
	public String indexData() {
		try {
			luceneIndexerService.createIndex();
			return "Indexing completed successfully!";
		} catch (IOException e) {
			e.printStackTrace();
			return "Indexing failed: " + e.getMessage();
		}
	}
}
