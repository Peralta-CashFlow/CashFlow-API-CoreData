package com.cashflow.coredata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.cashflow.coredata",
		"com.cashflow.exception.core.handler",
		"com.cashflow.auth"
})
public class ApiCoreDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCoreDataApplication.class, args);
	}

}
