package de.ftscraft.ftskampf.utils.exceptions;

public class SkillLimitException extends Exception{
    private int remainingPoints;
    public SkillLimitException(String errorMessage, int remainingPoints) {
        super(errorMessage);
        this.remainingPoints = remainingPoints;
    }

    public int getRemainingPoints() {
        return remainingPoints;
    }
}
