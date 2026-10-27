
package ru.dota.heroes.controller;

import ru.dota.heroes.model.Hero;
import ru.dota.heroes.model.Team;
import ru.dota.heroes.repository.HeroRepository;
import ru.dota.heroes.repository.TeamRepository;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final TeamRepository teamRepository;
    private final HeroRepository heroRepository;

    public TeamController(
            TeamRepository teamRepository,
            HeroRepository heroRepository) {
        this.teamRepository = teamRepository;
        this.heroRepository = heroRepository;
    }

    // Список команд
    @GetMapping
    public String listTeams(Model model) {
        model.addAttribute("teams", teamRepository.findAll());
        return "teams";
    }

    // Форма создания команды
    @GetMapping("/new")
    public String newTeamForm(Model model) {
        model.addAttribute("team", new Team());
        return "team-form";
    }

    // Создание команды
    @PostMapping
    public String createTeam(
            @RequestParam("name") String name,
            RedirectAttributes redirectAttributes) {

        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error", "Введите название команды");
            return "redirect:/teams/new";
        }

        Team team = new Team(name.trim());
        teamRepository.save(team);

        redirectAttributes.addFlashAttribute(
                "success", "Команда успешно создана");

        return "redirect:/teams";
    }

    // Информация о команде
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String teamDetails(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Team team = teamRepository.findById(id).orElse(null);

        if (team == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Команда не найдена");
            return "redirect:/teams";
        }

        // Загружаем героев команды внутри транзакции
        team.getHeroes().size();

        // Получаем всех героев и оставляем только свободных
        List<Hero> availableHeroes = heroRepository.findAll()
                .stream()
                .filter(hero -> hero.getTeam() == null)
                .collect(Collectors.toList());

        model.addAttribute("team", team);
        model.addAttribute("availableHeroes", availableHeroes);

        return "team-detail";
    }

    // Добавление героя в команду
    @PostMapping("/{teamId}/add-hero")
    public String addHero(
            @PathVariable Long teamId,
            @RequestParam Long heroId,
            RedirectAttributes redirectAttributes) {

        Team team = teamRepository.findById(teamId).orElse(null);
        Hero hero = heroRepository.findById(heroId).orElse(null);

        if (team == null || hero == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Команда или герой не найдены");
            return "redirect:/teams";
        }

        if (hero.getTeam() != null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Этот герой уже состоит в команде");
            return "redirect:/teams/" + teamId;
        }

        hero.setTeam(team);
        heroRepository.save(hero);

        redirectAttributes.addFlashAttribute(
                "success", "Герой добавлен в команду");

        return "redirect:/teams/" + teamId;
    }

    // Удаление героя из команды
    @PostMapping("/{teamId}/remove-hero")
    public String removeHero(
            @PathVariable Long teamId,
            @RequestParam Long heroId,
            RedirectAttributes redirectAttributes) {

        Team team = teamRepository.findById(teamId).orElse(null);
        Hero hero = heroRepository.findById(heroId).orElse(null);

        if (team == null || hero == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Команда или герой не найдены");
            return "redirect:/teams";
        }

        if (hero.getTeam() != null
                && hero.getTeam().getId().equals(teamId)) {

            hero.setTeam(null);
            heroRepository.save(hero);

            redirectAttributes.addFlashAttribute(
                    "success", "Герой удалён из команды");
        }

        return "redirect:/teams/" + teamId;
    }
}
