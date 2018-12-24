package com.ankitrex.mocksearch.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
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
	IndexService		indexService;

	@Autowired
	TokenizerUtility	tokenizerUtility;

	@Override
	public List<SearchResult> performSearchOnIndex(String query, Integer maxResults) {

		// get user, keyword inverted index and ngram index from memory
		List<User> users = indexService.getIndexedUsers();
		SortedMap<String, List<Integer>> keywordInvertedIndex = indexService.getKeywordInvertedIndex();
		Map<String, List<Integer>> ngramInvertedIndex = indexService.getNgramInvertedIndex();

		Map<Integer, DoubleAdder> scores = new HashMap<>();

		// get keyword token and get submap of keywordInvertedIndex that begins
		// with
		// input keyword
		String keywordQueryToken = tokenizerUtility.tokenizeQueryKeyword(query);
		SortedMap<String, List<Integer>> keywordMatchMap = keywordInvertedIndex.subMap(keywordQueryToken, keywordQueryToken + Character.MAX_VALUE);

		// Score and persist the results in scores map
		for (Entry<String, List<Integer>> entry : keywordMatchMap.entrySet()) {

			// substring matched directly affects the score
			// kat matched with kate will have more score than kat matched with
			// katelyn
			Double score = (double) keywordQueryToken.length() / entry.getKey().length();

			entry.getValue().forEach(index -> {
				scores.putIfAbsent(index, new DoubleAdder());
				scores.get(index).add(score);
			});
		}

		// if max results are satisfied, don't go for partial search
		if (scores.size() < maxResults) {

			// generate query tokens for search string and get all the users
			// that contain
			// the token
			List<String> queryTokens = tokenizerUtility.tokenizeQueryNgram(query);
			for (String token : queryTokens) {

				// if token exists, score and persist the results in scores map
				if (ngramInvertedIndex.containsKey(token)) {

					// length of the token matched will directly affect the
					// score
					// longer the token, better the score
					Double score = Math.pow((double) token.length() / query.length(), 3);
					List<Integer> indices = ngramInvertedIndex.get(token);

					// add to scores in the map
					indices.forEach(index -> {
						scores.putIfAbsent(index, new DoubleAdder());
						scores.get(index).add(score);
					});
				}
			}
		}

		// sort the scores map, limit the number of results, fetch data from
		// user
		// object, format the result and return it
		return scores.entrySet().stream().sorted((e1, e2) -> Double.valueOf(e2.getValue().doubleValue()).compareTo(e1.getValue().doubleValue())).limit(maxResults)
				.map(x -> formatUserResult(users.get(x.getKey()), x.getValue().doubleValue())).collect(Collectors.toList());

	}

	/**
	 * format user object to create single name string and assign score to it
	 * 
	 * @param user
	 *            user object
	 * @param score
	 *            search score
	 * @return formatted result
	 */
	private SearchResult formatUserResult(User user, Double score) {

		SearchResult searchResult = new SearchResult();
		searchResult.setName(String.format("%s %s %s", user.getFirstName(), user.getMiddleName(), user.getLastName()));
		searchResult.setScore(score);

		return searchResult;
	}
}
