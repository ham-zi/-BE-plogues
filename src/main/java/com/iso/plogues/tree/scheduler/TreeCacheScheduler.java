package com.iso.plogues.tree.scheduler;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TreeCacheScheduler {
	
	@CacheEvict(value="dailyTreeData", allEntries = true)
	@Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
	public void clearDailyTreeCache() {
	}
}

