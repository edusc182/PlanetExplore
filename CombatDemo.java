import java.util.*;

public class CombatDemo {
    public static void main(String[] args) {
        Random rnd = new Random(42);

        List<AICreature> roster = new ArrayList<>();

        roster.add(new AICreature(new String[]{"level:3", "speed:fast", "agile", "strong-sense"}));
        roster.add(new AICreature(new String[]{"level:2", "speed:medium", "resilient", "high endurance"}));
        roster.add(new AICreature(new String[]{"level:4", "speed:low", "fragile", "quick-learner"}));
        roster.add(new AICreature(new String[]{"level:1", "speed:fast", "adaptive", "swimming"}));

        System.out.println("Initial roster and scores:");
        for (AICreature c : roster) {
            System.out.println(" - score=" + CombatSystem.computeScore(c) + " health=" + c.getHealth() + " -> " + c.getGeneticCode());
        }

        System.out.println("\nRanking:");
        List<AICreature> ranked = CombatSystem.rank(roster);
        int pos = 1;
        for (AICreature c : ranked) {
            System.out.println(String.format(" %d) score=%d health=%d %s", pos++, CombatSystem.computeScore(c), c.getHealth(), c.getGeneticCode()));
        }

        System.out.println("\nRunning sample duels (pairwise):");
        for (int i = 0; i < roster.size(); i++) {
            for (int j = i + 1; j < roster.size(); j++) {
                AICreature a = roster.get(i);
                AICreature b = roster.get(j);
                CombatSystem.DuelResult res = CombatSystem.duel(a, b, rnd);
                if (res.draw) {
                    System.out.println(String.format(" Duel %d vs %d: DRAW — both took damage, new healths: %d, %d",
                            i, j, a.getHealth(), b.getHealth()));
                } else {
                    System.out.println(String.format(" Duel %d vs %d: winner lineage=%d damage=%d — healths: %d, %d",
                            i, j, res.winner.getLineageId(), res.damageDealtToLoser, res.a.getHealth(), res.b.getHealth()));
                }
            }
        }

        System.out.println("\nFinal ranking after duels:");
        List<AICreature> finalRank = CombatSystem.rank(roster);
        pos = 1;
        for (AICreature c : finalRank) {
            System.out.println(String.format(" %d) score=%d health=%d %s", pos++, CombatSystem.computeScore(c), c.getHealth(), c.getGeneticCode()));
        }
    }
}
