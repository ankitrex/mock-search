package com.ankitrex.mocksearch.controller;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankitrex.mocksearch.entity.User;
import com.ankitrex.mocksearch.service.IndexService;

import lombok.extern.slf4j.Slf4j;

/**
 * only for using in dev environment to check inverted indices and users.
 * change profile to dev in application.properties to expose these endpoints
 * 
 * @author ankit
 *
 */
@RestController
@Profile("dev")
public class IndexController {

	@Autowired
	IndexService indexService;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		
		return indexService.getIndexedUsers();
	}

	@GetMapping("/ngram-tokens")
	public Map<String, List<Integer>> getAllNgramTokens() {

		return indexService.getNgramInvertedIndex();
	}
	
	@GetMapping("/keyword-tokens")
	public Map<String, List<Integer>> getAllKeywordTokens() {

		return indexService.getKeywordInvertedIndex();
	}
}
