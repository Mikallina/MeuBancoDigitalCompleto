package br.com.cdb.MeuBancoDigitalCompleto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
public class MeuBancoDigitalCompletoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeuBancoDigitalCompletoApplication.class, args);

	}

}
