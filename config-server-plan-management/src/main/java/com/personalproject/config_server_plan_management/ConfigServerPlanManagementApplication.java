package com.personalproject.config_server_plan_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerPlanManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerPlanManagementApplication.class, args);
	}

}
