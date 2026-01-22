import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CreatureEvolutionManager {

    private final Random random;
    private final double mutationChance;

    public CreatureEvolutionManager(Random random) {
        this(random, 0.25);
    }

    public CreatureEvolutionManager(Random random, double mutationChance) {
        this.random = random;
        this.mutationChance = mutationChance;
    }

    private static final String[] POSITIVE_TRAITS = {"adaptive", "resilient", "quick-learner", "strong-sense", "agile"};
    private static final String[] NEGATIVE_TRAITS = {"fragile", "slow-reaction", "forgetful", "weak-sense"};
    private static final String[] SKILL_LEVELS = {"level:1", "level:2", "level:3", "level:4", "level:5"};

    public void updateCreatures(List<AICreature> creatures) {
        System.out.println("\n--- Starting Creature Evolution Cycle ---");
        for (AICreature creature : creatures) {
            creature.incrementAge();
            applyTraining(creature);

            if (random.nextDouble() < mutationChance) {
                applyMutation(creature);
            }
        }
        System.out.println("--- Evolution Cycle Finished ---\n");
    }

    /**
     * Environment-aware update (maintains backward compatibility with PlanetGenerator).
     */
    public void updateCreatures(List<AICreature> creatures, String planetType, String atmosphere) {
        updateCreatures(creatures);  // Delegate to the basic version
    }

    /**
     * NUEVO: Simula un cambio planetario drástico (ej. sequía global).
     * Fuerza a los nadadores a evolucionar o sufrir.
     */
    public void triggerPlanetaryCataclysm(List<AICreature> creatures, String cataclysmType) {
        System.out.println("\n!!! PLANETARY CATACLYSM DETECTED: " + cataclysmType + " !!!");
        
        for (AICreature creature : creatures) {
            // Lógica específica para criaturas acuáticas
            if (creature.getLocomotion() == AICreature.Locomotion.SWIMMING) {
                double survivalRoll = random.nextDouble();
                
                // 40% de probabilidad de evolucionar para sobrevivir a la tierra
                if (survivalRoll > 0.6) {
                    System.out.println("  > EVOLUTION MIRACLE: " + creature + " developed lungs/legs!");
                    
                    // Probabilidad pequeña de evolución avanzada (Pez volador)
                    if (random.nextDouble() > 0.8) {
                        creature.setLocomotion(AICreature.Locomotion.FLYING);
                        System.out.println("    *** It grew wings! Now it flies! ***");
                    } else {
                        // Evolución estándar: Arrastrarse
                        creature.setLocomotion(AICreature.Locomotion.CRAWLING);
                        System.out.println("    *** It evolved to crawl on land. ***");
                    }
                } else {
                    System.out.println("  > TRAGEDY: " + creature + " could not adapt and perished.");
                    // En una simulación real, aquí eliminaríamos la criatura de la lista.
                }
            } 
            // Lógica para los que se arrastran -> Caminar
            else if (creature.getLocomotion() == AICreature.Locomotion.CRAWLING) {
                 if (random.nextDouble() > 0.7) {
                     boolean bipedal = random.nextBoolean();
                     creature.setLocomotion(bipedal ? AICreature.Locomotion.BIPEDAL : AICreature.Locomotion.QUADRUPEDAL);
                     System.out.println("  > EVOLUTION: Crawler evolved to walker (" + creature.getLocomotion() + ")");
                 }
            }
        }
        System.out.println("!!! CATACLYSM ENDED !!!\n");
    }

    private void applyTraining(AICreature creature) {
        // Lógica de entrenamiento (simplificada para usar la nueva clase)
        List<String> currentAttributes = new ArrayList<>(Arrays.asList(creature.getAttributes()));
        
        // Entrenar añade un rasgo positivo a veces
        if (random.nextDouble() < 0.3) {
            String newTrait = POSITIVE_TRAITS[random.nextInt(POSITIVE_TRAITS.length)];
            if (!currentAttributes.contains(newTrait)) {
                currentAttributes.add(newTrait);
            }
        }
        creature.setAttributes(currentAttributes.toArray(new String[0]));
    }

    private void applyMutation(AICreature creature) {
        // La mutación ahora puede afectar los atributos de texto antiguos
        List<String> currentAttributes = new ArrayList<>(Arrays.asList(creature.getAttributes()));
        
        if (random.nextBoolean()) {
            currentAttributes.add(random.nextBoolean() ? "thick-skin" : "sharp-vision");
            System.out.println("  - Mutation: " + creature + " gained a physical trait.");
        }
        
        creature.setAttributes(currentAttributes.toArray(new String[0]));
    }

    // --- MAIN PARA PROBAR TODO ---
    public static void main(String[] args) {
        CreatureEvolutionManager manager = new CreatureEvolutionManager(new Random());
        List<AICreature> population = new ArrayList<>();

        // 1. Crear población inicial diversa
        // Nota: El constructor maneja automáticamente el género, tamaño y color exótico
        
        // Unos peces
        AdvancedAICreature swimmer1 = new AdvancedAICreature(AICreature.Locomotion.SWIMMING, "Scales", "Blue", "Torpedo", 10.0, 0.5, new String[]{"gills"});
        AdvancedAICreature swimmer2 = new AdvancedAICreature(AICreature.Locomotion.SWIMMING, "Scales", "Silver", "Flat", 5.0, 0.3, new String[]{"fins"});
        
        // Unos terrestres
        AdvancedAICreature quad = new AdvancedAICreature(AICreature.Locomotion.QUADRUPEDAL, "Fur", "Brown", "Robust", 50.0, 1.2, new String[]{"teeth"});
        AdvancedAICreature flyer = new AdvancedAICreature(AICreature.Locomotion.FLYING, "Feathers", "Red", "Aerodynamic", 2.0, 0.4, new String[]{"beak"});

        population.add(swimmer1);
        population.add(swimmer2);
        population.add(quad);
        population.add(flyer);

        System.out.println("=== INITIAL POPULATION (Notice Gender/Color/Size differences) ===");
        population.forEach(System.out::println);

        // 2. Correr ciclo normal
        manager.updateCreatures(population);

        // 3. ¡CATACLISMO! (Secado de océanos)
        // Aquí veremos si los peces evolucionan a caminar o volar
        manager.triggerPlanetaryCataclysm(population, "GLOBAL DROUGHT");

        System.out.println("=== FINAL POPULATION ===");
        population.forEach(System.out::println);
    }
}
