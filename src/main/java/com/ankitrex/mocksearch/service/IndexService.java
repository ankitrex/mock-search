package com.ankitrex.mocksearch.service;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.ankitrex.mocksearch.entity.User;

public interface IndexService {

	/**
	 * fetch data from csv, create inverted index for searching and keep it in
	 * memory for quick access.
	 * 
	 */
	void tokenizeAndIndexUserData();

	/**
	 * get list of indexed users.
	 * 
	 * @return all users
	 */
	List<User> getIndexedUsers();

	/**
	 * get ngram tokenized inverted index from memory.
	 * 
	 * @return ngram inverted index
	 */
	Map<String, List<Integer>> getNgramInvertedIndex();

	/**
	 * 
	 * get tokenized keywords inverted index from memory.
	 * 
	 * @return keyword inverted index
	 */
	SortedMap<String, List<Integer>> getKeywordInvertedIndex();
}
