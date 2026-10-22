package ru.terracraft.info.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

@Configuration
public class InfoConfig {

    @Comment("Язык по умолчанию (для консоли и как fallback)")
    public String language = "ru_RU";

    @Comment("Fallback язык, если ключ не найден в языке игрока")
    public String fallbackLanguage = "en_US";

    public PaginationSection pagination = new PaginationSection();

    public HelpSection help = new HelpSection();

    @Configuration
    public static class PaginationSection {
        @Comment("Строк на страницу")
        public int linesPerPage = 8;
    }

    @Configuration
    public static class HelpSection {
        @Comment("true — группы без совпадения с LuckPerms скрываются; " +
                "false — показываются все (отладка)")
        public boolean strictGroupCheck = true;
    }
}