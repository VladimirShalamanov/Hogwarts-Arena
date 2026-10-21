package main.service;

import main.exception.CustomException;
import main.model.Spell;
import main.model.Wizard;
import main.repository.WizardRepository;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class WizardService {

    private final WizardRepository wizardRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpellService spellService;

    @Autowired
    public WizardService(WizardRepository wizardRepository, PasswordEncoder passwordEncoder, SpellService spellService) {
        this.wizardRepository = wizardRepository;
        this.passwordEncoder = passwordEncoder;
        this.spellService = spellService;
    }

    public void register(RegisterRequest registerRequest) {

        Wizard wizard = Wizard.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .avatarUrl(registerRequest.getAvatarUrl())
                .house(registerRequest.getHouse())
                .alignment(registerRequest.getWizardAlignment())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        // not save in DB (should add 2 saves wizard)
        wizardRepository.save(wizard);

        Spell randonSpell = spellService.getRandomSpell(wizard);

        wizard.setSpells(randonSpell);

        wizardRepository.save(wizard);
    }

    public Wizard login(LoginRequest loginRequest) {
        Wizard wizard = wizardRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new CustomException("Username or password incorrect!"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), wizard.getPassword())) {
            throw new RuntimeException("Username or password incorrect!");
        }

        return wizard;
    }

    public Wizard getById(UUID userId) {

        return wizardRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Wizard not found"));
    }
}
