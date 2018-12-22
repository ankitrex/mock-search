package com.ankitrex.mocksearch.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
		Map<String, List<Integer>> invertedIndex = indexService.getInvertedIndex();

		List<String> queryTokens = tokenizerUtility.tokenizeQuery(query);

		Map<Integer, DoubleAdder> scores = new HashMap<>();

		for (String token : queryTokens) {

			if (invertedIndex.containsKey(token)) {
				Double score = Math.pow((double) token.length() / query.length(), 3);
				List<Integer> indices = invertedIndex.get(token);
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

//		List<Integer> indices = new ArrayList<>();
//		for (String token : queryTokens) {
//
//			if (invertedIndex.containsKey(token)) {
//				indices.addAll(invertedIndex.get(token));
//			}
//		}
//		
//		Collections.sort(indices);
//
//		Integer lastIndex = indices.get(0);
//		Double score = 1.0;
//		for (int i = 1; i < indices.size(); i++) {
//			Integer currentIndex = indices.get(i);
//			if (lastIndex.intValue() == currentIndex.intValue()) {
//				score++;
//			}
//			else {
//				SearchResult searchResult = new SearchResult();
//				searchResult.setName(formatUserResult(users.get(lastIndex)));
//				searchResult.setScore(score);
//				result.add(searchResult);
//
//				lastIndex = currentIndex;
//				score = 1.0;
//			}
//			if (i == indices.size() - 1) {
//				SearchResult searchResult = new SearchResult();
//				searchResult.setName(formatUserResult(users.get(currentIndex)));
//				searchResult.setScore(score);
//				result.add(searchResult);
//			}
//		}
//
//		Collections.sort(result, (SearchResult s1, SearchResult s2) -> s2.getScore().compareTo(s1.getScore()));
//
//		return result;
	}

	private SearchResult formatUserResult(User user, Double score) {

		SearchResult searchResult = new SearchResult();
		searchResult.setName(String.format("%s %s %s", user.getFirstName(), user.getMiddleName(), user.getLastName()));
		searchResult.setScore(score);

		return searchResult;
	}
}
