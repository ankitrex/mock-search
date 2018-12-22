package com.ankitrex.mocksearch.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ankitrex.mocksearch.entity.User;

@Component
public class TokenizerUtility {

	public List<String> tokenizeUser(User user) {

		List<String> tokens = new ArrayList<>();
		tokens.addAll(formatAndBreakStringToken(user.getFirstName()));
		tokens.addAll(formatAndBreakStringToken(user.getMiddleName()));
		tokens.addAll(formatAndBreakStringToken(user.getLastName()));

		return tokens;
	}

	public List<String> tokenizeQuery(String query) {

		return formatAndBreakStringToken(query);
	}

	private List<String> formatAndBreakStringToken(String token) {

		String[] formattedTokens = token.toLowerCase().trim().split("\\s+");

		List<String> tokens = new ArrayList<>();

		for (String s : formattedTokens) {
			for (int i = 0; i <= s.length() - Constants.TOKEN_LENGTH; i++) {
				for (int j = i; j <= s.length() - Constants.TOKEN_LENGTH; j++) {
					tokens.add(s.substring(i, j + Constants.TOKEN_LENGTH));
				}
			}
		}

		return tokens;
	}
}
