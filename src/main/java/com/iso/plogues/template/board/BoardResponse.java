package com.iso.plogues.template.board;

import java.util.List;

import com.iso.plogues.page.PageInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponse<T> {
	PageInfo page;
	List<T> board; 

}