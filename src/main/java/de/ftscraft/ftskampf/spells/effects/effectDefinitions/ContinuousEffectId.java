package de.ftscraft.ftskampf.spells.effects.effectDefinitions;

public enum ContinuousEffectId {

    BUFF_AGILITY("001"),
    BUFF_ARMOR( "002"),
    BUFF_ATTACK("003"),
    BUFF_UNARMED("004"),
    DAMAGE_OVER_TIME("005"),
    DEBUFF_TARGET_AGILITY("006"),
    DEBUFF_TARGET_ARMOR("007"),
    DEBUFF_TARGET_ATTACK("008"),
    PROTECT_ATTACK("009");

    private final String id;

    ContinuousEffectId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}