package com.ankitrex.mocksearch.entity;

import com.ankitrex.mocksearch.util.Constants;
import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class User {

	@CsvBindByName(column = Constants.FIRST_NAME_HEADER)
	private String	firstName;

	@CsvBindByName(column = Constants.MIDDLE_NAME_HEADER)
	private String	middleName;

	@CsvBindByName(column = Constants.LAST_NAME_HEADER)
	private String	lastName;
}
