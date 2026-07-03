package com.iso.plogues.util.dto;

import java.util.List;

import com.iso.plogues.util.page.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardResponse<T> {
	private PageInfo page;
	private List<T> board;

}