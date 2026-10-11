package ru.net.terracraft.info.lang;

/**
 * Все ключи локализации. Значения — пути в lang/*.yml.
 */
public enum Lang {

    PREFIX("prefix"),

    // ─── Rules ────────────────────────────────
    RULES_HEADER("rules.header"),
    RULES_FOOTER("rules.footer"),

    // ─── Info ─────────────────────────────────
    INFO_HEADER("info.header"),

    // ─── Help ─────────────────────────────────
    HELP_HEADER("help.header"),
    HELP_GROUP_HEADER("help.group_header"),
    HELP_ENTRY_FORMAT("help.entry_format"),
    HELP_ALIASES_FORMAT("help.aliases_format"),
    HELP_NO_GROUPS("help.no_groups"),
    HELP_HOVER_CLICK("help.hover_click"),

    // ─── Links ────────────────────────────────
    LINKS_HEADER("links.header"),
    LINKS_ENTRY_FORMAT("links.entry_format"),
    LINKS_HOVER_OPEN("links.hover_open"),

    // ─── Pagination ───────────────────────────
    PAGE_HEADER("pagination.header"),
    PAGE_FOOTER("pagination.footer"),
    PAGE_NEXT_LABEL("pagination.next_label"),
    PAGE_PREV_LABEL("pagination.prev_label"),
    PAGE_INDICATOR("pagination.page_indicator"),
    PAGE_HOVER_NEXT("pagination.hover_next"),
    PAGE_HOVER_PREV("pagination.hover_prev"),
    PAGE_INVALID("pagination.invalid"),

    // ─── Errors ───────────────────────────────
    ERROR_PLAYER_ONLY("error.player_only"),
    ERROR_NO_PERMISSION("error.no_permission"),
    ERROR_UNKNOWN_COMMAND("error.unknown_command"),

    // ─── Update ───────────────────────────────
    UPDATE_AVAILABLE("update.available"),
    UPDATE_LATEST("update.latest"),
    UPDATE_ERROR("update.error");

    private final String path;

    Lang(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}