package com.example.rsservice;

import com.example.rsservice.model.StatementDetail;
import com.example.rsservice.service.AuthorizedStatementConsumer;
import com.example.rsservice.service.StatementDetailConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RsserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsserviceApplication.class, args);
	}

	@Bean(initMethod="init")
	public StatementDetailConsumer statementConsumerBean() {
		return new StatementDetailConsumer();
	}

	@Bean(initMethod="init")
	public AuthorizedStatementConsumer authorizedStatementConsumerBean() {
		return new AuthorizedStatementConsumer();
	}
}
