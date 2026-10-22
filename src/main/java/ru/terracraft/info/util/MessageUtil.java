package ru.terracraft.info.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.terracraft.info.TerraCraftInfoPlugin;
import ru.terracraft.info.lang.Lang;
import ru.terracraft.info.lang.LangManager;

import java.util.Map;

/**
 * Утилита: получить строку из LangManager + PAPI + MiniMessage → отправить.
 * PAPI-плейсхолдеры (%...%) заменяются на unparsed-теги <papi_N>,
 * чтобы их содержимое не интерпретировалось как MiniMessage-разметка.
 */
public final class MessageUtil {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    private MessageUtil() {}

    private static boolean papiAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Парсит MiniMessage + PAPI для игрока.
     * PAPI-значения вставляются через unparsed, поэтому символы &lt; &gt;
     * в них не ломают MiniMessage-разметку.
     */
    public static Component parse(String raw, Player player) {
        if (raw == null || raw.isEmpty()) return Component.empty();

        if (player == null || !papiAvailable() || !raw.contains("%")) {
            return MM.deserialize(raw);
        }

        TagResolver.Builder resolver = TagResolver.builder();
        StringBuilder sb = new StringBuilder(raw);
        int index = 0;
        int counter = 0;

        while ((index = sb.indexOf("%", index)) != -1) {
            int end = sb.indexOf("%", index + 1);
            if (end == -1) break;

            String placeholder = sb.substring(index, end + 1); // "%player_name%"
            String value = PlaceholderAPI.setPlaceholders(player, placeholder);
            String tagName = "papi_" + (counter++);

            resolver.resolver(Placeholder.unparsed(tagName, value));
            sb.replace(index, end + 1, "<" + tagName + ">");
            index += tagName.length() + 2;
        }

        return MM.deserialize(sb.toString(), resolver.build());
    }

    /** Отправить сообщение по ключу Lang с подстановками и префиксом. */
    public static void send(CommandSender sender, Lang key, Map<String, String> placeholders) {
        Player player = sender instanceof Player p ? p : null;
        LangManager lang = TerraCraftInfoPluginHolder.lang();
        String raw = lang.raw(key, player, placeholders);
        String prefix = lang.raw(Lang.PREFIX, player);
        sender.sendMessage(parse(prefix + raw, player));
    }

    /** Отправить сообщение по ключу Lang без подстановок. */
    public static void send(CommandSender sender, Lang key) {
        send(sender, key, Map.of());
    }

    /** Отправить уже готовый текст. */
    public static void sendRaw(CommandSender sender, String raw) {
        Player player = sender instanceof Player p ? p : null;
        sender.sendMessage(parse(raw, player));
    }

    private static final class TerraCraftInfoPluginHolder {
        static LangManager lang() {
            return TerraCraftInfoPlugin.getInstance().getLangManager();
        }
    }
}