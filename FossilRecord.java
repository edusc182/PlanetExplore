import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * FossilRecord: saves human-readable fossils and a CSV 'hall of fame' for quick
 * tabular display. The CSV is appended to as new extinctions occur.
 */

public class FossilRecord {
    private static final String FOSSIL_FILE = "fossils.txt";
    private static final String FOSSIL_CSV = "fossils.csv";

    public static void saveFossil(AICreature creature, String planetName, String planetType, String atmosphere, String cause) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Human-readable fossil log (append)
        StringBuilder sb = new StringBuilder();
        sb.append("--- FOSSIL RECORD ---\n");
        sb.append("timestamp: ").append(timestamp).append("\n");
        sb.append("planetName: ").append(planetName).append("\n");
        sb.append("planetType: ").append(planetType).append("\n");
        sb.append("atmosphere: ").append(atmosphere).append("\n");
        sb.append("age: ").append(creature.getAge()).append("\n");
        sb.append("health: ").append(creature.getHealth()).append("\n");
        sb.append("cause: ").append(cause).append("\n");
        sb.append("attributes: ");
        StringBuilder traitsJoin = new StringBuilder();
        for (int i = 0; i < creature.getAttributes().length; i++) {
            String a = creature.getAttributes()[i];
            sb.append(a).append(",");
            if (i > 0) traitsJoin.append(";");
            traitsJoin.append(a);
        }
        sb.append("\n");
        sb.append("totalDamageTaken: ").append(creature.getTotalDamageTaken()).append("\n");
        sb.append("mutationCount: ").append(creature.getMutationCount()).append("\n\n");

        try (FileWriter fw = new FileWriter(FOSSIL_FILE, true)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.err.println("Failed to write fossil record: " + e.getMessage());
        }

        // CSV Hall-of-Fame append (id auto-increment)
        try {
            File csv = new File(FOSSIL_CSV);
            boolean writeHeader = !csv.exists();
            int nextId = 1;
            if (csv.exists()) {
                // count existing non-empty lines (subtract header)
                try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
                    int lines = 0;
                    while (br.readLine() != null) lines++;
                    nextId = Math.max(1, lines); // header occupies first line
                }
            }

            try (FileWriter fw = new FileWriter(csv, true)) {
                if (writeHeader) {
                    // geneticCode included for lineage tracking
                    fw.write("id,timestamp,planetName,planetType,atmosphere,finalAge,cause,geneticCode,survivalTraits,damageTaken,mutationCount\n");
                }
                // traits are separated by semicolon to avoid CSV comma conflicts
                String traitsEscaped = traitsJoin.toString().replace("\n", " ").replace("\r", " ");
                String genetic = creature.getGeneticCode();
                fw.write(String.format("%d,%s,%s,%s,%s,%d,%s,%s,%s,%d,%d\n",
                        nextId,
                        timestamp,
                        sanitizeCsv(planetName),
                        sanitizeCsv(planetType),
                        sanitizeCsv(atmosphere),
                        creature.getAge(),
                        sanitizeCsv(cause),
                        sanitizeCsv(genetic),
                        sanitizeCsv(traitsEscaped),
                        creature.getTotalDamageTaken(),
                        creature.getMutationCount()));
            }
        } catch (IOException e) {
            System.err.println("Failed to append fossil CSV: " + e.getMessage());
        }
    }

    private static String sanitizeCsv(String s) {
        if (s == null) return "";
        return s.replace(",", "|").replace("\n", " ").replace("\r", " ");
    }

    public static void printHallOfFame() {
        File csv = new File(FOSSIL_CSV);
        if (!csv.exists()) {
            System.out.println("Hall of Fame is empty (no fossils yet).");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String header = br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] cols = line.split(",", 11);
                if (cols.length >= 11) rows.add(cols);
            }
        } catch (IOException e) {
            System.err.println("Failed to read Hall of Fame: " + e.getMessage());
            return;
        }

        // Sort by finalAge desc (column index 5)
        Collections.sort(rows, new Comparator<String[]>() {
            public int compare(String[] a, String[] b) {
                try {
                    int ai = Integer.parseInt(a[5]);
                    int bi = Integer.parseInt(b[5]);
                    return Integer.compare(bi, ai);
                } catch (Exception e) { return 0; }
            }
        });

        System.out.println("\n=== Hall of Fame (Fossil Record) ===");
        System.out.printf("%-4s %-8s %-12s %-20s %-20s %-10s %-12s\n", "ID", "Age", "Cause", "Traits", "Planet", "Damage", "Mutations");
        for (String[] r : rows) {
            String id = r[0];
            String age = r[5];
            String cause = r[6];
            String genetic = r[7];
            String traits = r[8];
            String planet = r[2] + "(" + r[3] + ")";
            String damage = r[9];
            String muts = r[10];
            if (traits.length() > 18) traits = traits.substring(0, 18) + "..";
            if (planet.length() > 18) planet = planet.substring(0, 18) + "..";
            System.out.printf("%-4s %-8s %-12s %-20s %-20s %-10s %-12s\n", id, age, cause + "(" + genetic + ")", traits, planet, damage, muts);
        }
        System.out.println("=== End Hall of Fame ===\n");
    }

    public static void printLineageHistories() {
        File csv = new File(FOSSIL_CSV);
        if (!csv.exists()) {
            System.out.println("No fossil CSV available for lineage histories.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String header = br.readLine();
            String line;
            System.out.println("\n=== Fossil Lineage Entries ===");
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] cols = line.split(",", 11);
                if (cols.length < 11) continue;
                String id = cols[0];
                String genetic = cols[7];
                String age = cols[5];
                String cause = cols[6];
                System.out.println("Fossil#" + id + " | " + genetic + " | age=" + age + " | cause=" + cause);
            }
            System.out.println("=== End Lineage Entries ===\n");
        } catch (IOException e) {
            System.err.println("Failed to read lineage histories: " + e.getMessage());
        }
    }
}
