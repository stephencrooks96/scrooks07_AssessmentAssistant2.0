package com.pgault04;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Schedules an empty task every ten minutes
 * Prevents shutdown hook due to inactivity
 *
 * @author Paul Gault - 40126005
 * @since March 2019
 */
@Configuration
@EnableScheduling
public class ThreadStarvationPreventer {

    @Scheduled(fixedDelay = 600000)
    public void scheduleReadPrices() {}
}
