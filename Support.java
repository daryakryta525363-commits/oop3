
package ru.dota.heroes.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@DiscriminatorValue("SUPPORT")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Support extends Hero {

    @Min(0)
    private int mana = 100;

    @Min(0)
    private int healPower = 20;

    public Support() {
        super("New Support", 100, 10);
    }

    public Support(String name, int health, int damage,
                   int mana, int healPower) {
        super(name, health, damage);
        this.mana = Math.max(0, mana);
        this.healPower = Math.max(0, healPower);
    }

    @Override
    public String getRoleName() {
        return "Саппорт";
    }

    @Override
    public String getSpecificInfo() {
        return "Мана: " + mana
                + ", сила лечения: " + healPower;
    }

    /**
     * Использование способности саппорта:
     * лечит самого себя и расходует 10 маны.
     */
    @Override
    public String useAbility() {
        if (!isAlive()) {
            return getName()
                    + " погиб и не может использовать способность.";
        }

        if (mana < 10) {
            return getName()
                    + " не может использовать способность: недостаточно маны.";
        }

        int restoredHealth = healPower;
        setHealth(getHealth() + restoredHealth);
        setMana(mana - 10);

        return getName() + " использует лечение на "
                + restoredHealth
                + " HP. Здоровье: " + getHealth()
                + ", мана: " + mana;
    }

    /**
     * Лечение другого героя.
     */
    public String heal(Hero target) {
        if (!isAlive()) {
            return getName() + " погиб и не может лечить.";
        }

        if (target == null) {
            return "Не выбрана цель для лечения.";
        }

        if (!target.isAlive()) {
            return "Нельзя лечить погибшего героя.";
        }

        if (target.getId() != null
                && target.getId().equals(getId())) {
            return "Нельзя лечить самого себя этим методом.";
        }

        if (mana < 10) {
            return "Недостаточно маны для лечения. Нужно 10 маны.";
        }

        target.setHealth(target.getHealth() + healPower);
        setMana(mana - 10);

        return getName() + " лечит " + target.getName()
                + " на " + healPower
                + " HP. Здоровье цели: " + target.getHealth()
                + ", мана саппорта: " + mana;
    }
}
