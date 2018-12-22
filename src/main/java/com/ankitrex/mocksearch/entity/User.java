package com.ankitrex.mocksearch.entity;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class User {

	@CsvBindByName(column = "First Name")
	private String	firstName;

	@CsvBindByName(column = "Middle Name")
	private String	middleName;

	@CsvBindByName(column = "Last Name")
	private String	lastName;
}
