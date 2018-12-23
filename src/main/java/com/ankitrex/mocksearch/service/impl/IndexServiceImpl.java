package com.ankitrex.mocksearch.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ankitrex.mocksearch.entity.User;
import com.ankitrex.mocksearch.service.IndexService;
import com.ankitrex.mocksearch.util.TokenizerUtility;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

	@Autowired
	TokenizerUtility tokenizerUtility;

	@Value("${index.source.file.path}")
	private String filePath;
	
	@Value("${index.source.file.name}")
	private String fileName;

	private List<User> users;

	private Map<String, List<Integer>> ngramInvertedIndex = new HashMap<>();

	private SortedMap<String, List<Integer>> keywordInvertedIndex = new TreeMap<>();

	@Override
	@PostConstruct
	public void tokenizeAndIndexUserData() {

		users = readDataFromCsv(filePath + fileName);

		for (int i = 0; i < users.size(); i++) {

			User user = users.get(i);

			List<String> ngramTokens = tokenizerUtility.tokenizeUserNgram(user);
			for (String token : ngramTokens) {

				ngramInvertedIndex.putIfAbsent(token, new ArrayList<Integer>());
				ngramInvertedIndex.get(token).add(i);
			}
			
			List<String> keywordTokens = tokenizerUtility.tokenizeUserKeyword(user);
			for(String token : keywordTokens) {
				
				keywordInvertedIndex.putIfAbsent(token, new ArrayList<Integer>());
				keywordInvertedIndex.get(token).add(i);
			}
		}
	}

	@Override
	public List<User> getIndexedUsers() {

		return users;
	}

	@Override
	public Map<String, List<Integer>> getNgramInvertedIndex() {

		return ngramInvertedIndex;
	}
	

	@Override
	public SortedMap<String, List<Integer>> getKeywordInvertedIndex() {

		return keywordInvertedIndex;
	}


	@SuppressWarnings("unchecked")
	private List<User> readDataFromCsv(String source) {

		try (Reader reader = Files.newBufferedReader(Paths.get(source))) {

			@SuppressWarnings("rawtypes")
			CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader).withType(User.class)
					.withIgnoreLeadingWhiteSpace(true).build();

			return csvToBean.parse();
		} catch (FileNotFoundException e) {
			log.error(String.format("%s file not found", fileName), e);
		} catch (IOException e1) {
			log.error(String.format("error reading file %s", fileName), e1);
		}

		return new ArrayList<>();
	}
}
