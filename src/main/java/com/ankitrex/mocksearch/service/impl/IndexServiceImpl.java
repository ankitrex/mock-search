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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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
	TokenizerUtility						tokenizerUtility;

	private String							filePath		= "src/main/resources/";
	private String							fileName		= "data2.csv";

	private List<User>						users;

	private Map<String, List<Integer>>	invertedIndex	= new HashMap<>();

	@Override
	@PostConstruct
	public void tokenizeAndIndexUserData() {

		users = readDataFromCsv(filePath + fileName);

		for (int i = 0; i < users.size(); i++) {

			User user = users.get(i);

			List<String> tokens = tokenizerUtility.tokenizeUser(user);
			for (String token : tokens) {

				invertedIndex.putIfAbsent(token, new ArrayList<Integer>());
				invertedIndex.get(token).add(i);
			}
		}
	}

	@Override
	public List<User> getIndexedUsers() {

		return users;
	}

	@Override
	public Map<String, List<Integer>> getInvertedIndex() {

		return invertedIndex;
	}

	@SuppressWarnings("unchecked")
	private List<User> readDataFromCsv(String source) {

		try (Reader reader = Files.newBufferedReader(Paths.get(source))) {

			@SuppressWarnings("rawtypes")
			CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader).withType(User.class).withIgnoreLeadingWhiteSpace(true).build();

			return csvToBean.parse();
		}
		catch (FileNotFoundException e) {
			log.error(String.format("%s file not found", fileName), e);
		}
		catch (IOException e1) {
			log.error(String.format("error reading file %s", fileName), e1);
		}

		return new ArrayList<>();
	}

}
