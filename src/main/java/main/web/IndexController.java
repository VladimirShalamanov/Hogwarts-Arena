package main.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import main.model.Wizard;
import main.service.SpellService;
import main.service.WizardService;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {
    // TODO: Implement the necessary here.

    private final WizardService wizardService;
//    private final SpellService spellService;

    @Autowired
    public IndexController(WizardService wizardService, SpellService spellService) {
        this.wizardService = wizardService;
//        this.spellService = spellService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {

        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public String register(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        wizardService.register(registerRequest);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpSession session) {

        Wizard wizard = wizardService.login(loginRequest);
        session.setAttribute("user_id", wizard.getId());

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/home")
    public ModelAndView getLobbyPage(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        Wizard wizard = wizardService.getById(userId);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("wizard", wizard);
        modelAndView.addObject("wSpells", wizard.getSpells());

        return modelAndView;
    }
}


