package com.iso.plogues.tree.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iso.plogues.api.model.vo.ApiResponse;
import com.iso.plogues.tree.model.dto.TreeDto;
import com.iso.plogues.tree.model.service.TreeService;
import com.iso.plogues.tree.model.vo.CarbonReductionResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tree")
@Slf4j
public class TreeController {
	private final TreeService treeService;
	
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> saveTreeEnvironment(@RequestBody@Valid TreeDto treeDto){
		log.info("{}", treeDto);
		treeService.saveTreeEnvironments(treeDto);
		return ResponseEntity.ok().body(ApiResponse.success("센서 데이터를 성공적으로 저장했습니다.", null));
	}
	
	@GetMapping("/week")
	public ResponseEntity<ApiResponse<List<TreeDto>>> getWeeklyData() {
	    return ResponseEntity.ok().body(ApiResponse.success(treeService.findDataByWeek()));
	}

	@GetMapping("/carbon-reduction")
	public ResponseEntity<ApiResponse<List<CarbonReductionResponse>>> getCarbonReduction() {
	    return ResponseEntity.ok().body(ApiResponse.success(treeService.getCarbonReductionData()));
	}
}
