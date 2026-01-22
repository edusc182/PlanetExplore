/**
 * Creature3DVisualizerASCII: Visualizador 3D por terminal usando caracteres ASCII.
 * 
 * Proporciona una representación visual aproximada de criaturas en la terminal.
 * Útil para prototipos y debugging sin dependencias gráficas.
 */
public class Creature3DVisualizerASCII {

    /**
     * Renderiza una criatura en ASCII 3D.
     */
    public static void renderCreature(AICreature creature) {
        if (!(creature instanceof AdvancedAICreature)) {
            System.out.println("⚠ Creature is not AdvancedAICreature, using basic representation");
            return;
        }

        AdvancedAICreature adv = (AdvancedAICreature) creature;

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║  🧬 CREATURE 3D VISUALIZATION (ASCII)                  ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        // Genetic info
        System.out.println("📊 Genetic Code: " + creature.getGeneticCode());
        System.out.println("🏷️  Name: Creature-" + adv.getLineageId() + " (Gen " + adv.getAge() + ")");
        System.out.println("♀️ ♂️ Gender: " + adv.getGender());
        System.out.println("🚴 Locomotion: " + adv.getLocomotion());
        System.out.println();

        // Physical rendering
        System.out.println("📐 Physical Properties:");
        System.out.printf("   Weight: %.1f kg | Height: %.2f m\n", adv.getWeight(), adv.getHeight());
        System.out.println("   Skin: " + adv.getSkinType() + " | Color: " + adv.getColor());
        System.out.println();

        // 3D ASCII Visualization
        System.out.println("🎨 3D Silhouette:");
        renderSilhouette(adv);

        // Traits
        System.out.println("\n📋 Traits:");
        String[] traits = creature.getAttributes();
        for (int i = 0; i < traits.length; i++) {
            System.out.println("   ✓ " + traits[i]);
        }

        System.out.println();
    }

    /**
     * Renderiza la silueta 3D en ASCII basada en el tipo de locomoción.
     */
    private static void renderSilhouette(AdvancedAICreature creature) {
        double weight = creature.getWeight();
        double height = creature.getHeight();
        String locomo = creature.getLocomotion().toString();

        switch (creature.getLocomotion()) {
            case SWIMMING:
                renderSwimmer(weight, height);
                break;
            case BIPEDAL:
                renderBiped(weight, height);
                break;
            case QUADRUPEDAL:
                renderQuadruped(weight, height);
                break;
            case FLYING:
                renderFlyer(weight, height);
                break;
            case CRAWLING:
                renderCrawler(weight, height);
                break;
            default:
                renderGeneric(weight, height);
        }

        System.out.println();
    }

    private static void renderSwimmer(double weight, double height) {
        System.out.println("   (Streamlined aquatic form)");
        System.out.println("            ╱─────────╲");
        System.out.println("       ════════════════════════");
        System.out.println("          ╲~~~~~◉~~~~~╱      ← Head");
        System.out.println("       ════════════════════════");
        System.out.println("            ╲─────────╱");
        System.out.println("      Scale: " + String.format("%.2f", weight / 50.0) + " width × " + 
                          String.format("%.2f", height) + " height");
    }

    private static void renderBiped(double weight, double height) {
        System.out.println("   (Upright humanoid form)");
        System.out.println("              ◯");
        System.out.println("             ╱ ╲");
        System.out.println("            │   │         ← Arms");
        System.out.println("            │ ◉ │");
        System.out.println("            │ │ │         ← Body");
        System.out.println("             ╱ ╲");
        System.out.println("            │   │         ← Legs");
        System.out.println("            │   │");
        System.out.println("      Scale: " + String.format("%.2f", weight / 50.0) + " width × " + 
                          String.format("%.2f", height) + " height (tall)");
    }

    private static void renderQuadruped(double weight, double height) {
        System.out.println("   (Four-legged beast form)");
        System.out.println("       ◯─────────◯");
        System.out.println("      ╱ ◉═════◉   ╲       ← Ears/Head");
        System.out.println("     │  ║       ║  │      ← Body");
        System.out.println("     │  ║ ◉   ◉ ║  │");
        System.out.println("    ╱│  ║       ║  │╲     ← Legs");
        System.out.println("   │ │  ║       ║  │ │");
        System.out.println("      Scale: " + String.format("%.2f", weight / 50.0) + " width × " + 
                          String.format("%.2f", height) + " height (robust)");
    }

    private static void renderFlyer(double weight, double height) {
        System.out.println("   (Aerial flight form)");
        System.out.println("             ◯");
        System.out.println("          ╱ ◉ ╲");
        System.out.println("    ════════◉════════      ← Wings");
        System.out.println("          ╱   ╲");
        System.out.println("         │     │");
        System.out.println("      Scale: " + String.format("%.2f", weight / 50.0) + " width × " + 
                          String.format("%.2f", height) + " height (lightweight)");
    }

    private static void renderCrawler(double weight, double height) {
        System.out.println("   (Ground-hugging crawler form)");
        System.out.println("     ◯─────────◯─────────◯");
        System.out.println("     ║         ║         ║  ← Segments");
        System.out.println("     ◉  ◉   ◉  ◉   ◉   ◉  ◉  ← Legs");
        System.out.println("     ║         ║         ║");
        System.out.println("   ═════════════════════════");
        System.out.println("      Scale: " + String.format("%.2f", weight / 50.0) + " width × " + 
                          String.format("%.2f", height * 0.7) + " height (flattened)");
    }

    private static void renderGeneric(double weight, double height) {
        System.out.println("   (Generic form)");
        System.out.println("            ◯");
        System.out.println("           ╱◉╲");
        System.out.println("          │   │");
        System.out.println("          │ ◉ │");
        System.out.println("          │   │");
        System.out.println("           ╲◉╱");
        System.out.println("            ◯");
        System.out.println("      Scale: " + String.format("%.2f", weight / 50.0) + " width × " + 
                          String.format("%.2f", height) + " height");
    }

    /**
     * Renderiza un panel comparativo de múltiples criaturas.
     */
    public static void renderComparison(java.util.List<AICreature> creatures) {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║  🧬 CREATURE COMPARISON PANEL                          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-20s │ %-15s │ %-15s │\n", "Creature", "Type", "Scale");
        System.out.println("├─────────────────────────────────────────────────────────┤");

        for (AICreature creature : creatures) {
            if (creature instanceof AdvancedAICreature) {
                AdvancedAICreature adv = (AdvancedAICreature) creature;
                String name = "G" + adv.getLineageId() + "-A" + adv.getAge();
                String type = adv.getLocomotion().toString();
                String scale = String.format("%.1f×%.1f×%.1f", 
                    adv.getWeight() / 50.0, 
                    adv.getHeight(),
                    adv.getWeight() / 50.0);
                
                System.out.printf("│ %-20s │ %-15s │ %-15s │\n", 
                    name.substring(0, Math.min(20, name.length())), 
                    type.substring(0, Math.min(15, type.length())),
                    scale.substring(0, Math.min(15, scale.length())));
            }
        }
        System.out.println("└─────────────────────────────────────────────────────────┘\n");
    }
}
