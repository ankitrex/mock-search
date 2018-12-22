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

@RestController
public class SearchController {

	@Autowired
	SearchService searchService;

	@GetMapping("/search")
	public List<SearchResult> search(@RequestParam(name = "query", required = true) String searchQuery) {

		if (searchQuery.length() < Constants.TOKEN_LENGTH) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Search query must be atleast 3 characters long.");
		}

		return searchService.performSearchOnIndex(searchQuery);
	}
}
