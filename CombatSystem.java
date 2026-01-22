import java.util.*;

public class CombatSystem {

    public static int computeScore(AICreature c) {
        double score = 0.0;

        // base from attributes
        String[] attrs = c.getAttributes();
        if (attrs != null) {
            for (String a : attrs) {
                if (a == null) continue;
                String lower = a.toLowerCase();

                if (lower.startsWith("level:")) {
                    try {
                        int lvl = Integer.parseInt(lower.split(":")[1]);
                        score += lvl * 10; // big impact
                    } catch (Exception e) {
                        score += 5;
                    }
                } else if (lower.startsWith("speed:")) {
                    if (lower.contains("fast")) score += 6;
                    else if (lower.contains("medium")) score += 3;
                    else score += 1;
                } else if (lower.contains("agile")) score += 4;
                else if (lower.contains("adaptive")) score += 3;
                else if (lower.contains("resilient")) score += 3;
                else if (lower.contains("quick-learner")) score += 2;
                else if (lower.contains("strong-sense") || lower.contains("perception")) score += 2;
                else if (lower.contains("fragile")) score -= 4;
                else if (lower.contains("slow-reaction")) score -= 3;
                else if (lower.contains("weak-sense")) score -= 2;

                // environment survival traits
                else if (lower.contains("heat resistance") || lower.contains("cold resistance") || lower.contains("swimming")
                        || lower.contains("water conservation") || lower.contains("toxic resistance") || lower.contains("methane breather")
                        || lower.contains("nitrogen tolerance") || lower.contains("high endurance")) {
                    score += 3;
                }
            }
        }

        // health contributes moderately
        score += c.getHealth() / 10.0;

        return (int)Math.round(score);
    }

    public static DuelResult duel(AICreature a, AICreature b, Random rnd) {
        int sa = computeScore(a);
        int sb = computeScore(b);

        // determine winner
        boolean draw = false;
        AICreature winner = null;
        AICreature loser = null;

        if (sa == sb) {
            // tie-breaker: higher health, then random
            if (a.getHealth() == b.getHealth()) {
                draw = true;
            } else if (a.getHealth() > b.getHealth()) {
                winner = a; loser = b;
            } else {
                winner = b; loser = a;
            }
        } else if (sa > sb) {
            winner = a; loser = b;
        } else {
            winner = b; loser = a;
        }

        int damage = 0;
        if (draw) {
            // both take small skirmish damage
            damage = Math.max(3, Math.abs(sa - sb));
            a.reduceHealth(damage);
            b.reduceHealth(damage);
        } else {
            int wscore = computeScore(winner);
            int lscore = computeScore(loser);

            // base damage grows with score gap and winner capability
            int gap = Math.max(1, Math.abs(wscore - lscore));
            damage = Math.max(5, gap + wscore / 5);

            // apply damage to loser
            loser.reduceHealth(damage);
        }

        return new DuelResult(a, b, sa, sb, draw, winner, loser, damage);
    }

    public static List<AICreature> rank(List<AICreature> list) {
        List<AICreature> copy = new ArrayList<>(list);
        copy.sort(Comparator.comparingInt(CombatSystem::computeScore).reversed());
        return copy;
    }

    public static class DuelResult {
        public final AICreature a;
        public final AICreature b;
        public final int scoreA;
        public final int scoreB;
        public final boolean draw;
        public final AICreature winner; // null if draw
        public final AICreature loser;  // null if draw
        public final int damageDealtToLoser;

        public DuelResult(AICreature a, AICreature b, int scoreA, int scoreB, boolean draw,
                          AICreature winner, AICreature loser, int damage) {
            this.a = a; this.b = b; this.scoreA = scoreA; this.scoreB = scoreB;
            this.draw = draw; this.winner = winner; this.loser = loser; this.damageDealtToLoser = damage;
        }
    }
}
