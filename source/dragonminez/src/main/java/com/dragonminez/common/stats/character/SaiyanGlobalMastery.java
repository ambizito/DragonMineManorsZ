package com.dragonminez.common.stats.character;

import com.dragonminez.common.config.ConfigManager;
import com.dragonminez.common.config.FormConfig;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Derives a Saiyan-wide mastery score from the existing per-form masteries.
 * Nothing new is persisted, so existing characters and saves remain compatible.
 */
public final class SaiyanGlobalMastery {
    public static final double MAX_GLOBAL_MASTERY = 100.0;
    public static final double MAX_GAIN_MULTIPLIER = 10.0;

    private static final String SAIYAN = "saiyan";
    private static final String SUPER_SAIYAN_4 = "supersaiyan4";

    private static final List<WeightedForm> WEIGHTED_FORMS = List.of(
            // Basic / Z: 15%
            new WeightedForm("ssgrades", "supersaiyan", 2.0),
            new WeightedForm("ssgrades", "supersaiyangrade2", 1.0),
            new WeightedForm("ssgrades", "supersaiyangrade3", 1.0),
            new WeightedForm("supersaiyan", "supersaiyanmastered", 2.0),
            new WeightedForm("supersaiyan", "supersaiyan2", 4.0),
            new WeightedForm("supersaiyan", "supersaiyan3", 5.0),

            // GT: 20%. SSJ4 exists in two groups and contributes only once.
            new WeightedForm("oozaru", "oozaru", 2.0),
            new WeightedForm("oozaru", "goldenoozaru", 3.0),
            new WeightedForm("supersaiyan", SUPER_SAIYAN_4, 4.0),
            new WeightedForm("oozaru", "supersaiyan4fp", 5.0),
            new WeightedForm("oozaru", "supersaiyan4lb", 6.0),

            // Legendary: 20%
            new WeightedForm("legendaryforms", "ikari", 2.0),
            new WeightedForm("legendaryforms", "ssjhybrid", 3.0),
            new WeightedForm("legendaryforms", "ssjfullpower", 4.0),
            new WeightedForm("legendaryforms", "ssjlegendary3", 5.0),
            new WeightedForm("legendaryforms", "ssjlegendary4", 6.0),

            // Divine (Blue and Rose branches): 37%
            new WeightedForm("godforms", "supersaiyangod", 3.0),
            new WeightedForm("godforms", "supersaiyanblue", 4.0),
            new WeightedForm("godforms", "supersaiyanblueevolved", 5.0),
            new WeightedForm("godforms", "supersaiyanblueperfected", 6.0),
            new WeightedForm("godforms", "supersaiyanrose", 4.0),
            new WeightedForm("godforms", "supersaiyanroseevolved", 4.0),
            new WeightedForm("godforms", "supersaiyanrose3", 5.0),
            new WeightedForm("godforms", "supersaiyanrosefullpower", 6.0),

            // Beast: 8%
            new WeightedForm("beastforms", "beast", 8.0)
    );

    private static final Set<String> ELIGIBLE_FORMS = WEIGHTED_FORMS.stream()
            .map(form -> key(form.group(), form.form()))
            .collect(Collectors.toUnmodifiableSet());

    private SaiyanGlobalMastery() {}

    public static double getGlobalMastery(Character character) {
        if (!isSaiyan(character)) return 0.0;

        double total = 0.0;
        for (WeightedForm weightedForm : WEIGHTED_FORMS) {
            total += getNormalizedMastery(character, weightedForm) * weightedForm.weight();
        }
        return Math.max(0.0, Math.min(MAX_GLOBAL_MASTERY, total));
    }

    public static double getGainMultiplier(Character character) {
        double progress = getGlobalMastery(character) / MAX_GLOBAL_MASTERY;
        return 1.0 + (MAX_GAIN_MULTIPLIER - 1.0) * Math.pow(progress, 1.5);
    }

    public static double getGainMultiplier(Character character, String group, String form) {
        return isEligible(character, group, form) ? getGainMultiplier(character) : 1.0;
    }

    public static boolean isEligible(Character character, String group, String form) {
        if (!isSaiyan(character) || group == null || form == null) return false;
        String normalizedGroup = group.toLowerCase(Locale.ROOT);
        String normalizedForm = form.toLowerCase(Locale.ROOT);
        if (normalizedForm.equals(SUPER_SAIYAN_4) && normalizedGroup.equals("oozaru")) return true;
        return ELIGIBLE_FORMS.contains(key(normalizedGroup, normalizedForm));
    }

    private static boolean isSaiyan(Character character) {
        return character != null && SAIYAN.equalsIgnoreCase(character.getRaceName());
    }

    private static double getNormalizedMastery(Character character, WeightedForm weightedForm) {
        double normalized = getNormalizedMastery(character, weightedForm.group(), weightedForm.form());
        if (weightedForm.form().equals(SUPER_SAIYAN_4)) {
            normalized = Math.max(normalized, getNormalizedMastery(character, "oozaru", SUPER_SAIYAN_4));
        }
        return normalized;
    }

    private static double getNormalizedMastery(Character character, String group, String form) {
        FormConfig.FormData formData = ConfigManager.getForm(SAIYAN, group, form);
        if (formData == null || formData.getMaxMastery() <= 0.0) return 0.0;
        double mastery = character.getFormMasteries().getMastery(group, form);
        return Math.max(0.0, Math.min(1.0, mastery / formData.getMaxMastery()));
    }

    private static String key(String group, String form) {
        return group.toLowerCase(Locale.ROOT) + ":" + form.toLowerCase(Locale.ROOT);
    }

    private record WeightedForm(String group, String form, double weight) {}
}
