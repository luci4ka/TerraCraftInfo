package ru.net.terracraft.info.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.net.terracraft.info.TerraCraftInfoPlugin;
import ru.net.terracraft.info.lang.Lang;
import ru.net.terracraft.info.lang.LangManager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class PaginationUtil {

    private PaginationUtil() {}

    public static <T> void sendPaginated(
            CommandSender sender,
            List<T> items,
            int page,
            int perPage,
            String command,
            String title,
            Function<T, Component> renderer
    ) {
        TerraCraftInfoPlugin plugin = TerraCraftInfoPlugin.getInstance();
        LangManager lang = plugin.getLangManager();
        Player player = sender instanceof Player p ? p : null;

        int totalPages = Math.max(1, (int) Math.ceil((double) items.size() / perPage));
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        // Заголовок
        sender.sendMessage(MessageUtil.parse(
                lang.raw(Lang.PAGE_HEADER, player, Map.of(
                        "title", title,
                        "page", String.valueOf(page),
                        "total", String.valueOf(totalPages)
                )),
                player
        ));

        // Строки
        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, items.size());
        for (int i = start; i < end; i++) {
            sender.sendMessage(renderer.apply(items.get(i)));
        }

        // Навигация
        sender.sendMessage(buildFooter(command, page, totalPages, lang, player));
    }

    private static Component buildFooter(
            String command, int currentPage, int totalPages,
            LangManager lang, Player player
    ) {
        Component prev = MessageUtil.parse(lang.raw(Lang.PAGE_PREV_LABEL, player), player);
        Component next = MessageUtil.parse(lang.raw(Lang.PAGE_NEXT_LABEL, player), player);

        if (currentPage > 1) {
            prev = prev
                    .clickEvent(ClickEvent.runCommand("/" + command + " " + (currentPage - 1)))
                    .hoverEvent(HoverEvent.showText(
                            MessageUtil.parse(lang.raw(Lang.PAGE_HOVER_PREV, player), player)));
        }
        if (currentPage < totalPages) {
            next = next
                    .clickEvent(ClickEvent.runCommand("/" + command + " " + (currentPage + 1)))
                    .hoverEvent(HoverEvent.showText(
                            MessageUtil.parse(lang.raw(Lang.PAGE_HOVER_NEXT, player), player)));
        }

        Component indicator = MessageUtil.parse(
                lang.raw(Lang.PAGE_INDICATOR, player, Map.of(
                        "page", String.valueOf(currentPage),
                        "total", String.valueOf(totalPages)
                )),
                player
        );

        return Component.empty().append(prev).append(indicator).append(next);
    }
}