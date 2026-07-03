package com.iso.plogues.tree.model.service;

import org.springframework.stereotype.Service;

import com.iso.plogues.tree.model.dao.TreeMapper;
import com.iso.plogues.tree.model.dto.TreeDto;
import com.iso.plogues.tree.model.vo.Tree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TreeService {

	private final TreeMapper treeMapper;
	
	public void saveTreeEnvironments(TreeDto treeDto) {
		treeMapper.saveTreeEnvironments(Tree.builder()
				                            .zoneName(treeDto.getZoneName())
				                            .humidity(treeDto.getHumidity())
				                            .soilMoisture(treeDto.getSoilMoisture())
				                            .temperature(treeDto.getTemperature())
				                            .build());
	}
}
