package br.com.cdb.MeuBancoDigitalCompleto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cdb.MeuBancoDigitalCompleto.entity.Login;
import br.com.cdb.MeuBancoDigitalCompleto.repository.LoginRepository;

@Service
public class LoginService {
	@Autowired
    private LoginRepository loginRepository;



    public boolean autenticar(String email, String senha) {
        Login login = loginRepository.findByEmail(email);
        if (login == null) {
            return false; 
        }

       
        return true;
    }

}
