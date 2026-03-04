package com.personalproject.eureka_server_plan_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerPlanManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerPlanManagementApplication.class, args);
	}

}
