import java.util.ArrayList;
import java.util.List;

/**
 * Demo of AdvancedAICreature with physical attributes and sexual dimorphism.
 */
public class AdvancedAICreatureDemo {
    public static void main(String[] args) {
        System.out.println("=== AdvancedAICreature Physical Attributes Demo ===\n");

        // Create sample creatures with different locomotion types
        List<AdvancedAICreature> population = new ArrayList<>();

        // Aquatic swimmer
        population.add(new AdvancedAICreature(
            AdvancedAICreature.Locomotion.SWIMMING,
            "scales",
            "Blue",
            "Fish-like",
            25.0, 1.2,
            new String[]{"Swimming", "Gills", "Salt Tolerance"}
        ));

        // Desert runner
        population.add(new AdvancedAICreature(
            AdvancedAICreature.Locomotion.BIPEDAL,
            "skin",
            "Brown",
            "Humanoid",
            70.0, 1.75,
            new String[]{"Running", "Heat Tolerance", "Water Conservation"}
        ));

        // Mountain flyer
        population.add(new AdvancedAICreature(
            AdvancedAICreature.Locomotion.FLYING,
            "feathers",
            "White",
            "Avian",
            15.0, 0.8,
            new String[]{"Flight", "High Altitude", "Vision"}
        ));

        // Forest crawler
        population.add(new AdvancedAICreature(
            AdvancedAICreature.Locomotion.QUADRUPEDAL,
            "fur",
            "Green",
            "Insectoid",
            40.0, 1.5,
            new String[]{"Climbing", "Camouflage", "Nocturnal"}
        ));

        System.out.println("Population Overview:");
        for (int i = 0; i < population.size(); i++) {
            AdvancedAICreature c = population.get(i);
            System.out.println(String.format("%d. %s", i + 1, c.toString()));
            System.out.println(String.format("   Physical: %s", c.getPhysicalCode()));
        }

        System.out.println("\n=== Fitness Analysis by Planet Type ===\n");
        
        String[] planetTypes = {"Ocean", "Lava", "Desert", "Forest", "Mountain"};
        
        for (String planet : planetTypes) {
            System.out.println("Planet: " + planet);
            for (AdvancedAICreature c : population) {
                int fitness = c.computePhysicalFitness(planet);
                System.out.println(String.format("  - %s (%s): Fitness +%d",
                    c.getShape(), c.getLocomotion(), fitness));
            }
            System.out.println();
        }

        System.out.println("=== Sexual Dimorphism Examples ===");
        System.out.println("(Run multiple times to see different random genders)\n");
        
        for (int i = 0; i < 3; i++) {
            AdvancedAICreature swimmer = new AdvancedAICreature(
                AdvancedAICreature.Locomotion.SWIMMING,
                "scales", "Blue", "Fish-like", 25.0, 1.2,
                new String[]{"Swimming", "Gills"}
            );
            System.out.println(String.format("Generation %d: Gender=%s, Weight=%.1fkg, Height=%.2fm, Color=%s",
                i + 1, swimmer.getGender(), swimmer.getWeight(), swimmer.getHeight(), swimmer.getColor()));
        }
    }
}
