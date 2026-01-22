import java.awt.Color;

/**
 * CreatureRenderer: Translates biological (mathematical) creature data into visual 3D representations.
 * 
 * This system bridges the gap between AICreature genetics and 3D model visualization,
 * applying scale, color, texture, and animations based on the creature's attributes.
 * 
 * Compatible with: Unity, JavaFX 3D, LibGDX, JMonkeyEngine, or any 3D engine
 * that follows the GameObject3D interface pattern.
 */
public class CreatureRenderer {

    /**
     * Applies all biological characteristics (DNA) to a 3D model for visual rendering.
     * 
     * @param creature The AICreature with biological data
     * @param model3D The 3D game object to be visually modified
     */
    public void applyVisualTraits(AICreature creature, GameObject3D model3D) {
        System.out.println("--- Rendering Creature: " + creature.getGeneticCode() + " ---");

        // Ensure we're working with an AdvancedAICreature for full physical data
        if (!(creature instanceof AdvancedAICreature)) {
            System.out.println("   WARNING: Creature is not AdvancedAICreature. Limited visual data available.");
            return;
        }

        AdvancedAICreature advCreature = (AdvancedAICreature) creature;

        // 1. APPLY SCALE (Height and Weight-based body girth)
        applyScale(advCreature, model3D);

        // 2. APPLY COLOR & TEXTURE (Skin type and sexual dimorphism)
        applyColorAndTexture(advCreature, model3D);

        // 3. ACTIVATE BODY PARTS (Locomotion-based skeleton)
        activateBodyParts(advCreature, model3D);

        // 4. SET POSE/ANIMATION (Movement type)
        setPose(advCreature, model3D);

        System.out.println("   > Visual rendering complete.\n");
    }

    /**
     * Apply height and weight-based scaling to the 3D model.
     * Height maps directly to Y-axis scale; weight affects X/Z girth.
     */
    private void applyScale(AdvancedAICreature creature, GameObject3D model3D) {
        // Height in meters maps to Y-scale (1.0m = 1.0 scale unit)
        float scaleY = (float) creature.getHeight();

        // Weight affects body girth (X and Z axes)
        // Base assumption: 50kg = 1.0 scale unit
        float scaleXZ = (float) (creature.getWeight() / 50.0);

        // Clamp scales to reasonable values
        scaleY = Math.max(0.3f, Math.min(3.0f, scaleY));
        scaleXZ = Math.max(0.2f, Math.min(3.0f, scaleXZ));

        model3D.setScale(scaleXZ, scaleY, scaleXZ);
        System.out.println("   > Scale applied: " + String.format("%.2f", scaleXZ) + " (girth) × " + 
                           String.format("%.2f", scaleY) + " (height)");
    }

    /**
     * Apply color based on skin type, and apply sexual dimorphism coloration.
     * Also sets appropriate texture for the skin type.
     */
    private void applyColorAndTexture(AdvancedAICreature creature, GameObject3D model3D) {
        Color baseColor = Color.GRAY;
        String textureFile = "texture_default.png";
        String skin = creature.getSkinType().toLowerCase();

        // Determine base color and texture based on skin type
        if (skin.contains("scale")) {
            baseColor = new Color(0, 150, 200);  // Cyan-blue for scales
            textureFile = "texture_scales_shiny.png";
        } else if (skin.contains("fur")) {
            baseColor = new Color(139, 69, 19);  // Brown for fur
            textureFile = "texture_fur_shaggy.png";
        } else if (skin.contains("feather")) {
            baseColor = new Color(255, 100, 0);  // Orange for feathers
            textureFile = "texture_feathers.png";
        } else if (skin.contains("chitin")) {
            baseColor = new Color(80, 80, 80);   // Dark gray for chitin/carapace
            textureFile = "texture_chitin_reflective.png";
        }

        // Apply sexual dimorphism: females get more vibrant/exotic colors
        if (creature.getGender() == AdvancedAICreature.Gender.FEMALE) {
            baseColor = mixColors(baseColor, new Color(255, 0, 255), 0.3f);  // Mix toward magenta
            System.out.println("   > Sexual Dimorphism: Exotic coloration applied (Female)");
        }

        model3D.setColor(baseColor);
        model3D.setTexture(textureFile);
        System.out.println("   > Color: " + baseColor.toString() + " | Texture: " + textureFile);
    }

    /**
     * Enable/disable specific body parts based on locomotion type and mutations.
     * Assumes the 3D model has named sub-meshes for different body parts.
     */
    private void activateBodyParts(AdvancedAICreature creature, GameObject3D model3D) {
        AICreature.Locomotion loco = creature.getLocomotion();

        // Wings (for flying creatures)
        boolean hasWings = (loco == AICreature.Locomotion.FLYING);
        model3D.setSubMeshVisible("Wings", hasWings);

        // Fins (for swimming creatures)
        boolean hasFins = (loco == AICreature.Locomotion.SWIMMING);
        model3D.setSubMeshVisible("Fins", hasFins);

        // Legs visibility varies by locomotion type
        if (loco == AICreature.Locomotion.BIPEDAL || loco == AICreature.Locomotion.QUADRUPEDAL) {
            model3D.setSubMeshVisible("Legs", true);
        } else {
            model3D.setSubMeshVisible("Legs", false);
        }

        System.out.println("   > Body parts activated for locomotion: " + loco);
    }

    /**
     * Set animation or pose based on locomotion type.
     * Also adjust scaling for crawlers (flatten them slightly).
     */
    private void setPose(AdvancedAICreature creature, GameObject3D model3D) {
        AICreature.Locomotion loco = creature.getLocomotion();

        switch (loco) {
            case SWIMMING:
                model3D.setAnimation("Swim_Loop");
                break;
            case BIPEDAL:
                model3D.setAnimation("Walk_Bipedal");
                break;
            case QUADRUPEDAL:
                model3D.setAnimation("Walk_Quadrupedal");
                break;
            case FLYING:
                model3D.setAnimation("Fly_Loop");
                break;
            case CRAWLING:
                model3D.setAnimation("Crawl_Loop");
                // Flatten crawlers slightly (reduce Y scale by 30%)
                float currentScale = (float) creature.getHeight();
                currentScale *= 0.7f;
                model3D.setScale((float) (creature.getWeight() / 50.0), currentScale, 
                               (float) (creature.getWeight() / 50.0));
                System.out.println("   > Flattened crawling creature (Y × 0.7)");
                break;
        }

        System.out.println("   > Animation: " + (loco == AICreature.Locomotion.SWIMMING ? "Swim_Loop" :
                           loco == AICreature.Locomotion.BIPEDAL ? "Walk_Bipedal" :
                           loco == AICreature.Locomotion.QUADRUPEDAL ? "Walk_Quadrupedal" :
                           loco == AICreature.Locomotion.FLYING ? "Fly_Loop" : "Crawl_Loop"));
    }

    /**
     * Simple color mixing utility: blends two colors with a weight.
     * @param color1 First color
     * @param color2 Second color
     * @param weight Weight of color2 (0.0 = pure color1, 1.0 = pure color2)
     * @return Blended color
     */
    private Color mixColors(Color color1, Color color2, float weight) {
        weight = Math.max(0, Math.min(1, weight));
        int r = Math.round(color1.getRed() * (1 - weight) + color2.getRed() * weight);
        int g = Math.round(color1.getGreen() * (1 - weight) + color2.getGreen() * weight);
        int b = Math.round(color1.getBlue() * (1 - weight) + color2.getBlue() * weight);
        return new Color(r, g, b);
    }
}
