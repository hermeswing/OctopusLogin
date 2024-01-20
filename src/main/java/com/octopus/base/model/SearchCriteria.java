package com.octopus.base.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SearchCriteria {
	private String searchKey;
	private String searchOperation;
	private Object searchValue;
}
