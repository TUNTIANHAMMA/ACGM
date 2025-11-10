package com.acgm.acgmmediatracker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.acgm.acgmmediatracker.mapper")

public class AcgmMediaTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcgmMediaTrackerApplication.class, args);
	}

}
