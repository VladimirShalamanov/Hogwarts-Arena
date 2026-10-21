package main.property;

import lombok.Data;
import main.config.YamlPropertySourceFactory;
import main.model.SpellAlignment;
import main.model.SpellCategory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties
@PropertySource(value = "spells.yaml", factory = YamlPropertySourceFactory.class)
public class SpellsProperties {

    // TODO: Load spell properties here.
    // Hint: Make an inner public class here that matches the structure of spells.yaml file

    private List<Spells> spells;

    @Data
    public static class Spells {

        private String code;
        private String name;
        private String image;
        private SpellCategory category;
        private SpellAlignment alignment;
        private int minLearned;
        private int power;
        private String description;
    }
}
