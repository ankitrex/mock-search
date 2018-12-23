package com.ankitrex.mocksearch.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ankitrex.mocksearch.entity.User;

@Component
public class TokenizerUtility {

	public List<String> tokenizeUserNgram(User user) {

		List<String> tokens = new ArrayList<>();
		tokens.addAll(formatAndBreakStringTokenNgram(user.getFirstName()));
		tokens.addAll(formatAndBreakStringTokenNgram(user.getMiddleName()));
		tokens.addAll(formatAndBreakStringTokenNgram(user.getLastName()));

		return tokens;
	}

	public List<String> tokenizeQueryNgram(String query) {

		return formatAndBreakStringTokenNgram(query);
	}

	private List<String> formatAndBreakStringTokenNgram(String token) {

		String formattedToken = token.toLowerCase().trim();

		List<String> tokens = new ArrayList<>();

		for (int i = 0; i <= formattedToken.length() - Constants.MINIMUM_TOKEN_LENGTH; i++) {
			for (int j = i; j <= formattedToken.length() - Constants.MINIMUM_TOKEN_LENGTH; j++) {
				tokens.add(formattedToken.substring(i, j + Constants.MINIMUM_TOKEN_LENGTH));
			}
		}

		return tokens;
	}

	public List<String> tokenizeUserKeyword(User user) {

		List<String> tokens = new ArrayList<>();

		tokens.add(formatAndBreakStringTokenKeyword(user.getFirstName()));
		tokens.add(formatAndBreakStringTokenKeyword(user.getMiddleName()));
		tokens.add(formatAndBreakStringTokenKeyword(user.getLastName()));

		return tokens;
	}

	public String tokenizeQueryKeyword(String query) {

		return formatAndBreakStringTokenKeyword(query);
	}

	private String formatAndBreakStringTokenKeyword(String token) {

		return token.toLowerCase().trim();
	}
}
