package openassemblee.web.rest.dto;

import java.util.List;

public class HemicycleDefinition {

    public enum LineOrientation {
        LEFT, CENTER, RIGHT
    }

    public static class LineDefinition {
        public LineOrientation lineOrientation;
        public double ry;
        public double startAngle;
        public boolean firstIsCentralChair;
        public int chairsNumber;

        public LineDefinition() {
        }

        public LineDefinition(LineOrientation lineOrientation, double ry, double startAngle, boolean firstIsCentralChair, int chairsNumber) {
            if (lineOrientation == LineOrientation.CENTER && startAngle != 0.0) {
                throw new IllegalArgumentException();
            }
            if (lineOrientation != LineOrientation.CENTER && firstIsCentralChair) {
                throw new IllegalArgumentException();
            }
            this.lineOrientation = lineOrientation;
            this.ry = ry;
            this.startAngle = startAngle;
            this.firstIsCentralChair = firstIsCentralChair;
            this.chairsNumber = chairsNumber;
        }
    }

    public int baseX;
    public int baseY;

    public double largeur;
    public double prof;

    public double ratioHemicycle;

    //    private double demieLargeur = largeur / 2;
//    private double demieProf = prof / 2;
    // revient un peu à une précision graphique en pixel
    // TODO tester d'autres values ;)
    public double expectedPrecision;
    public List<LineDefinition> lines;

    public HemicycleDefinition() {
    }

    public HemicycleDefinition(int baseX, int baseY, double largeur, double prof, double ratioHemicycle, double expectedPrecision, List<LineDefinition> lines) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.largeur = largeur;
        this.prof = prof;
        this.ratioHemicycle = ratioHemicycle;
        this.expectedPrecision = expectedPrecision;
        this.lines = lines;
    }
}
