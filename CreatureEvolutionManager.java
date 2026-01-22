import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CreatureEvolutionManager {

    private final Random random;

    public CreatureEvolutionManager(Random random) {
        this.random = random;
    }

    // Example pools of possible attributes for mutation/training
    private static final String[] POSITIVE_TRAITS = {"adaptive", "resilient", "quick-learner", "strong-sense", "agile"};
    private static final String[] NEGATIVE_TRAITS = {"fragile", "slow-reaction", "forgetful", "weak-sense"};
    private static final String[] SKILL_LEVELS = {"level:1", "level:2", "level:3", "level:4", "level:5"};

    /**
     * Updates a list of AICreature objects by applying training and mutation logic.
     *
     * @param creatures The list of AICreature objects to update.
     */
    public void updateCreatures(List<AICreature> creatures) {
        // Backwards-compatible method: no environment provided
        updateCreatures(creatures, null, null);
    }

    /**
     * Environment-aware update routine. Pass the current planet type and atmosphere
     * so creatures can adapt or suffer environmental pressure.
     */
    public void updateCreatures(List<AICreature> creatures, String planetType, String atmosphere) {
        System.out.println("\n--- Starting Creature Evolution Cycle ---");
        for (AICreature creature : creatures) {
            System.out.println("Processing: " + creature.toString());

            // Apply training based on some simulated condition
            applyTraining(creature);

            // Let the creature handle its own probabilistic mutation (age+environment)
            creature.tickAndMaybeMutate(random, planetType, atmosphere);
            System.out.println("After update: " + creature.toString());
        }
        System.out.println("--- Evolution Cycle Finished ---\n");
    }

    /**
     * Applies training logic to a single AICreature.
     * Training here means modifying existing attributes or adding new learned ones.
     *
     * @param creature The AICreature to train.
     */
    private void applyTraining(AICreature creature) {
        // Convert array to List for easier modification
        List<String> currentAttributes = new ArrayList<>(Arrays.asList(creature.getAttributes()));

        // Example Training Logic:
        // 1. Improve a random existing skill level
        for (int i = 0; i < currentAttributes.size(); i++) {
            if (currentAttributes.get(i).startsWith("level:")) {
                String currentLevelStr = currentAttributes.get(i);
                try {
                    int currentLevel = Integer.parseInt(currentLevelStr.split(":")[1]);
                    if (currentLevel < SKILL_LEVELS.length) {
                        currentAttributes.set(i, SKILL_LEVELS[currentLevel]); // Upgrade to next level
                        System.out.println("  - " + creature.toString() + " trained: Skill upgraded to " + SKILL_LEVELS[currentLevel]);
                        break; // Only upgrade one skill per training session
                    }
                } catch (NumberFormatException e) {
                    // Ignore if it's not a valid level string
                }
            }
        }

        // 2. Potentially add a positive trait if not already present
        String positiveTrait = POSITIVE_TRAITS[random.nextInt(POSITIVE_TRAITS.length)];
        if (!currentAttributes.contains(positiveTrait) && random.nextDouble() < 0.3) { // 30% chance to gain a positive trait
            currentAttributes.add(positiveTrait);
            System.out.println("  - " + creature.toString() + " trained: Gained trait '" + positiveTrait + "'");
        }

        // Convert back to array and use the public setter
        creature.setAttributes(currentAttributes.toArray(new String[0]));
    }

    /**
     * Applies mutation logic to a single AICreature.
     * Mutations can be random additions, removals, or changes to attributes.
     *
     * @param creature The AICreature to mutate.
     */
    private void applyMutation(AICreature creature) {
        List<String> currentAttributes = new ArrayList<>(Arrays.asList(creature.getAttributes()));
        int mutationType = random.nextInt(3); // 0: add, 1: remove, 2: modify

        System.out.print("  - " + creature.toString() + " is mutating: ");

        switch (mutationType) {
            case 0: // Add a new random trait (could be positive or negative)
                String newTrait = random.nextBoolean() ?
                                  POSITIVE_TRAITS[random.nextInt(POSITIVE_TRAITS.length)] :
                                  NEGATIVE_TRAITS[random.nextInt(NEGATIVE_TRAITS.length)];
                if (!currentAttributes.contains(newTrait)) {
                    currentAttributes.add(newTrait);
                    System.out.println("Added new trait '" + newTrait + "'");
                } else {
                    System.out.println("Tried to add existing trait, no change.");
                }
                break;
            case 1: // Remove an existing random trait
                if (!currentAttributes.isEmpty()) {
                    String removedTrait = currentAttributes.remove(random.nextInt(currentAttributes.size()));
                    System.out.println("Removed trait '" + removedTrait + "'");
                } else {
                    System.out.println("No traits to remove.");
                }
                break;
            case 2: // Modify an existing trait (e.g., change a specific keyword)
                if (!currentAttributes.isEmpty()) {
                    int indexToModify = random.nextInt(currentAttributes.size());
                    String oldTrait = currentAttributes.get(indexToModify);
                    String modifiedTrait = oldTrait;

                    if (oldTrait.contains("sense")) { // Example: modify a sense trait
                        modifiedTrait = oldTrait.replace("sense", "perception");
                    } else if (oldTrait.contains("speed")) { // Example: modify speed
                        modifiedTrait = oldTrait.replace("speed:low", "speed:fast").replace("speed:medium", "speed:fast");
                    } else { // Generic random modification
                         // Just randomly pick a positive trait to replace a random one
                        modifiedTrait = POSITIVE_TRAITS[random.nextInt(POSITIVE_TRAITS.length)];
                    }

                    if (!oldTrait.equals(modifiedTrait) && !currentAttributes.contains(modifiedTrait)) {
                        currentAttributes.set(indexToModify, modifiedTrait);
                        System.out.println("Modified '" + oldTrait + "' to '" + modifiedTrait + "'");
                    } else {
                        System.out.println("Trait modification attempt failed or resulted in duplicate.");
                    }
                } else {
                    System.out.println("No traits to modify.");
                }
                break;
        }

        // Convert back to array and use the public setter
        creature.setAttributes(currentAttributes.toArray(new String[0]));
    }

    public static void main(String[] args) {
        CreatureEvolutionManager manager = new CreatureEvolutionManager(new Random());
        List<AICreature> creaturePopulation = new ArrayList<>();

        // Create some initial creatures with diverse attributes
        creaturePopulation.add(new AICreature(new String[]{"speed:low", "tough", "level:1", "camouflage:forest"}));
        creaturePopulation.add(new AICreature(new String[]{"strong", "agile", "level:3", "sharp-claws"}));
        creaturePopulation.add(new AICreature(new String[]{"speed:medium", "level:2", "poisonous"}));
        creaturePopulation.add(new AICreature(new String[]{"level:4", "adaptive"}));

        System.out.println("--- Initial Population ---");
        creaturePopulation.forEach(System.out::println);

        // Run the evolution cycle for a few iterations
        int generations = 3;
        for (int i = 0; i < generations; i++) {
            System.out.println("\n===== GENERATION " + (i + 1) + " =====");
            manager.updateCreatures(creaturePopulation);
            System.out.println("\n--- Population After Generation " + (i + 1) + " ---");
            creaturePopulation.forEach(System.out::println);
        }
    }
}
