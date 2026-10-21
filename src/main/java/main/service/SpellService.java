package main.service;

import main.model.Spell;
import main.model.Wizard;
import main.property.SpellsProperties;
import main.repository.SpellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SpellService {

    private final SpellRepository spellRepository;
    private final SpellsProperties spellsProperties;

    @Autowired
    public SpellService(SpellRepository spellRepository, SpellsProperties spellsProperties) {
        this.spellRepository = spellRepository;
        this.spellsProperties = spellsProperties;
    }

    public Spell getRandomSpell(Wizard wizard) {

        Random random = new Random();

        List<SpellsProperties.Spells> spells = spellsProperties.getSpells();

        List<SpellsProperties.Spells> spellsZero = spells
                .stream()
                .filter(s -> s.getMinLearned() == 0)
                .toList();

        int all = spellsZero.size();
        int index = random.nextInt(1, all);

        SpellsProperties.Spells currSpell = spellsZero.get(index);

        Spell newSpell = Spell.builder()
                .code(currSpell.getCode())
                .name(currSpell.getName())
                .description(currSpell.getDescription())
                .wizard(wizard)
                .category(currSpell.getCategory())
                .alignment(currSpell.getAlignment())
                .image(currSpell.getImage())
                .power(currSpell.getPower())
                .createdOn(LocalDateTime.now())
                .build();

        spellRepository.save(newSpell);

        return newSpell;
    }
}
