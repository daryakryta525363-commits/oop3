
package ru.dota.heroes.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Column;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "teams")
@Data
@EqualsAndHashCode(exclude = "heroes")
@ToString(exclude = "heroes")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Hero> heroes = new ArrayList<>();

    public Team() {
    }

    public Team(String name) {
        this.name = name;
    }

    public void addHero(Hero hero) {
        if (hero == null) {
            return;
        }

        if (hero.getTeam() != null && hero.getTeam() != this) {
            hero.getTeam().removeHero(hero);
        }

        if (!heroes.contains(hero)) {
            heroes.add(hero);
        }

        hero.setTeam(this);
    }

    public void removeHero(Hero hero) {
        if (hero == null) {
            return;
        }

        heroes.remove(hero);

        if (hero.getTeam() == this) {
            hero.setTeam(null);
        }
    }
}
