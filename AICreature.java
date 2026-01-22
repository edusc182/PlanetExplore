import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AICreature {
    private String[] attributes;
    private int age = 0;
    private double mutationChance = 0.25; // base chance to mutate each tick
    private int health = 100;
    private int totalDamageTaken = 0;
    private int mutationCount = 0;
    private int stableGenerations = 0;
    private int adaptiveCharges = 2; // charges consumed when mitigating missing traits
    private static final String[] DEFAULT_TRAITS = {
        "adaptive", "resilient", "quick-learner", "strong-sense", "agile",
        "fragile", "slow-reaction", "forgetful", "weak-sense"
    };
    private static int nextLineageId = 1;
    private final int lineageId;

    public AICreature(String[] attributes) {
        this.attributes = attributes;
        this.lineageId = nextLineageId++;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] newAttributes) {
        this.attributes = newAttributes;
    }

    public int getAge() {
        return age;
    }

    public void incrementAge() {
        this.age++;
    }

    public int getHealth() {
        return health;
    }

    public void reduceHealth(int amount) {
        this.totalDamageTaken += amount;
        this.health = Math.max(0, this.health - amount);
    }

    public int getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public int getMutationCount() {
        return mutationCount;
    }

    public int getLineageId() {
        return lineageId;
    }

    /**
     * Generate a compact genetic code string for quick lineage tracking.
     * Example: G3-A5-M2-D40-HEAT-ADAPT
     */
    public String getGeneticCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("G").append(lineageId);
        sb.append("-A").append(age);
        sb.append("-M").append(mutationCount);
        sb.append("-D").append(totalDamageTaken);

        // include up to first three attribute tokens (shortened)
        for (int i = 0; i < Math.min(3, attributes.length); i++) {
            String a = attributes[i].replaceAll("[^A-Za-z]", "").toUpperCase();
            if (a.length() > 6) a = a.substring(0, 6);
            sb.append("-").append(a);
        }
        return sb.toString();
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public void resetStability() {
        this.stableGenerations = 0;
    }

    public int getAdaptiveCharges() {
        return adaptiveCharges;
    }

    /**
     * Consume up to `requested` adaptive charges. Returns number of charges actually used.
     */
    public int useAdaptive(int requested) {
        int used = Math.max(0, Math.min(requested, adaptiveCharges));
        adaptiveCharges -= used;
        return used;
    }

    /**
     * Fully recharge adaptive charges (reset to 2).
     */
    public void rechargeAdaptive() {
        this.adaptiveCharges = 2;
    }

    public void incrementStableAndMaybeHeal() {
        this.stableGenerations++;
        if (this.stableGenerations >= 3 && this.isAlive()) {
            // heal +17 per cycle when stable
            this.heal(17);
            System.out.println("  - " + this.toString() + " regenerates +17 health (stable=" + this.stableGenerations + ")");
        }
    }

    public void heal(int amount) {
        this.health = Math.min(100, this.health + amount);
    }

    /**
     * Called each update cycle. Increments age and applies a probabilistic
     * mutation to the creature's attribute list. Mutation logic is intentionally
     * lightweight and deterministic in shape so the evolution manager can still
     * orchestrate higher-level behaviors.
     */
    public void tickAndMaybeMutate(Random random, String planetType, String atmosphere) {
        incrementAge();

        // Determine environmental pressure based on planet type / atmosphere
        double environmentalPressure = 0.0;
        if (atmosphere != null) {
            if (atmosphere.contains("Carbon")) {
                if (!Arrays.asList(attributes).contains("Toxic Resistance")) environmentalPressure += 0.4;
            } else if (atmosphere.contains("Methane")) {
                if (!Arrays.asList(attributes).contains("Methane Breather")) environmentalPressure += 0.4;
            } else if (atmosphere.contains("Nitrogen")) {
                if (!Arrays.asList(attributes).contains("Nitrogen Tolerance")) environmentalPressure += 0.2;
            } else if (atmosphere.contains("Oxygen")) {
                if (!Arrays.asList(attributes).contains("High Endurance")) environmentalPressure += 0.1;
            }
        }
        if (planetType != null) {
            if (planetType.equals("Ocean") && !Arrays.asList(attributes).contains("Swimming")) environmentalPressure += 0.3;
            if (planetType.equals("Lava") && !Arrays.asList(attributes).contains("Heat Resistance")) environmentalPressure += 0.3;
            if (planetType.equals("Ice") && !Arrays.asList(attributes).contains("Cold Resistance")) environmentalPressure += 0.3;
            if (planetType.equals("Desert") && !Arrays.asList(attributes).contains("Water Conservation")) environmentalPressure += 0.3;
        }

        // If creature is missing vital traits, boost chance further to encourage guided evolution
        List<String> missingVital = new ArrayList<>();
        if (atmosphere != null) {
            if (atmosphere.contains("Carbon") && !Arrays.asList(attributes).contains("Toxic Resistance")) missingVital.add("Toxic Resistance");
            if (atmosphere.contains("Methane") && !Arrays.asList(attributes).contains("Methane Breather")) missingVital.add("Methane Breather");
            if (atmosphere.contains("Nitrogen") && !Arrays.asList(attributes).contains("Nitrogen Tolerance")) missingVital.add("Nitrogen Tolerance");
            if (atmosphere.contains("Oxygen") && !Arrays.asList(attributes).contains("High Endurance")) missingVital.add("High Endurance");
        }
        if (planetType != null) {
            if (planetType.equals("Ocean") && !Arrays.asList(attributes).contains("Swimming")) missingVital.add("Swimming");
            if (planetType.equals("Lava") && !Arrays.asList(attributes).contains("Heat Resistance")) missingVital.add("Heat Resistance");
            if (planetType.equals("Ice") && !Arrays.asList(attributes).contains("Cold Resistance")) missingVital.add("Cold Resistance");
            if (planetType.equals("Desert") && !Arrays.asList(attributes).contains("Water Conservation")) missingVital.add("Water Conservation");
        }

        double effectiveMutationChance = mutationChance + environmentalPressure + (missingVital.isEmpty() ? 0.0 : 0.25);

        if (random.nextDouble() < effectiveMutationChance) {
            List<String> currentAttributes = new ArrayList<>(Arrays.asList(attributes));

            int mutationType = random.nextInt(3); // 0: add, 1: remove, 2: modify
            System.out.print("  - " + this.toString() + " internal mutation: ");

            switch (mutationType) {
                case 0: // add a trait; prefer missing vital traits
                    if (!missingVital.isEmpty() && random.nextDouble() < 0.8) {
                        String vital = missingVital.get(random.nextInt(missingVital.size()));
                        if (!currentAttributes.contains(vital)) {
                            currentAttributes.add(vital);
                            System.out.println("re-evolved vital trait '" + vital + "'");
                        } else {
                            System.out.println("vital trait already present, no-op");
                        }
                    } else {
                        // pick a trait from DEFAULT_TRAITS that isn't already present
                        List<String> pool = new ArrayList<>();
                        for (String t : DEFAULT_TRAITS) if (!currentAttributes.contains(t)) pool.add(t);
                        if (!pool.isEmpty()) {
                            String pick = pool.get(random.nextInt(pool.size()));
                            currentAttributes.add(pick);
                            System.out.println("added '" + pick + "'");
                        } else {
                            // fallback to generated mutated trait
                            String newTrait = "mutated_trait_" + (100 + random.nextInt(900));
                            currentAttributes.add(newTrait);
                            System.out.println("added '" + newTrait + "' (fallback)");
                        }
                    }
                    break;
                case 1: // remove a random trait
                    if (!currentAttributes.isEmpty()) {
                        String removed = currentAttributes.remove(random.nextInt(currentAttributes.size()));
                        System.out.println("removed '" + removed + "'");
                    } else {
                        System.out.println("no traits to remove");
                    }
                    break;
                case 2: // modify an existing trait
                    if (!currentAttributes.isEmpty()) {
                        // attempt several times to find a meaningful modification that doesn't duplicate
                        boolean modifiedOk = false;
                        for (int attempt = 0; attempt < 5 && !modifiedOk; attempt++) {
                            int idx = random.nextInt(currentAttributes.size());
                            String oldTrait = currentAttributes.get(idx);
                            String modified = oldTrait;

                            if (oldTrait.contains("sense")) {
                                modified = oldTrait.replace("sense", "perception");
                            } else if (oldTrait.startsWith("level:")) {
                                try {
                                    int lvl = Integer.parseInt(oldTrait.split(":" )[1]);
                                    lvl = Math.min(lvl + 1, 5);
                                    modified = "level:" + lvl;
                                } catch (Exception e) {
                                    modified = "level:1";
                                }
                            } else if (oldTrait.contains("speed:low")) {
                                modified = oldTrait.replace("speed:low", "speed:medium");
                            } else {
                                // pick a replacement from DEFAULT_TRAITS not already present
                                for (String cand : DEFAULT_TRAITS) {
                                    if (!currentAttributes.contains(cand) && !cand.equals(oldTrait)) {
                                        modified = cand;
                                        break;
                                    }
                                }
                            }

                            if (!modified.equals(oldTrait) && !currentAttributes.contains(modified)) {
                                currentAttributes.set(idx, modified);
                                System.out.println("modified '" + oldTrait + "' -> '" + modified + "'");
                                modifiedOk = true;
                            }
                        }
                        if (!modifiedOk) System.out.println("modification resulted in duplicate/no-op");
                    } else {
                        System.out.println("no traits to modify");
                    }
                    break;
            }

            this.attributes = currentAttributes.toArray(new String[0]);
            this.mutationCount++;
        }
    }

    @Override
    public String toString() {
        return "AICreature{" +
                "attributes=" + Arrays.toString(attributes) +
                ", age=" + age +
                ", health=" + health +
                ", lineage=" + lineageId +
                ", dna=" + getGeneticCode() +
                '}';
    }
}
