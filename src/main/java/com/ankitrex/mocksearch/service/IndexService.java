package com.ankitrex.mocksearch.service;

import java.util.List;
import java.util.Map;

import com.ankitrex.mocksearch.entity.User;

public interface IndexService {

	void tokenizeAndIndexUserData();

	List<User> getIndexedUsers();

	Map<String, List<Integer>> getInvertedIndex();
}
