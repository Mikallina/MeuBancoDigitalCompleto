package br.com.cdb.MeuBancoDigitalCompleto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cdb.MeuBancoDigitalCompleto.repository.LoginRepository;
import br.com.cdb.MeuBancoDigitalCompleto.service.LoginService;

@Controller
@RequestMapping("/home")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "home/login"; 
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, Model model) {
        boolean autenticado = loginService.autenticar(email, senha);
        if (autenticado) {
            model.addAttribute("message", "Login bem-sucedido!");
            System.out.println("Login Efetuado com Sucesso");
            return "redirect:/menuprincipal/menuprincipal"; 
        } else {
            model.addAttribute("message", "Email ou senha inválidos!");
            return "home/login";  
        }
    }
}