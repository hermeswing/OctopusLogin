package com.octopus.base.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListResult<T> extends CommonResult {
	private List<T> list;
	// private CollectionModel<T> collection;
}
