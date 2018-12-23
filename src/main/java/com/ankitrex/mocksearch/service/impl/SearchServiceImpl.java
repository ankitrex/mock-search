package com.ankitrex.mocksearch.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ankitrex.mocksearch.entity.SearchResult;
import com.ankitrex.mocksearch.entity.User;
import com.ankitrex.mocksearch.service.IndexService;
import com.ankitrex.mocksearch.service.SearchService;
import com.ankitrex.mocksearch.util.TokenizerUtility;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	IndexService indexService;

	@Autowired
	TokenizerUtility tokenizerUtility;

	@Override
	public List<SearchResult> performSearchOnIndex(String query) {

		List<User> users = indexService.getIndexedUsers();
		SortedMap<String, List<Integer>> keywordInvertedIndex = indexService.getKeywordInvertedIndex();
		Map<String, List<Integer>> ngramInvertedIndex = indexService.getNgramInvertedIndex();

		Map<Integer, DoubleAdder> scores = new HashMap<>();

		String keywordQueryToken = tokenizerUtility.tokenizeQueryKeyword(query);
		SortedMap<String, List<Integer>> keywordMatchMap = keywordInvertedIndex.subMap(keywordQueryToken,
				keywordQueryToken + Character.MAX_VALUE);
		
		for (Entry<String, List<Integer>> entry : keywordMatchMap.entrySet()) {
			
			Double score = (double) keywordQueryToken.length() / entry.getKey().length();
			
			entry.getValue().forEach(index -> {
				scores.putIfAbsent(index, new DoubleAdder());
				scores.get(index).add(score);
			});
		}

		List<String> queryTokens = tokenizerUtility.tokenizeQueryNgram(query);
		for (String token : queryTokens) {

			if (ngramInvertedIndex.containsKey(token)) {
				Double score = Math.pow((double) token.length() / query.length(), 3);
				List<Integer> indices = ngramInvertedIndex.get(token);
				indices.forEach(index -> {
					scores.putIfAbsent(index, new DoubleAdder());
					scores.get(index).add(score);
				});
			}
		}

		return scores.entrySet().stream()
				.sorted((e1, e2) -> Double.valueOf(e2.getValue().doubleValue()).compareTo(e1.getValue().doubleValue()))
				.map(x -> formatUserResult(users.get(x.getKey()), x.getValue().doubleValue()))
				.collect(Collectors.toList());

	}

	private SearchResult formatUserResult(User user, Double score) {

		SearchResult searchResult = new SearchResult();
		searchResult.setName(String.format("%s %s %s", user.getFirstName(), user.getMiddleName(), user.getLastName()));
		searchResult.setScore(score);

		return searchResult;
	}
}
