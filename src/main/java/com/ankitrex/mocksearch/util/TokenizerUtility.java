package com.ankitrex.mocksearch.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ankitrex.mocksearch.entity.User;

@Component
public class TokenizerUtility {

	/**
	 * generate ngram token for each User object's field
	 * 
	 * @param user
	 *            user object
	 * @return String tokens
	 */
	public List<String> tokenizeUserNgram(User user) {

		List<String> tokens = new ArrayList<>();
		tokens.addAll(formatAndBreakStringTokenNgram(user.getFirstName()));
		tokens.addAll(formatAndBreakStringTokenNgram(user.getMiddleName()));
		tokens.addAll(formatAndBreakStringTokenNgram(user.getLastName()));

		return tokens;
	}

	/**
	 * ngram tokenize the query in same way it was indexed
	 * 
	 * @param query
	 *            search query term
	 * @return ngram tokens
	 */
	public List<String> tokenizeQueryNgram(String query) {

		return formatAndBreakStringTokenNgram(query);
	}

	/**
	 * format string and generate ngram tokens with minimum gram weight taken
	 * from constants (3 as of now). ankit will be broke into - ank, anki,
	 * ankit, nki, nkit, kit
	 * 
	 * @param keyword
	 *            string to be tokenized
	 * @return List<String> - tokens
	 */
	private List<String> formatAndBreakStringTokenNgram(String keyword) {

		String formattedToken = keyword.toLowerCase().trim();

		List<String> tokens = new ArrayList<>();

		for (int i = 0; i <= formattedToken.length() - Constants.MINIMUM_TOKEN_LENGTH; i++) {
			for (int j = i; j <= formattedToken.length() - Constants.MINIMUM_TOKEN_LENGTH; j++) {
				tokens.add(formattedToken.substring(i, j + Constants.MINIMUM_TOKEN_LENGTH));
			}
		}

		return tokens;
	}

	/**
	 * generate full keyword tokens for first, middle and last names
	 * 
	 * @param user
	 *            user object
	 * @return tokens
	 */
	public List<String> tokenizeUserKeyword(User user) {

		List<String> tokens = new ArrayList<>();

		tokens.add(formatAndBreakStringTokenKeyword(user.getFirstName()));
		tokens.add(formatAndBreakStringTokenKeyword(user.getMiddleName()));
		tokens.add(formatAndBreakStringTokenKeyword(user.getLastName()));

		return tokens;
	}

	/**
	 * tokenize keywords in the query in same way it was indexed
	 * 
	 * @param query
	 *            search query term
	 * @return keyword tokens
	 */
	public String tokenizeQueryKeyword(String query) {

		return formatAndBreakStringTokenKeyword(query);
	}

	/**
	 * lowercase and trim the string token
	 * 
	 * @param token
	 *            input string
	 * @return formatted token
	 */
	private String formatAndBreakStringTokenKeyword(String token) {

		return token.toLowerCase().trim();
	}
}
