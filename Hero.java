
package ru.dota.heroes.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "heroes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "hero_type")
@Data
@EqualsAndHashCode(exclude = "team")
@ToString(exclude = "team")
public abstract class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Min(0)
    @Column(nullable = false)
    private int health;

    @Min(0)
    @Column(nullable = false)
    private int damage;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    protected Hero() {
    }

    public Hero(String name, int health, int damage) {
        this.name = name;
        this.health = Math.max(0, health);
        this.damage = Math.max(0, damage);
    }

    public abstract String getRoleName();

    public abstract String getSpecificInfo();

    public abstract String useAbility();

    public String attack(Hero target) {
        if (!isAlive()) {
            return name + " погиб и не может атаковать.";
        }

        if (target == null || !target.isAlive()) {
            return "Нельзя атаковать отсутствующую цель или погибшего героя.";
        }

        target.takeDamage(damage);

        return name + " атакует " + target.getName()
                + ". Урон: " + damage
                + ". У " + target.getName()
                + " осталось HP: " + target.getHealth();
    }

    public void takeDamage(int damage) {
        if (damage > 0) {
            health = Math.max(0, health - damage);
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public String getStatusText() {
        return isAlive() ? "Жив" : "Погиб";
    }

    public String getInfo() {
        return name
                + " | Роль: " + getRoleName()
                + " | HP: " + health
                + " | Урон: " + damage
                + " | " + getSpecificInfo();
    }
}
