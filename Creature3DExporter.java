/**
 * Creature3DExporter: Exporta datos de criaturas en formatos estándar para motores 3D.
 * 
 * Soporta:
 * - OBJ (Wavefront) para Blender, Unity, Unreal
 * - JSON con parámetros físicos para Three.js
 * - CSV para análisis de datos
 */
public class Creature3DExporter {

    /**
     * Exporta una criatura a formato OBJ (Wavefront 3D).
     * Este archivo puede importarse a Blender, Unity, Unreal, etc.
     * 
     * @param creature La criatura a exportar
     * @return Contenido del archivo OBJ como String
     */
    public static String exportToOBJ(AICreature creature) {
        StringBuilder obj = new StringBuilder();
        
        // Header
        obj.append("# Creature OBJ Model\n");
        obj.append("# Generated from Genetic Code: ").append(creature.getGeneticCode()).append("\n");
        obj.append("# Lineage: ").append(creature.getLineageId()).append("\n");
        obj.append("# Age: ").append(creature.getAge()).append("\n\n");

        // Export physical properties as comments
        if (creature instanceof AdvancedAICreature) {
            AdvancedAICreature adv = (AdvancedAICreature) creature;
            obj.append("# Physical Properties\n");
            obj.append("# Weight: ").append(adv.getWeight()).append(" kg\n");
            obj.append("# Height: ").append(adv.getHeight()).append(" m\n");
            obj.append("# Gender: ").append(adv.getGender()).append("\n");
            obj.append("# Locomotion: ").append(adv.getLocomotion()).append("\n");
            obj.append("# Skin Type: ").append(adv.getSkinType()).append("\n");
            obj.append("# Color: ").append(adv.getColor()).append("\n\n");
        }

        // Generate vertices for basic spheroid body
        double weight = creature instanceof AdvancedAICreature ? 
            ((AdvancedAICreature) creature).getWeight() : 50.0;
        double height = creature instanceof AdvancedAICreature ? 
            ((AdvancedAICreature) creature).getHeight() : 1.0;

        float scaleX = (float) (weight / 50.0);
        float scaleY = (float) height;
        float scaleZ = (float) (weight / 50.0);

        // Generate vertices in spheroid pattern
        int segments = 10;
        for (int i = 0; i < segments; i++) {
            for (int j = 0; j < segments; j++) {
                double theta = (2.0 * Math.PI * j) / segments;
                double phi = (Math.PI * i) / (segments - 1);
                
                double x = scaleX * Math.sin(phi) * Math.cos(theta);
                double y = scaleY * Math.cos(phi);
                double z = scaleZ * Math.sin(phi) * Math.sin(theta);
                
                obj.append(String.format("v %.3f %.3f %.3f\n", x, y, z));
            }
        }

        // Vertex normals
        obj.append("\n# Vertex Normals\n");
        for (int i = 0; i < segments * segments; i++) {
            obj.append("vn 0.0 1.0 0.0\n");
        }

        // Faces (triangles)
        obj.append("\n# Faces\n");
        for (int i = 0; i < segments - 1; i++) {
            for (int j = 0; j < segments; j++) {
                int a = i * segments + j + 1;
                int b = i * segments + ((j + 1) % segments) + 1;
                int c = (i + 1) * segments + j + 1;
                int d = (i + 1) * segments + ((j + 1) % segments) + 1;
                
                obj.append(String.format("f %d %d %d\n", a, b, c));
                obj.append(String.format("f %d %d %d\n", b, d, c));
            }
        }

        return obj.toString();
    }

    /**
     * Exporta una criatura a formato JSON con parámetros para Three.js o Babylon.js.
     */
    public static String exportToJSON(AICreature creature) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"creature\": {\n");
        json.append("    \"geneticCode\": \"").append(creature.getGeneticCode()).append("\",\n");
        json.append("    \"lineageId\": ").append(creature.getLineageId()).append(",\n");
        json.append("    \"age\": ").append(creature.getAge()).append(",\n");
        json.append("    \"health\": ").append(creature.getHealth()).append(",\n");

        if (creature instanceof AdvancedAICreature) {
            AdvancedAICreature adv = (AdvancedAICreature) creature;
            json.append("    \"physical\": {\n");
            json.append("      \"weight\": ").append(adv.getWeight()).append(",\n");
            json.append("      \"height\": ").append(adv.getHeight()).append(",\n");
            json.append("      \"scaleX\": ").append(adv.getWeight() / 50.0).append(",\n");
            json.append("      \"scaleY\": ").append(adv.getHeight()).append(",\n");
            json.append("      \"scaleZ\": ").append(adv.getWeight() / 50.0).append(",\n");
            json.append("      \"gender\": \"").append(adv.getGender()).append("\",\n");
            json.append("      \"locomotion\": \"").append(adv.getLocomotion()).append("\",\n");
            json.append("      \"skinType\": \"").append(adv.getSkinType()).append("\",\n");
            json.append("      \"color\": \"").append(adv.getColor()).append("\"\n");
            json.append("    },\n");
        }

        json.append("    \"traits\": [\n");
        String[] attributes = creature.getAttributes();
        for (int i = 0; i < attributes.length; i++) {
            json.append("      \"").append(attributes[i]).append("\"");
            if (i < attributes.length - 1) json.append(",");
            json.append("\n");
        }
        json.append("    ]\n");
        json.append("  }\n");
        json.append("}\n");

        return json.toString();
    }

    /**
     * Exporta una criatura a formato CSV para análisis de datos.
     */
    public static String exportToCSV(AICreature creature) {
        StringBuilder csv = new StringBuilder();
        
        csv.append("GeneticCode,LineageID,Age,Health,TotalDamage,Traits\n");
        csv.append(creature.getGeneticCode()).append(",");
        csv.append(creature.getLineageId()).append(",");
        csv.append(creature.getAge()).append(",");
        csv.append(creature.getHealth()).append(",");
        csv.append(creature.getTotalDamageTaken()).append(",");
        
        // Traits as comma-separated (quoted to handle embedded commas)
        csv.append("\"");
        String[] attrs = creature.getAttributes();
        for (int i = 0; i < attrs.length; i++) {
            csv.append(attrs[i]);
            if (i < attrs.length - 1) csv.append(";");
        }
        csv.append("\"\n");

        return csv.toString();
    }

    /**
     * Exporta datos de varias criaturas a un batch OBJ con manifest.
     */
    public static String exportBatchOBJManifest(java.util.List<AICreature> creatures) {
        StringBuilder manifest = new StringBuilder();
        manifest.append("# Creature Batch Export\n");
        manifest.append("# Total creatures: ").append(creatures.size()).append("\n\n");

        for (int i = 0; i < creatures.size(); i++) {
            AICreature creature = creatures.get(i);
            String filename = "creature_" + creature.getLineageId() + "_gen" + creature.getAge() + ".obj";
            manifest.append("File: ").append(filename).append("\n");
            manifest.append("  Code: ").append(creature.getGeneticCode()).append("\n");
            if (creature instanceof AdvancedAICreature) {
                AdvancedAICreature adv = (AdvancedAICreature) creature;
                manifest.append("  Type: ").append(adv.getLocomotion()).append("\n");
            }
            manifest.append("\n");
        }

        return manifest.toString();
    }
}
