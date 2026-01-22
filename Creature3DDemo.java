import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Creature3DDemo: Demostración del sistema de visualización y exportación 3D.
 * 
 * Muestra:
 * - Visualización ASCII de criaturas
 * - Exportación a OBJ (Wavefront)
 * - Exportación a JSON (Three.js)
 * - Exportación a CSV (análisis de datos)
 */
public class Creature3DDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║  🎨 3D Creature Visualization & Export System Demo      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        // Create diverse creatures
        List<AdvancedAICreature> creatures = new ArrayList<>();

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.SWIMMING,
            "Scales", "Blue", "Streamlined",
            8.0, 0.45,
            new String[]{"Gills", "Swimming", "Salt Tolerance"}
        ));

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.BIPEDAL,
            "Fur", "Orange", "Upright",
            45.0, 1.6,
            new String[]{"Intelligence", "Tool Use"}
        ));

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.FLYING,
            "Feathers", "Red", "Aerodynamic",
            2.5, 0.35,
            new String[]{"Beak", "Sharp Vision"}
        ));

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.QUADRUPEDAL,
            "Fur", "Brown", "Robust",
            55.0, 1.3,
            new String[]{"Claws", "Pack Hunter"}
        ));

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.CRAWLING,
            "Chitin", "Gray", "Elongated",
            12.0, 0.6,
            new String[]{"Armor Plates", "Burrowing"}
        ));

        // 1. Render each creature as ASCII art
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("PART 1: ASCII 3D VISUALIZATION");
        System.out.println("═══════════════════════════════════════════════════════\n");

        for (AICreature creature : creatures) {
            Creature3DVisualizerASCII.renderCreature(creature);
        }

        // 2. Show comparison panel
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("PART 2: CREATURE COMPARISON");
        System.out.println("═══════════════════════════════════════════════════════");
        Creature3DVisualizerASCII.renderComparison(new ArrayList<>(creatures));

        // 3. Export to various formats
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("PART 3: 3D EXPORT FORMATS");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // Export first creature as OBJ
        System.out.println("📦 Exporting to OBJ (Wavefront 3D)...");
        String objContent = Creature3DExporter.exportToOBJ(creatures.get(0));
        saveToFile("creature_0.obj", objContent);
        System.out.println("   ✓ Saved as: creature_0.obj");
        System.out.println("   → Import to Blender, Unity, Unreal with this file\n");

        // Export as JSON for Three.js
        System.out.println("📦 Exporting to JSON (Three.js/Babylon.js)...");
        String jsonContent = Creature3DExporter.exportToJSON(creatures.get(1));
        saveToFile("creature_1.json", jsonContent);
        System.out.println("   ✓ Saved as: creature_1.json");
        System.out.println("   → Use with Three.js scene loader\n");

        // Export as CSV for data analysis
        System.out.println("📦 Exporting to CSV (Data Analysis)...");
        StringBuilder csvContent = new StringBuilder();
        for (AICreature creature : creatures) {
            csvContent.append(Creature3DExporter.exportToCSV(creature));
        }
        saveToFile("creatures_export.csv", csvContent.toString());
        System.out.println("   ✓ Saved as: creatures_export.csv");
        System.out.println("   → Open in Excel or your analysis tool\n");

        // Export batch manifest
        System.out.println("📦 Creating batch export manifest...");
        String manifest = Creature3DExporter.exportBatchOBJManifest(new ArrayList<>(creatures));
        saveToFile("creatures_manifest.txt", manifest);
        System.out.println("   ✓ Saved as: creatures_manifest.txt");
        System.out.println("   → Reference guide for all exported files\n");

        // Summary
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║  ✅ EXPORT COMPLETE                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        System.out.println("📊 Summary:");
        System.out.println("   Total creatures exported: " + creatures.size());
        System.out.println("   Formats available:");
        System.out.println("     • OBJ - 3D mesh (Blender, Unity, Unreal)");
        System.out.println("     • JSON - Data & parameters (Three.js, custom engines)");
        System.out.println("     • CSV - Tabular data (Excel, databases)");
        System.out.println("     • TXT - Manifest (documentation)");
        System.out.println();

        System.out.println("🎮 Next steps:");
        System.out.println("   1. Open creature_0.obj in Blender for 3D editing");
        System.out.println("   2. Import creature_1.json to Three.js scene");
        System.out.println("   3. Analyze creatures_export.csv for genetics research");
        System.out.println("   4. Use manifest.txt as reference for batch operations");
        System.out.println();

        System.out.println("💡 Integration paths:");
        System.out.println("   → Unity: Import .obj files via Assets menu");
        System.out.println("   → Blender: File > Import > Wavefront (.obj)");
        System.out.println("   → Three.js: Use OBJLoader with creature_0.obj");
        System.out.println("   → Unreal Engine: Content Browser > Import (drag .obj)");
        System.out.println();
    }

    /**
     * Save string content to a file.
     */
    private static void saveToFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("   ✗ Error saving " + filename + ": " + e.getMessage());
        }
    }
}
