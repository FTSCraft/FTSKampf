package de.ftscraft.ftskampf.utils;

public class PlainResult {
    private int result;
    private int skill;
    private boolean success;

    public PlainResult(int result, int skill, boolean success) {
        this.result = result;
        this.skill = skill;
        this.success = success;
    }

    public int getResult() {
        return result;
    }

    public int getSkill() {
        return skill;
    }

    public boolean isSuccess() {
        return success;
    }
}
