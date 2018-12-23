package com.ankitrex.mocksearch.service;

import java.util.List;

import com.ankitrex.mocksearch.entity.SearchResult;

public interface SearchService {
	
	/**
	 * Search the keyword and ngram indexes, score, filter and rank the results.
	 * 
	 * @param query - search query
	 * @param maxResults - max results to return
	 * @return List<SearchResult> - Matched data in descending order by score
	 */
	List<SearchResult> performSearchOnIndex(String query, Integer maxResults);
}
