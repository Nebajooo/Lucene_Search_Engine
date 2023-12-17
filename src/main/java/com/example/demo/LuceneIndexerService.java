package com.example.demo;

import org.springframework.stereotype.Service;
import org.apache.lucene.document.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service

public class LuceneIndexerService {
	private final String dataDir = "C:\\Users\\dell\\Desktop\\lucenecode\\datadir";
	private final String indexDir = "C:\\Users\\dell\\Desktop\\lucenecode\\indexdir";
//C:\\Users\\dell\\eclipse-workspace\\SearchEngineLab\\datadir";
//	"C:\\Users\\dell\\eclipse-workspace\\SearchEngineLab\\indexdir"
	public void createIndex() throws IOException {
		List<File> results = new ArrayList<>();
		findFiles(results, new File(dataDir));
		System.out.println(results.size() + " articles to index");

		// Create index writer
		Directory directory = FSDirectory.open(Paths.get(indexDir));
		IndexWriterConfig iwconf = new IndexWriterConfig(new StandardAnalyzer());
		iwconf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		try (IndexWriter indexWriter = new IndexWriter(directory, iwconf)) {
			// Add documents to the index
			for (File file : results) {
				Document doc = getDocument(file);
				indexWriter.addDocument(doc);
		
			}
		}
		directory.close();
	}

	// Implement the findFiles method to populate the 'results' list with files from
	// the data directory.
	private void findFiles(List<File> results, File dataDir) {
		if (dataDir != null && dataDir.isDirectory()) {
			File[] files = dataDir.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						// Add only files to the results list, skipping directories

						results.add(file);
					}
				}
			}
		}
	}

	private Document getDocument(File file) throws IOException {
		Properties props = new Properties();
		try (FileInputStream inputStream = new FileInputStream(file)) {
			props.load(inputStream);
		}

		Document doc = new Document();

		String title = props.getProperty("title");
		String url = props.getProperty("url");
		String author = props.getProperty("author");
		String content = props.getProperty("content");

		/*
		 * Let's be ensure that the values retrieved from the properties file are not
		 * null before attempting to create Lucene Field instances.
		 */
		if (title != null) {
			FieldType fieldType = new FieldType();
			fieldType.setStored(true);
			fieldType.setTokenized(true);
			// This field should be tokenized for text

			doc.add(new Field("title", title, fieldType));

		}

		if (url != null) {
			FieldType fieldType = new FieldType();
			fieldType.setStored(true);
			fieldType.setTokenized(false); // This field should not be tokenized. doc.add(new Field("url", url,
											// fieldType));
		}

		if (author != null) {
			FieldType fieldType = new FieldType();
			fieldType.setStored(true);
			fieldType.setTokenized(true); // This field should be tokenized for

			doc.add(new Field("author", author, fieldType));

		}

		if (content != null) {
			doc.add(new TextField("content", content, Field.Store.NO)); // Content is tokenized.
		}
		return doc;
	}
}
