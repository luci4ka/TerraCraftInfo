package ru.net.terracraft.info.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.net.terracraft.info.TerraCraftInfoPlugin;
import ru.net.terracraft.info.config.InfoConfig;
import ru.net.terracraft.info.lang.Lang;
import ru.net.terracraft.info.util.MessageUtil;

public class LinksCommand {

    private final TerraCraftInfoPlugin plugin;

    public LinksCommand(TerraCraftInfoPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        new CommandAPICommand("links")
                .withAliases("ссылки")
                .executes((CommandSender sender, CommandArguments args) -> {
                    execute(sender);
                    return 1;
                })
                .register();
    }

    private void execute(CommandSender sender) {
        var cfg = plugin.getInfoConfig();
        var lang = plugin.getLangManager();
        Player player = sender instanceof Player p ? p : null;

        sender.sendMessage(MessageUtil.parse(lang.raw(Lang.LINKS_HEADER, player), player));

        String format = lang.raw(Lang.LINKS_ENTRY_FORMAT, player);
        String hoverOpen = lang.raw(Lang.LINKS_HOVER_OPEN, player);

        for (InfoConfig.LinkEntry entry : cfg.links.entries) {
            String line = format
                    .replace("{display}", entry.display())
                    .replace("{url}", entry.url());

            Component msg = MessageUtil.parse(line, player)
                    .clickEvent(ClickEvent.openUrl(entry.url()))
                    .hoverEvent(HoverEvent.showText(
                            MessageUtil.parse(
                                    (hoverOpen == null ? "<gray>Открыть: <white>{url}</white></gray>" : hoverOpen)
                                            .replace("{url}", entry.url()),
                                    player)));

            sender.sendMessage(msg);
        }
    }
}