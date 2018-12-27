package me.chrisstar123.util;

import net.minecraft.server.v1_13_R2.Tuple;

public class XPManager {

    public static int getTotalExperience(int level, float progress) {
        return getExperienceFromLevel(level) + getExperienceFromLevelProgress(level, progress);
    }

    public static Tuple<Integer, Float> setTotalExperience(int total) {
        int level = getLevelFromTotalXp(total);
        float progress = getProgressTowardsNextLevel(level, total - getExperienceFromLevel(level));
        return new Tuple<>(level, progress);
    }

    private static int getExperienceFromLevel(int level) {
        if (level > 16 && level <= 31) {
            // Level between 17 and 31
            return (int) (2.5 * (level * level) - 40.5 * level + 360);
        } else if (level > 31) {
            // Level above 31
            return (int) (4.5 * (level * level) - 162.5 * level + 2220);
        } else {
            // Level under 17
            return (level * level) + (6 * level);
        }
    }

    private static int getExperienceFromLevelProgress(int level, float progress) {
        if (level >= 16 && level <= 31) {
            // Level between 17 and 31
            return (5 * level) - 38;
        } else if (level >= 31) {
            // Level above 31
            return (9 * level) - 158;
        } else {
            // Level under 17
            return (2 * level) + 7;
        }
    }

    private static int getLevelFromTotalXp(int totalXp) {
        int level = 0;
        int xpNeededForLevel = 0;
        while (xpNeededForLevel < totalXp) {
            level++;
            xpNeededForLevel = getExperienceFromLevel(level);
        }
        return --level;
    }

    private static float getProgressTowardsNextLevel(int currentLevel, int xpTowardsNextLevel) {
        int xpNeeded = 0;
        if (currentLevel >= 16 && currentLevel <= 31) {
            // Level between 17 and 31
            xpNeeded = (5 * currentLevel) - 38;
        } else if (currentLevel >= 31) {
            // Level above 31
            xpNeeded = (9 * currentLevel) - 158;
        } else {
            // Level under 17
            xpNeeded = (2 * currentLevel) + 7;
        }
        return (float) xpTowardsNextLevel / (float) xpNeeded;
    }
}
