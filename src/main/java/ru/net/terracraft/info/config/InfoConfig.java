package ru.net.terracraft.info.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Только контент. Все тексты — в lang/*.yml.
 */
@Configuration
public class InfoConfig {

    @Comment("Язык по умолчанию (для консоли и как fallback)")
    public String language = "ru_RU";

    @Comment("Fallback язык, если ключ не найден в языке игрока")
    public String fallbackLanguage = "en_US";

    public PaginationSection pagination = new PaginationSection();
    public RulesSection rules = new RulesSection();
    public InfoSection info = new InfoSection();
    public LinksSection links = new LinksSection();
    public HelpSection help = new HelpSection();

    @Configuration
    public static class PaginationSection {
        @Comment("Строк на страницу")
        public int linesPerPage = 8;
    }

    @Configuration
    public static class RulesSection {
        @Comment("Правила. Можно добавлять/удалять строки — /rules покажет их все.")
        public List<String> list = List.of(
                "<gold>1.</gold> <white>Не гриферить.</white>",
                "<gold>2.</gold> <white>Не использовать читы.</white>"
        );
    }

    @Configuration
    public static class InfoSection {
        @Comment("Строки /info. Поддерживает PlaceholderAPI.")
        public List<String> lines = List.of(
                "<gray>Сервер:</gray> <white>TerraCraft</white>",
                "<gray>Онлайн:</gray> <white>%server_online%</white>"
        );
    }

    @Configuration
    public static class LinksSection {
        @Comment("Ссылки. Добавляй новые — /links покажет все.")
        public List<LinkEntry> entries = List.of(
                new LinkEntry("Discord", "<aqua>Discord</aqua>", "https://discord.gg/terracraft")
        );
    }

    public record LinkEntry(String name, String display, String url) {}

    @Configuration
    public static class HelpSection {
        @Comment("Группы команд. Ключ — группа LuckPerms. 'default' видна всем.")
        public Map<String, GroupEntry> groups = Map.of(
                "default", new GroupEntry(List.of(
                        new CommandEntry("/rules", "Правила сервера", List.of("/правила"))
                ))
        );

        @Comment("true — группы без совпадения с LuckPerms скрываются; " +
                "false — показываются все (отладка)")
        public boolean strictGroupCheck = true;
    }

    public record GroupEntry(List<CommandEntry> commands) {}

    public record CommandEntry(String cmd, String description, List<String> aliases) {}
}