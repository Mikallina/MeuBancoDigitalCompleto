package br.com.cdb.MeuBancoDigitalCompleto.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Login;
import br.com.cdb.MeuBancoDigitalCompleto.repository.LoginRepository;

@Configuration
public class Dados {
	
	@Bean
    public CommandLineRunner loadData(LoginRepository loginRepository) {
        return args -> {
            String email = "admin@admin.com.br";
            String senha = "admin123";
          

            // Verifica se o login já existe
            if (!loginRepository.existsByEmail(email)) {
                Login login = new Login(email, senha);
                loginRepository.save(login);
            }
        };
    }
}
