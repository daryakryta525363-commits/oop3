
package ru.dota.heroes.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@DiscriminatorValue("CARRY")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Carry extends Hero {

    @Min(0)
    @Max(100)
    @Column(name = "crit_chance")
    private int critChance = 15;

    @Min(0)
    private int agility = 10;

    public Carry() {
        super("New Carry", 120, 20);
    }

    public Carry(String name, int health, int damage,
                 int critChance, int agility) {
        super(name, health, damage);
        this.critChance = Math.max(0, Math.min(100, critChance));
        this.agility = Math.max(0, agility);
    }

    @Override
    public String getRoleName() {
        return "Керри";
    }

    @Override
    public String getSpecificInfo() {
        return "Крит: " + critChance
                + "%, ловкость: " + agility;
    }

    @Override
    public String useAbility() {
        setAgility(agility + 5);

        return getName()
                + " повышает ловкость на 5. Ловкость: "
                + agility;
    }

    @Override
    public String attack(Hero target) {
        if (!isAlive()) {
            return getName() + " погиб и не может атаковать.";
        }

        if (target == null || !target.isAlive()) {
            return "Нельзя атаковать отсутствующую цель или погибшего героя.";
        }

        int dmg = getDamage();
        boolean critical = Math.random() * 100 < critChance;

        if (critical) {
            dmg *= 2;
        }

        target.takeDamage(dmg);

        return getName() + " атакует " + target.getName()
                + (critical ? " критическим ударом" : "")
                + ". Урон: " + dmg
                + ". У " + target.getName()
                + " осталось HP: " + target.getHealth();
    }

    public boolean canCriticalHit() {
        return critChance > 0;
    }
}
