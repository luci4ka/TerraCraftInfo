package ru.net.terracraft.info.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.net.terracraft.info.TerraCraftInfoPlugin;
import ru.net.terracraft.info.lang.Lang;
import ru.net.terracraft.info.util.MessageUtil;
import ru.net.terracraft.info.util.PaginationUtil;

public class RulesCommand {

    private final TerraCraftInfoPlugin plugin;

    public RulesCommand(TerraCraftInfoPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        new CommandAPICommand("rules")
                .withAliases("правила")
                .withOptionalArguments(new IntegerArgument("page", 1))
                .executes((CommandSender sender, CommandArguments args) -> {
                    Integer page = (Integer) args.get("page");
                    execute(sender, page == null ? 1 : page);
                    return 1;
                })
                .register();
    }

    private void execute(CommandSender sender, int page) {
        var cfg = plugin.getInfoConfig();
        var lang = plugin.getLangManager();
        Player player = sender instanceof Player p ? p : null;

        String title = MiniMessage.miniMessage()
                .stripTags(lang.raw(Lang.RULES_HEADER, player));

        PaginationUtil.sendPaginated(
                sender,
                cfg.rules.list,
                page,
                cfg.pagination.linesPerPage,
                "rules",
                title,
                line -> MessageUtil.parse(line, player)
        );

        String footer = lang.raw(Lang.RULES_FOOTER, player);
        if (footer != null && !footer.isEmpty()) {
            sender.sendMessage(MessageUtil.parse(footer, player));
        }
    }
}