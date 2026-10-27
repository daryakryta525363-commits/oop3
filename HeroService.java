
package ru.dota.heroes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.dota.heroes.model.Hero;
import ru.dota.heroes.repository.HeroRepository;

import java.util.List;

@Service
public class HeroService {

    private final HeroRepository heroRepository;

    public HeroService(HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    // Получить всех героев
    public List<Hero> findAll() {
        return heroRepository.findAll();
    }

    // Найти героя по ID
    public Hero findById(Long id) {
        return heroRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Герой с ID " + id + " не найден"));
    }

    // Сохранить нового героя
    @Transactional
    public Hero save(Hero hero) {
        return heroRepository.save(hero);
    }

    // Обновить существующего героя
    @Transactional
    public Hero update(Long id, Hero updatedHero) {
        Hero existingHero = findById(id);

        existingHero.setName(updatedHero.getName());
        existingHero.setHealth(updatedHero.getHealth());
        existingHero.setDamage(updatedHero.getDamage());

        return heroRepository.save(existingHero);
    }

    // Удалить героя
    @Transactional
    public void delete(Long id) {
        Hero hero = findById(id);
        heroRepository.delete(hero);
    }
}
