/**
 * CreatureRendererDemo: Demonstrates the visual rendering system.
 * 
 * This demo shows how biological creature data (genetics, physical traits)
 * is translated into visual 3D representations. Each creature's appearance
 * is determined by:
 * - Height and Weight → Scale
 * - Skin type → Texture and base color
 * - Gender → Sexual dimorphism (color vibrance)
 * - Locomotion → Body parts and animations
 */
public class CreatureRendererDemo {

    public static void main(String[] args) {
        System.out.println("=== CREATURE VISUAL RENDERING DEMO ===\n");

        CreatureRenderer renderer = new CreatureRenderer();

        // Demo 1: Swimming creature (female) - Blue scales, smaller
        System.out.println("DEMO 1: Aquatic Swimmer (Female)\n");
        AdvancedAICreature swimmer = new AdvancedAICreature(
            AICreature.Locomotion.SWIMMING,
            "Scales",
            "Blue",
            "Streamlined",
            8.0,   // base weight (kg)
            0.45,  // base height (m)
            new String[]{"Gills", "Swimming", "Salt Tolerance"}
        );
        
        GameObject3D swimmerModel = new GameObject3D("Creature_Swimmer_1");
        renderer.applyVisualTraits(swimmer, swimmerModel);
        System.out.println("Result: " + swimmerModel.toString() + "\n");

        // Demo 2: Terrestrial walker (male) - Fur, larger
        System.out.println("\nDEMO 2: Terrestrial Quadruped (Male)\n");
        AdvancedAICreature quad = new AdvancedAICreature(
            AICreature.Locomotion.QUADRUPEDAL,
            "Fur",
            "Brown",
            "Robust",
            55.0,  // base weight (kg)
            1.3,   // base height (m)
            new String[]{"Teeth", "Claws", "Pack Hunter"}
        );
        
        GameObject3D quadModel = new GameObject3D("Creature_Quad_1");
        renderer.applyVisualTraits(quad, quadModel);
        System.out.println("Result: " + quadModel.toString() + "\n");

        // Demo 3: Flying creature (female) - Feathers, exotic coloration
        System.out.println("\nDEMO 3: Aerial Flyer (Female)\n");
        AdvancedAICreature flyer = new AdvancedAICreature(
            AICreature.Locomotion.FLYING,
            "Feathers",
            "Red",
            "Aerodynamic",
            2.5,   // base weight (kg)
            0.35,  // base height (m)
            new String[]{"Beak", "Sharp Vision", "Lightweight"}
        );
        
        GameObject3D flyerModel = new GameObject3D("Creature_Flyer_1");
        renderer.applyVisualTraits(flyer, flyerModel);
        System.out.println("Result: " + flyerModel.toString() + "\n");

        // Demo 4: Crawler (male) - Generic skin, reduced height
        System.out.println("\nDEMO 4: Ground Crawler (Male)\n");
        AdvancedAICreature crawler = new AdvancedAICreature(
            AICreature.Locomotion.CRAWLING,
            "Chitin",
            "Gray",
            "Elongated",
            12.0,  // base weight (kg)
            0.6,   // base height (m)
            new String[]{"Armor Plates", "Burrowing", "Toxic Spit"}
        );
        
        GameObject3D crawlerModel = new GameObject3D("Creature_Crawler_1");
        renderer.applyVisualTraits(crawler, crawlerModel);
        System.out.println("Result: " + crawlerModel.toString() + "\n");

        // Demo 5: Bipedal walker (female) - Mixed traits
        System.out.println("\nDEMO 5: Bipedal Humanoid (Female)\n");
        AdvancedAICreature biped = new AdvancedAICreature(
            AICreature.Locomotion.BIPEDAL,
            "Fur",
            "Orange",
            "Upright",
            45.0,  // base weight (kg)
            1.6,   // base height (m)
            new String[]{"Intelligence", "Tool Use", "Language"}
        );
        
        GameObject3D bipedModel = new GameObject3D("Creature_Biped_1");
        renderer.applyVisualTraits(biped, bipedModel);
        System.out.println("Result: " + bipedModel.toString() + "\n");

        // Summary
        System.out.println("\n=== RENDERING SUMMARY ===");
        System.out.println("All creatures have been rendered with:");
        System.out.println("  ✓ Height/Weight-based scaling");
        System.out.println("  ✓ Skin type textures");
        System.out.println("  ✓ Sexual dimorphism coloring");
        System.out.println("  ✓ Locomotion-specific animations");
        System.out.println("  ✓ Body part visibility based on traits");
        System.out.println("\nVisual data is now ready for 3D engine integration!");
    }
}
