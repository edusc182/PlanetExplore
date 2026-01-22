import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class PlanetGenerator {
    private String[] planetTypes = {"Lava", "Ice", "Desert", "Ocean", "Forest", "Swamp", "Mountain"};
    private String[] atmosphereList = {"Oxygen-rich", "Nitrogen", "Methane", "Carbon monoxide"};
    private List<AICreature> playerCreatures = new ArrayList<>();
    private final CreatureEvolutionManager evolutionManager = new CreatureEvolutionManager(new Random());
    private final Random rand = new Random();
    private String currentPlanetType;
    private String currentPlanetAtmosphere;
    private String currentPlanetName;
    private boolean competitionEnabled = true;
    private int numCompetitors = 2;

    private String generateCreativePlanetName() {
        String[] prefixes = {"Zyra", "Vega", "Orion", "Nova", "Astra", "Lumen", "Celes", "Pyra", "Cryo", "Terra"};
        String[] suffixes = {"lon", "thar", "dora", "nix", "mir", "vex", "tune", "lith", "dane", "phos"};
        Random random = new Random();
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        int number = 100 + random.nextInt(900); // 100-999
        return prefix + suffix + "-" + number;
    }

    private String[] generateAttributes(String planetType, String atmosphere) {
        List<String> attributes = new ArrayList<>();

        // Map planet types to a richer set of required/typical traits
        Map<String, String[]> required = new HashMap<>();
        required.put("Lava", new String[]{"Heat Resistance", "Fire Breath", "Magma Camouflage", "Thermal Vision"});
        required.put("Ice", new String[]{"Cold Resistance", "Ice Camouflage", "Antifreeze Blood", "Blubber"});
        required.put("Desert", new String[]{"Water Conservation", "Sand Burrower", "Heat Tolerance", "Night Vision"});
        required.put("Ocean", new String[]{"Swimming", "Gills", "Salt Tolerance", "Pressure Resistance"});
        required.put("Forest", new String[]{"Climbing", "Leaf Camouflage", "Camouflage Scent", "Nocturnal"});
        // New world types with larger requirement sets
        required.put("Swamp", new String[]{"Mire Grip", "Toxin Metabolism", "Buoyant", "Camouflage Algae", "Amphibious Respiration"});
        required.put("Mountain", new String[]{"Cliff Climbing", "Thick Fur", "High Altitude Lung", "Rock Camouflage", "Low Oxygen Tolerance"});

        String[] base = required.get(planetType);
        if (base != null) {
            for (String b : base) attributes.add(b);
        }
        // Add attributes based on atmosphere
        if (atmosphere.contains("Oxygen")) {
            attributes.add("High Endurance");
        } else if (atmosphere.contains("Nitrogen")) {
            attributes.add("Nitrogen Tolerance");
        } else if (atmosphere.contains("Methane")) {
            attributes.add("Methane Breather");
        } else if (atmosphere.contains("Carbon monoxide")) {
            attributes.add("Toxic Resistance");
        }
        return attributes.toArray(new String[0]);
    }

    private AICreature createAICreature(String[] attributes) {
        return new AICreature(attributes);
    }

    private void addCreatureToPlayerList(AICreature creature) {
        playerCreatures.add(creature);
        System.out.println("AI Creature added to player list: " + creature);
    }

    private void updateCreatures() {
        // Use CreatureEvolutionManager to update playerCreatures with environment info
        evolutionManager.updateCreatures(playerCreatures, this.currentPlanetType, this.currentPlanetAtmosphere);
    }

    private void createPlanetWithType(String type, String atmosphere, double distanceFromStar) {
        System.out.println("Creating planet: Type=" + type + ", Atmosphere=" + atmosphere + ", Distance=" + distanceFromStar);
    }

    private void addPlanetToDatabase(Planet planet) {
        System.out.println("Adding planet to database: " + planet.getName());
    }

    public void generatePlanetAndCreature() {
        Random random = new Random();
        int typeIndex = random.nextInt(planetTypes.length);
        String type = planetTypes[typeIndex];
        int atmosphereIndex = random.nextInt(atmosphereList.length);
        String atmosphere = atmosphereList[atmosphereIndex];
        double distanceFromStar = random.nextDouble();
        
        // Store planet type and atmosphere for asset loading
        this.currentPlanetType = type;
        this.currentPlanetAtmosphere = atmosphere;
        
        createPlanetWithType(type, atmosphere, distanceFromStar);
        String creativeName = generateCreativePlanetName();
        this.currentPlanetName = creativeName;
        Planet planetName = new Planet(creativeName);
        addPlanetToDatabase(planetName);
        // Generate N AI creatures (competitors) based on planet
        for (int i = 0; i < numCompetitors; i++) {
            String spawnType = (i == 0) ? type : planetTypes[(typeIndex + i) % planetTypes.length];
            String spawnAtmo = atmosphereList[random.nextInt(atmosphereList.length)];
            String[] attributes = generateAttributes(spawnType, spawnAtmo);
            AICreature aiCreature = createAICreature(attributes);
            addCreatureToPlayerList(aiCreature);
            System.out.println("Spawned competitor #" + (i+1) + ": Type=" + spawnType + ", Atmosphere=" + spawnAtmo + " -> " + aiCreature);
        }
        updateCreatures();
    }

    /**
     * Run multiple evolution cycles and log only attribute changes and survivability.
     */
    public void runEvolutionSimulation(int cycles) {
        if (playerCreatures.isEmpty()) return;

        // Determine required planet traits (first two entries from generateAttributes)
        String[] baseline = generateAttributes(this.currentPlanetType, this.currentPlanetAtmosphere);
        Set<String> requiredTraits = new HashSet<>();
        if (baseline.length >= 2) {
            requiredTraits.add(baseline[0]);
            requiredTraits.add(baseline[1]);
        }

        double crisisChance = 0.15; // 15% chance of a PlanetCrisis each generation
        for (int gen = 1; gen <= cycles; gen++) {
            boolean envChanged = false;
            // random PlanetCrisis can occur at any generation
                if (rand.nextDouble() < crisisChance) {
                String[] env = EventEngine.triggerCrisis(rand, this.currentPlanetType, this.currentPlanetAtmosphere);
                this.setPlanetEnvironment(env[0], env[1]);
                envChanged = true;
                // recompute required traits after crisis
                baseline = generateAttributes(this.currentPlanetType, this.currentPlanetAtmosphere);
                requiredTraits.clear();
                if (baseline.length >= 2) {
                    requiredTraits.add(baseline[0]);
                    requiredTraits.add(baseline[1]);
                }

                // immediately recharge adaptive charges because environment just changed
                for (AICreature c : playerCreatures) {
                    c.rechargeAdaptive();
                }

                // apply immediate fitness penalties for missing vital traits
                List<AICreature> dead = new ArrayList<>();
                for (AICreature c : playerCreatures) {
                    Set<String> afterSet = new HashSet<>(Arrays.asList(c.getAttributes()));
                    Set<String> missing = new HashSet<>(requiredTraits);
                    missing.removeAll(afterSet);
                    if (!missing.isEmpty()) {
                        double baseDmg = 30.0 * missing.size();
                        int usedAdaptive = 0;
                        if (afterSet.contains("adaptive")) {
                            // consume up to min(2, missing.size()) charges
                            usedAdaptive = c.useAdaptive(Math.min(2, missing.size()));
                        }
                        // each used adaptive charge halves the damage
                        double dmg = baseDmg * Math.pow(0.5, usedAdaptive);
                        if (afterSet.contains("resilient")) dmg *= 0.5;
                        int idmg = (int)Math.ceil(dmg);
                        c.reduceHealth(idmg);
                        System.out.println("CRISIS DAMAGE applied " + idmg + " to " + c + " missing=" + missing + " adaptiveUsed=" + usedAdaptive);
                        if (!c.isAlive()) {
                            dead.add(c);
                        }
                    }
                }
                if (!dead.isEmpty()) {
                    for (AICreature d : dead) {
                        // save fossil before removing
                        FossilRecord.saveFossil(d, this.currentPlanetName, this.currentPlanetType, this.currentPlanetAtmosphere, "PlanetCrisis");
                        playerCreatures.remove(d);
                        System.out.println("EXTINCTION: " + d + " removed from population (fossil saved)");
                        // respawn a new creature to start a new lineage
                        String[] newAttrs = generateAttributes(this.currentPlanetType, this.currentPlanetAtmosphere);
                        AICreature newborn = createAICreature(newAttrs);
                        addCreatureToPlayerList(newborn);
                        System.out.println("RESPAWN: New lineage started: " + newborn);
                    }
                }
            }
            // mid-simulation environmental event: change planet at halfway point
            if (gen == (cycles / 2) + 1) {
                // example: orbit shift -> Desert to Ice, Ocean to Desert, Lava to Ocean
                simulateOrbitShift();
                envChanged = true;
                // recompute required traits after environment change
                baseline = generateAttributes(this.currentPlanetType, this.currentPlanetAtmosphere);
                requiredTraits.clear();
                if (baseline.length >= 2) {
                    requiredTraits.add(baseline[0]);
                    requiredTraits.add(baseline[1]);
                }
            }

            // snapshot before entire generation
            List<String[]> beforeAll = new ArrayList<>();
            for (AICreature c : playerCreatures) beforeAll.add(c.getAttributes().clone());

            // perform one environment-aware update for the generation
            evolutionManager.updateCreatures(playerCreatures, this.currentPlanetType, this.currentPlanetAtmosphere);

            // apply regeneration or reset stability based on whether environment changed this generation
            if (envChanged) {
                for (AICreature c : playerCreatures) c.resetStability();
            } else {
                for (AICreature c : playerCreatures) c.incrementStableAndMaybeHeal();
            }

            // compare and report per-creature changes
            for (int i = 0; i < playerCreatures.size(); i++) {
                AICreature creature = playerCreatures.get(i);
                List<String> before = Arrays.asList(beforeAll.get(i));
                List<String> after = Arrays.asList(creature.getAttributes());

                Set<String> beforeSet = new HashSet<>(before);
                Set<String> afterSet = new HashSet<>(after);

                Set<String> added = new HashSet<>(afterSet);
                added.removeAll(beforeSet);

                Set<String> removed = new HashSet<>(beforeSet);
                removed.removeAll(afterSet);

                boolean printed = false;
                if (!added.isEmpty() || !removed.isEmpty()) {
                    System.out.println("Gen " + gen + ": Changes for " + creature + "\n  +" + added + "\n  -" + removed);
                    printed = true;
                }

                // survivability: check presence of required planet traits
                Set<String> missing = new HashSet<>(requiredTraits);
                missing.removeAll(afterSet);
                if (!missing.isEmpty()) {
                    if (afterSet.contains("adaptive")) {
                        System.out.println("Gen " + gen + ": ADAPTIVE mitigated missing=" + missing + " for " + creature);
                    } else {
                        System.out.println("Gen " + gen + ": SURVIVABILITY WARNING for " + creature + " missing=" + missing);
                    }
                    printed = true;
                }

                if (!printed) {
                    // keep console minimal
                }
            }

            // Interspecies competition round (if at least two creatures)
            if (competitionEnabled && playerCreatures.size() >= 2) {
                // compute initial scores and print ranking
                List<AICreature> pool = new ArrayList<>(playerCreatures);
                pool.sort((c1, c2) -> Integer.compare(computeCompetitionScore(c2, requiredTraits), computeCompetitionScore(c1, requiredTraits)));
                System.out.println("Gen " + gen + ": Competition ranking:");
                for (AICreature c : pool) {
                    System.out.println("  " + c.getGeneticCode() + " -> score=" + computeCompetitionScore(c, requiredTraits));
                }

                // Run pairwise duels among the pool (each unique pair fights once)
                List<AICreature> toRemove = new ArrayList<>();
                for (int i = 0; i < pool.size(); i++) {
                    for (int j = i + 1; j < pool.size(); j++) {
                        AICreature a = pool.get(i);
                        AICreature b = pool.get(j);
                        if (!a.isAlive() || !b.isAlive()) continue; // skip dead
                        CombatSystem.DuelResult res = CombatSystem.duel(a, b, rand);
                        if (res.draw) {
                            System.out.println(String.format("  Duel %d vs %d: DRAW — %s and %s both took damage (h=%d, h=%d)", i, j, a.getGeneticCode(), b.getGeneticCode(), a.getHealth(), b.getHealth()));
                        } else {
                            System.out.println(String.format("  Duel %d vs %d: winner lineage=%d damage=%d — healths: %d, %d", i, j, res.winner.getLineageId(), res.damageDealtToLoser, res.a.getHealth(), res.b.getHealth()));
                        }

                        if (!a.isAlive() && !toRemove.contains(a)) toRemove.add(a);
                        if (!b.isAlive() && !toRemove.contains(b)) toRemove.add(b);
                    }
                }

                // Process deaths and respawn
                for (AICreature dead : toRemove) {
                    FossilRecord.saveFossil(dead, this.currentPlanetName, this.currentPlanetType, this.currentPlanetAtmosphere, "Competition_Duel");
                    playerCreatures.remove(dead);
                    System.out.println("EXTINCTION (duel): " + dead + " removed (fossil saved)");
                    String[] newAttrs = generateAttributes(this.currentPlanetType, this.currentPlanetAtmosphere);
                    AICreature newborn = createAICreature(newAttrs);
                    addCreatureToPlayerList(newborn);
                    System.out.println("RESPAWN (duel): New lineage started: " + newborn);
                }

                // After duels, heal the top-ranked survivor as reward
                List<AICreature> survivors = new ArrayList<>(playerCreatures);
                if (!survivors.isEmpty()) {
                    survivors.sort((c1, c2) -> Integer.compare(computeCompetitionScore(c2, requiredTraits), computeCompetitionScore(c1, requiredTraits)));
                    AICreature winner = survivors.get(0);
                    int winnerScore = computeCompetitionScore(winner, requiredTraits);
                    winner.heal(10 + Math.max(0, survivors.size()-1) * 2);
                    System.out.println("  Winner after duels: " + winner + " (score=" + winnerScore + ")\n");
                }
            }
        }
    }

    private int computeCompetitionScore(AICreature c, Set<String> requiredTraits) {
        int score = c.getHealth();
        Set<String> attrs = new HashSet<>(Arrays.asList(c.getAttributes()));
        int matches = 0;
        for (String t : requiredTraits) if (attrs.contains(t)) matches++;
        score += matches * 50; // big advantage for matching required traits
        score += c.getMutationCount() * 2; // small benefit for evolvability
        if (attrs.contains("agile") || attrs.contains("quick-learner")) score += 10;
        if (attrs.contains("strong-sense")) score += 10;
        return score;
    }

    private void simulateOrbitShift() {
        System.out.println("-- ORBIT SHIFT EVENT: Planet environment is changing --");
        String prevType = this.currentPlanetType;
        String prevAtmo = this.currentPlanetAtmosphere;
        // small deterministic mapping for example
        switch (prevType) {
            case "Desert": this.currentPlanetType = "Ice"; this.currentPlanetAtmosphere = "Nitrogen"; break;
            case "Ocean": this.currentPlanetType = "Desert"; this.currentPlanetAtmosphere = "Methane"; break;
            case "Lava": this.currentPlanetType = "Ocean"; this.currentPlanetAtmosphere = "Carbon monoxide"; break;
            case "Ice": this.currentPlanetType = "Forest"; this.currentPlanetAtmosphere = "Oxygen-rich"; break;
            case "Forest": this.currentPlanetType = "Desert"; this.currentPlanetAtmosphere = "Nitrogen"; break;
            default: this.currentPlanetType = "Forest"; this.currentPlanetAtmosphere = "Oxygen-rich"; break;
        }
        System.out.println("Planet changed: " + prevType + " (" + prevAtmo + ") -> " + this.currentPlanetType + " (" + this.currentPlanetAtmosphere + ")");
        // immediate recharge for adaptive trait after a major environmental shift
        for (AICreature c : playerCreatures) {
            c.rechargeAdaptive();
        }
    }

    public void setPlanetEnvironment(String type, String atmosphere) {
        this.currentPlanetType = type;
        this.currentPlanetAtmosphere = atmosphere;
    }

    public String getCurrentPlanetType() {
        return this.currentPlanetType;
    }

    public String getCurrentPlanetAtmosphere() {
        return this.currentPlanetAtmosphere;
    }

    private List<AssetType> generateAssetsForPlanet(String planetType, String atmosphere) {
        List<AssetType> assets = new ArrayList<>();
        // All planets get a creature model
        assets.add(new AssetType("model", "Spore_Creature_Model.obj"));
        
        // Map planet type to appropriate background and texture
        switch (planetType) {
            case "Lava":
                assets.add(new AssetType("texture", "Molten_Rock_Texture.png"));
                assets.add(new AssetType("background", "Lava_Planet_Background.jpg"));
                break;
            case "Ice":
                assets.add(new AssetType("texture", "Frozen_Tundra_Texture.png"));
                assets.add(new AssetType("background", "Ice_Planet_Background.jpg"));
                break;
            case "Desert":
                assets.add(new AssetType("texture", "Sand_Dune_Texture.png"));
                assets.add(new AssetType("background", "Desert_Planet_Background.jpg"));
                break;
            case "Ocean":
                assets.add(new AssetType("texture", "Water_Surface_Texture.png"));
                assets.add(new AssetType("background", "Ocean_Planet_Background.jpg"));
                break;
            case "Forest":
                assets.add(new AssetType("texture", "Alien_Forest_Texture.png"));
                assets.add(new AssetType("background", "Forest_Planet_Background.jpg"));
                break;
        }
        
        // Map atmosphere to appropriate audio
        if (atmosphere.contains("Oxygen")) {
            assets.add(new AssetType("audio", "Windy_Atmosphere.wav"));
        } else if (atmosphere.contains("Nitrogen")) {
            assets.add(new AssetType("audio", "Nitrogen_Hum.wav"));
        } else if (atmosphere.contains("Methane")) {
            assets.add(new AssetType("audio", "Methane_Bubbles.wav"));
        } else if (atmosphere.contains("Carbon monoxide")) {
            assets.add(new AssetType("audio", "Toxic_Atmosphere.wav"));
        }
        
        return assets;
    }

    public void exploreSceneWithAssets() {
        List<AssetType> assetList = generateAssetsForPlanet(currentPlanetType, currentPlanetAtmosphere);
        AssetManager assetManager = new AssetManager(assetList);
        Scene scene = new Scene();
        scene.loadAssets(assetManager);
    }

    public static void main(String[] args) {
        PlanetGenerator generator = new PlanetGenerator();
        // parse CLI args
        int generations = 10;
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.equals("--no-competition")) generator.competitionEnabled = false;
            if (a.equals("--competitors") && i+1 < args.length) {
                try { generator.numCompetitors = Integer.parseInt(args[i+1]); } catch (Exception e) {}
            }
            if (a.equals("--generations") && i+1 < args.length) {
                try { generations = Integer.parseInt(args[i+1]); } catch (Exception e) {}
            }
            if (a.equals("--print-lineages")) {
                // after run we will print lineages as well; mark with a system property in generator
                // store in a small field by using reflection-like quick flag: reuse competitionEnabled false/true
                // Instead, use an environment-like static call after generating
            }
        }

        generator.generatePlanetAndCreature();
        // Run evolution simulation and then load assets
        generator.runEvolutionSimulation(generations);
        // Print Hall of Fame (fossil summaries)
        FossilRecord.printHallOfFame();
        // Optionally print lineage histories when requested via --print-lineages
        for (String a : args) if (a.equals("--print-lineages")) FossilRecord.printLineageHistories();
        generator.exploreSceneWithAssets();
    }
}
