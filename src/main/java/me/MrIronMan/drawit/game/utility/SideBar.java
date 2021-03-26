package me.MrIronMan.drawit.game.utility;

import me.MrIronMan.drawit.utility.ReflectionUtils;
import me.MrIronMan.drawit.utility.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SideBar {

    private static final VersionType VERSION_TYPE;

    private static final Field PLAYER_CONNECTION;
    private static final Method SEND_PACKET;
    private static final Method PLAYER_GET_HANDLE;

    private static final Class<?> CHAT_COMPONENT_CLASS;
    private static final Method MESSAGE_FROM_STRING;

    private static final Constructor<?> PACKET_SB_OBJ;
    private static final Constructor<?> PACKET_SB_DISPLAY_OBJ;
    private static final Constructor<?> PACKET_SB_SCORE;
    private static final Constructor<?> PACKET_SB_TEAM;

    private static final Class<?> ENUM_SB_HEALTH_DISPLAY;
    private static final Class<?> ENUM_SB_ACTION;
    private static final Object ENUM_SB_HEALTH_DISPLAY_INTEGER;
    private static final Object ENUM_SB_ACTION_CHANGE;
    private static final Object ENUM_SB_ACTION_REMOVE;

    static {
        try {
            if (ReflectionUtils.nmsOptionalClass("ScoreboardServer$Action").isPresent()) {
                VERSION_TYPE = VersionType.V1_13;
            } else if (ReflectionUtils.nmsOptionalClass("IScoreboardCriteria$EnumScoreboardHealthDisplay").isPresent()) {
                VERSION_TYPE = VersionType.V1_8;
            } else {
                VERSION_TYPE = VersionType.V1_7;
            }

            Class<?> craftChatMessageClass = ReflectionUtils.obcClass("util.CraftChatMessage");
            Class<?> entityPlayerClass = ReflectionUtils.nmsClass("EntityPlayer");
            Class<?> playerConnectionClass = ReflectionUtils.nmsClass("PlayerConnection");
            Class<?> craftPlayerClass = ReflectionUtils.obcClass("entity.CraftPlayer");

            MESSAGE_FROM_STRING = craftChatMessageClass.getDeclaredMethod("fromString", String.class);
            CHAT_COMPONENT_CLASS = ReflectionUtils.nmsClass("IChatBaseComponent");

            PLAYER_GET_HANDLE = craftPlayerClass.getDeclaredMethod("getHandle");
            PLAYER_CONNECTION = entityPlayerClass.getDeclaredField("playerConnection");
            SEND_PACKET = playerConnectionClass.getDeclaredMethod("sendPacket", ReflectionUtils.nmsClass("Packet"));

            PACKET_SB_OBJ = ReflectionUtils.nmsClass("PacketPlayOutScoreboardObjective").getConstructor();
            PACKET_SB_DISPLAY_OBJ = ReflectionUtils.nmsClass("PacketPlayOutScoreboardDisplayObjective").getConstructor();
            PACKET_SB_SCORE = ReflectionUtils.nmsClass("PacketPlayOutScoreboardScore").getConstructor();
            PACKET_SB_TEAM = ReflectionUtils.nmsClass("PacketPlayOutScoreboardTeam").getConstructor();

            if (VersionType.V1_8.isHigherOrEqual()) {
                ENUM_SB_HEALTH_DISPLAY = ReflectionUtils.nmsClass("IScoreboardCriteria$EnumScoreboardHealthDisplay");

                if (VersionType.V1_13.isHigherOrEqual()) {
                    ENUM_SB_ACTION = ReflectionUtils.nmsClass("ScoreboardServer$Action");
                } else {
                    ENUM_SB_ACTION = ReflectionUtils.nmsClass("PacketPlayOutScoreboardScore$EnumScoreboardAction");
                }

                ENUM_SB_HEALTH_DISPLAY_INTEGER = ReflectionUtils.enumValueOf(ENUM_SB_HEALTH_DISPLAY, "INTEGER");
                ENUM_SB_ACTION_CHANGE = ReflectionUtils.enumValueOf(ENUM_SB_ACTION, "CHANGE");
                ENUM_SB_ACTION_REMOVE = ReflectionUtils.enumValueOf(ENUM_SB_ACTION, "REMOVE");
            } else {
                ENUM_SB_HEALTH_DISPLAY = null;
                ENUM_SB_ACTION = null;

                ENUM_SB_HEALTH_DISPLAY_INTEGER = null;
                ENUM_SB_ACTION_CHANGE = null;
                ENUM_SB_ACTION_REMOVE = null;
            }
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Player player;
    private String id;

    private String title = ChatColor.RESET.toString();
    private List<String> lines = new ArrayList<>();

    private boolean deleted = false;

    public SideBar(Player player) {
        this.player = Objects.requireNonNull(player, "player");

        id = "fb-" + Double.toString(Math.random()).substring(2, 10);

        try {
            sendObjectivePacket(ObjectiveMode.CREATE);
            sendDisplayObjectivePacket();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTitle() {
        return title;
    }

    public void updateTitle(String title) {
        title = TextUtil.colorize(title);
        if (this.title.equals(Objects.requireNonNull(title, "title"))) {
            return;
        }

        if (!VersionType.V1_13.isHigherOrEqual() && title.length() > 32) {
            throw new IllegalArgumentException("Title is longer than 32 chars");
        }

        this.title = title;

        try {
            sendObjectivePacket(ObjectiveMode.UPDATE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getLines() {
        return new ArrayList<>(lines);
    }

    public String getLine(int line) {
        checkLineNumber(line, true);

        return lines.get(line);
    }

    public void updateLine(int line, String text) {
        text = TextUtil.colorize(text);
        checkLineNumber(line, false);

        try {
            if (line < size()) {
                lines.set(line, text);

                sendTeamPacket(getScoreByLine(line), TeamMode.UPDATE);
                return;
            }

            List<String> newLines = new ArrayList<>(lines);

            if (line > size()) {
                for (int i = size(); i < line; i++) {
                    newLines.add("");
                }
            }

            newLines.add(text);

            updateLines(newLines);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeLine(int line) {
        checkLineNumber(line, false);

        if (line >= size()) {
            return; // The line don't exists
        }

        List<String> lines = new ArrayList<>(this.lines);
        lines.remove(line);
        updateLines(lines);
    }

    public void updateLines(String... lines) {
        lines = TextUtil.colorize(lines);
        updateLines(Arrays.asList(lines));
    }

    public void updateLines(List<String> lines) {
        lines = TextUtil.colorize(lines);
        Objects.requireNonNull(lines, "lines");

        if (!VersionType.V1_13.isHigherOrEqual()) {
            int lineCount = 0;
            for (String s : lines) {
                if (s != null && s.length() > 30) {
                    throw new IllegalArgumentException("Line " + lineCount + " is longer than 30 chars");
                }
                lineCount++;
            }
        }

        List<String> oldLines = new ArrayList<>(this.lines);
        this.lines.clear();
        this.lines.addAll(lines);

        int linesSize = this.lines.size();

        try {
            if (oldLines.size() != linesSize) {
                List<String> oldLinesCopy = new ArrayList<>(oldLines);

                if (oldLines.size() > linesSize) {
                    for (int i = oldLinesCopy.size(); i > linesSize; i--) {
                        sendTeamPacket(i - 1, TeamMode.REMOVE);

                        sendScorePacket(i - 1, ScoreboardAction.REMOVE);

                        oldLines.remove(0);
                    }
                } else {
                    for (int i = oldLinesCopy.size(); i < linesSize; i++) {
                        sendScorePacket(i, ScoreboardAction.CHANGE);

                        sendTeamPacket(i, TeamMode.CREATE);

                        oldLines.add(oldLines.size() - i, getLineByScore(i));
                    }
                }
            }

            for (int i = 0; i < linesSize; i++) {
                if (!Objects.equals(getLineByScore(oldLines, i), getLineByScore(i))) {
                    sendTeamPacket(i, TeamMode.UPDATE);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public String getId() {
        return id;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public int size() {
        return lines.size();
    }

    public void delete() {
        try {
            for (int i = 0; i < lines.size(); i++) {
                sendTeamPacket(i, TeamMode.REMOVE);
            }

            sendObjectivePacket(ObjectiveMode.REMOVE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        deleted = true;
    }

    private void checkLineNumber(int line, boolean checkMax) {
        if (line < 0) {
            throw new IllegalArgumentException("Line number must be positive");
        }

        if (checkMax && line >= lines.size()) {
            throw new IllegalArgumentException("Line number must be under " + lines.size());
        }
    }

    private int getScoreByLine(int line) {
        return lines.size() - line - 1;
    }

    private String getLineByScore(int score) {
        return getLineByScore(lines, score);
    }

    private String getLineByScore(List<String> lines, int score) {
        return lines.get(lines.size() - score - 1);
    }

    private void sendObjectivePacket(ObjectiveMode mode) throws ReflectiveOperationException {
        Object packet = PACKET_SB_OBJ.newInstance();

        setField(packet, String.class, id);
        setField(packet, int.class, mode.ordinal());

        if (mode != ObjectiveMode.REMOVE) {
            setComponentField(packet, title, 1);

            if (VersionType.V1_8.isHigherOrEqual()) {
                setField(packet, ENUM_SB_HEALTH_DISPLAY, ENUM_SB_HEALTH_DISPLAY_INTEGER);
            }
        } else if (VERSION_TYPE == VersionType.V1_7) {
            setField(packet, String.class, "", 1);
        }

        sendPacket(packet);
    }

    private void sendDisplayObjectivePacket() throws ReflectiveOperationException {
        Object packet = PACKET_SB_DISPLAY_OBJ.newInstance();

        setField(packet, int.class, 1);
        setField(packet, String.class, id);

        sendPacket(packet);
    }

    private void sendScorePacket(int score, ScoreboardAction action) throws ReflectiveOperationException {
        Object packet = PACKET_SB_SCORE.newInstance();

        setField(packet, String.class, getColorCode(score), 0);

        if (VersionType.V1_8.isHigherOrEqual()) {
            setField(packet, ENUM_SB_ACTION, action == ScoreboardAction.REMOVE ? ENUM_SB_ACTION_REMOVE : ENUM_SB_ACTION_CHANGE);
        } else {
            setField(packet, int.class, action.ordinal(), 1);
        }

        if (action == ScoreboardAction.CHANGE) {
            setField(packet, String.class, id, 1);
            setField(packet, int.class, score);
        }

        sendPacket(packet);
    }

    private void sendTeamPacket(int score, TeamMode mode) throws ReflectiveOperationException {
        if (mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
            throw new UnsupportedOperationException();
        }

        Object packet = PACKET_SB_TEAM.newInstance();

        setField(packet, String.class, id + ':' + score); // Team name
        setField(packet, int.class, mode.ordinal(), VERSION_TYPE == VersionType.V1_8 ? 1 : 0); // Update mode

        if (mode == TeamMode.CREATE || mode == TeamMode.UPDATE) {
            String line = getLineByScore(score);
            String prefix;
            String suffix = null;

            if (line == null || line.isEmpty()) {
                prefix = getColorCode(score) + ChatColor.RESET;
            } else if (line.length() <= 16 || VersionType.V1_13.isHigherOrEqual()) {
                prefix = line;
            } else {
                // Prevent splitting color codes
                int index = line.charAt(15) == ChatColor.COLOR_CHAR ? 15 : 16;
                prefix = line.substring(0, index);
                String suffixTmp = line.substring(index);
                ChatColor chatColor = null;

                if (suffixTmp.length() >= 2 && suffixTmp.charAt(0) == ChatColor.COLOR_CHAR) {
                    chatColor = ChatColor.getByChar(suffixTmp.charAt(1));
                }

                String color = ChatColor.getLastColors(prefix);
                boolean addColor = chatColor == null || chatColor.isFormat();

                suffix = (addColor ? (color.isEmpty() ? ChatColor.RESET.toString() : color) : "") + suffixTmp;
            }

            if (VERSION_TYPE != VersionType.V1_13) {
                if (prefix.length() > 16 || (suffix != null && suffix.length() > 16)) {
                    // Something went wrong, just cut to prevent client crash/kick
                    prefix = prefix.substring(0, 16);
                    suffix = (suffix != null) ? suffix.substring(0, 16) : null;
                }
            }

            setComponentField(packet, prefix, 2); // Prefix
            setComponentField(packet, suffix == null ? "" : suffix, 3); // Suffix
            setField(packet, String.class, "always", 4); // Visibility for 1.8+
            setField(packet, String.class, "always", 5); // Collisions for 1.9+

            if (mode == TeamMode.CREATE) {
                setField(packet, Collection.class, Collections.singletonList(getColorCode(score))); // Players in the team
            }
        }

        sendPacket(packet);
    }

    private String getColorCode(int score) {
        return ChatColor.values()[score].toString();
    }

    private void sendPacket(Object packet) throws ReflectiveOperationException {
        if (deleted) {
            throw new IllegalStateException("This FastBoard is deleted");
        }

        if (player.isOnline()) {
            Object entityPlayer = PLAYER_GET_HANDLE.invoke(player);
            Object playerConnection = PLAYER_CONNECTION.get(entityPlayer);
            SEND_PACKET.invoke(playerConnection, packet);
        }
    }

    private void setField(Object object, Class<?> fieldType, Object value) throws ReflectiveOperationException {
        setField(object, fieldType, value, 0);
    }

    private void setField(Object object, Class<?> fieldType, Object value, int count) throws ReflectiveOperationException {
        int i = 0;

        for (Field f : object.getClass().getDeclaredFields()) {
            if (f.getType() == fieldType && i++ == count) {
                f.setAccessible(true);
                f.set(object, value);
            }
        }
    }

    private void setComponentField(Object object, String value, int count) throws ReflectiveOperationException {
        if (VERSION_TYPE != VersionType.V1_13) {
            setField(object, String.class, value, count);
            return;
        }

        int i = 0;
        for (Field f : object.getClass().getDeclaredFields()) {
            if ((f.getType() == String.class || f.getType() == CHAT_COMPONENT_CLASS) && i++ == count) {
                f.setAccessible(true);
                f.set(object, Array.get(MESSAGE_FROM_STRING.invoke(null, value), 0));
            }
        }
    }

    enum ObjectiveMode {

        CREATE, REMOVE, UPDATE

    }

    enum TeamMode {

        CREATE, REMOVE, UPDATE, ADD_PLAYERS, REMOVE_PLAYERS

    }

    enum ScoreboardAction {

        CHANGE, REMOVE

    }

    enum VersionType {

        V1_7, V1_8, V1_13;

        public boolean isHigherOrEqual() {
            return VERSION_TYPE.ordinal() >= ordinal();
        }
    }
}
