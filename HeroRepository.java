
package ru.dota.heroes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dota.heroes.model.Hero;

import java.util.List;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

}
