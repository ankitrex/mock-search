package com.ankitrex.mocksearch.controller;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankitrex.mocksearch.entity.User;
import com.ankitrex.mocksearch.service.IndexService;

import lombok.extern.slf4j.Slf4j;

@RestController
public class IndexController {

	@Autowired
	IndexService indexService;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		
		return indexService.getIndexedUsers();
	}

	@GetMapping("/tokens")
	public Map<String, List<Integer>> getAllTokens() {

		return indexService.getInvertedIndex();
	}
}
