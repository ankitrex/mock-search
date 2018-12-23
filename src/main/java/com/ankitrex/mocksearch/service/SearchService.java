package com.ankitrex.mocksearch.service;

import java.util.List;

import com.ankitrex.mocksearch.entity.SearchResult;

public interface SearchService {

	List<SearchResult> performSearchOnIndex(String query, Integer maxResults);
}
