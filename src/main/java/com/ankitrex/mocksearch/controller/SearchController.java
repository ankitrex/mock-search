package com.ankitrex.mocksearch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ankitrex.mocksearch.entity.SearchResult;
import com.ankitrex.mocksearch.service.SearchService;
import com.ankitrex.mocksearch.util.Constants;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SearchController {

	@Autowired
	SearchService searchService;

	/**
	 * search the keyword in index data, score, filter and rank it.
	 * 
	 * @param searchQuery - input query.
	 * @param maxResults  - max results to return. default is 20 results.+
	 * @return - List<SearchResult> - Matched data in descending order by score.
	 */
	@GetMapping("/search")
	public List<SearchResult> search(@RequestParam(name = "query", required = true) String searchQuery,
			@RequestParam(name = "maxResults", defaultValue = "20", required = false) Integer maxResults) {

		long start = System.currentTimeMillis();

		// if search query is less than 3 characters, throw an error.
		if (searchQuery.length() < Constants.MINIMUM_TOKEN_LENGTH) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
					"Search query must be atleast 3 characters long.");
		}
		
		// perform search and return the results
		List<SearchResult> results = searchService.performSearchOnIndex(searchQuery, maxResults);
		
		log.info(String.format("Time to search, score, filter and rank: %dms", (System.currentTimeMillis() - start)));

		return results;
	}
}
