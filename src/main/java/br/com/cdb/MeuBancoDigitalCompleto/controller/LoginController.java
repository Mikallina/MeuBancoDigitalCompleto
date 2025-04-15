package br.com.cdb.MeuBancoDigitalCompleto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.cdb.MeuBancoDigitalCompleto.service.LoginService;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Retorna a view do formulário de login
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        boolean autenticado = loginService.autenticar(username, password);
        if (autenticado) {
            model.addAttribute("message", "Login bem-sucedido!");
            return "redirect:/home"; // Redireciona após login bem-sucedido
        } else {
            model.addAttribute("message", "Email ou senha inválidos!");
            return "home/login"; // Retorna à página de login em caso de falha
        }
    }
}