package com.iso.plogues.tree.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iso.plogues.proof.model.service.ProofService;
import com.iso.plogues.tree.model.dao.TreeMapper;
import com.iso.plogues.tree.model.dto.TreeCountDto;
import com.iso.plogues.tree.model.dto.TreeDto;
import com.iso.plogues.tree.model.dto.TreeResponse;
import com.iso.plogues.tree.model.vo.CarbonReductionResponse;
import com.iso.plogues.tree.model.vo.Tree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class TreeService {

	private final TreeMapper treeMapper;
	private final ProofService proofService;

	@Transactional
	public void saveTreeEnvironments(TreeDto treeDto) {
		treeMapper.saveTreeEnvironments(Tree.builder()
				                            .zoneName(treeDto.getZoneName())
				                            .humidity(treeDto.getHumidity())
				                            .soilMoisture(treeDto.getSoilMoisture())
				                            .temperature(treeDto.getTemperature())
				                            .build());
	}
	
	//나무 환경 센서 일주일 데이터 조회
	public List<TreeDto> findDataByWeek() {
		return treeMapper.findDataByWeek();
	}
	
	public List<TreeResponse> findDataByDay() {
		return treeMapper.findDataByDay();
	}
	
	//연도별 탄소 감축량 예측
	public List<CarbonReductionResponse> getCarbonReductionData() {
	    List<TreeCountDto> treeCounts = getTreeCountByYear();

	    // 식재 데이터가 없으면 그래프에 보여줄 데이터도 없으므로 빈 리스트를 반환한다.
	    // 없으면 예외할 시 홈페이지에 띄워줄게 아무것도 안뜨니까 빈 리스트로 보여줌.
	    if (treeCounts.isEmpty()) {
	        return Collections.emptyList();
	    }

	    // 나무 1그루가 성목일 때 1년에 흡수한다고 가정하는 CO2 양이다.
	    final double MATURE_CO2_PER_TREE = 22.0;

	    // 묘목이 성목까지의 기간이다.
	    final int GROWTH_YEARS = 10;

	    // 시작연도
	    int startYear = treeCounts.get(0).getYear();

	    // 마지막 식재연도
	    int lastPlantYear = treeCounts.get(treeCounts.size() - 1).getYear();

	    // 마지막에 심은 나무가 성장기간을 채울 때까지 그래프를 보여준다.
	    int endYear = lastPlantYear + GROWTH_YEARS - 1;

	    // 계산된 그래프 데이터를 담을 리스트
	    List<CarbonReductionResponse> result = new ArrayList<>();

	    // 전체 기간 동안 누적된 CO2 감축량
	    double cumulativeReduction = 0;

	    // 시작연도부터 마지막 식재 나무가 성목이 되는 연도까지 반복
	    for (int year = startYear; year <= endYear; year++) {

	        // 해당 연도까지 존재하는 총 나무 수를 계산
	        int totalTreeCount = 0;

	        // 해당 연도에 모든 나무가 흡수한 CO2 총량
	        double yearlyReduction = 0;

	        // 각 식재연도별 나무 묶음을 현재 연도 기준으로 계산
	        for (TreeCountDto treeCount : treeCounts) {

	            // 해당 묶음의 식재연도를 가져온다.
	            int plantedYear = treeCount.getYear();

	            // 해당 묶음의 식재 수를 가져온다.
	            int count = treeCount.getTreeCount();

	            // 아직 심기 전인 나무는 현재 연도 계산에서 제외한다.
	            if (plantedYear > year) {
	                continue;
	            }

	            // 현재 연도 기준 나무 나이를 계산
	            int treeAge = year - plantedYear + 1;

	            // 나무 나이에 따른 성장률을 계산하되 10년 이후에는 100%로 고정
	            double growthRate = Math.min(treeAge, GROWTH_YEARS) / (double) GROWTH_YEARS;

	            // 해당 묶음의 연간 CO2 흡수량을 더한다.
	            yearlyReduction += count * MATURE_CO2_PER_TREE * growthRate;

	            // 현재 연도까지 심어진 총 나무 수를 더한다.
	            totalTreeCount += count;
	        }

	        // 올해 흡수량을 전체 누적 감축량에 더한다.
	        cumulativeReduction += yearlyReduction;

	        // 프론트 그래프에서 쓰기 좋게 소수점 한 자리로 반올림해서 결과에 담는다.

	        result.add(CarbonReductionResponse.builder()
	        		                          .year(year)
	        		                          .totalTreeCount(totalTreeCount)
	        		                          .yearlyReduction(Math.round(yearlyReduction * 10) / 10.0)
	        		                          .cumulativeReduction(Math.round(cumulativeReduction * 10) / 10.0)
	        		                          .build());
	    }

	    return result;
	}
	
	private List<TreeCountDto> getTreeCountByYear() {
		return proofService.getTreeCountByYear();
	}
	

	
}
