package openassemblee.web.rest.dto;

import java.util.List;

public class HemicycleDefinition {

    //    private double largeur = 24.0;
    //    private double prof = 14.0;
    //    private double largeur = 30.0;
    //    private double prof = 20.0;

    public enum RowOrientation {
        LEFT, CENTER, RIGHT
    }

    public static class RowDefinition {
        public RowOrientation rowOrientation;
        public int line;
        public double startAngle;
        public int chairsNumber;

        public RowDefinition() {
        }

        public RowDefinition(RowOrientation rowOrientation, int line, double startAngle, boolean firstIsCentralChair, int chairsNumber) {
            if (rowOrientation == RowOrientation.CENTER && startAngle != 0.0) {
                throw new IllegalArgumentException();
            }
            if (rowOrientation != RowOrientation.CENTER && firstIsCentralChair) {
                throw new IllegalArgumentException();
            }
            this.rowOrientation = rowOrientation;
            this.line = line;
            this.startAngle = startAngle;
            this.chairsNumber = chairsNumber;
        }
    }

    public int baseX;
    public int baseY;

    public double largeur;
    public double prof;
    public int lineBase;
    public int lineHeight;

    public double ratioHemicycle;

    //    private double demieLargeur = largeur / 2;
//    private double demieProf = prof / 2;
    // revient un peu à une précision graphique en pixel
    // TODO tester d'autres values ;)
    public double expectedPrecision;
    public List<RowDefinition> rows;
    public int margin;
    public int frontChairs;

    public HemicycleDefinition() {
    }

    public HemicycleDefinition(int baseX, int baseY, double largeur, double prof, int lineBase, int lineHeight,
                               double ratioHemicycle, double expectedPrecision, List<RowDefinition> rows, int margin,
                               int frontChairs) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.largeur = largeur;
        this.prof = prof;
        this.lineBase = lineBase;
        this.lineHeight = lineHeight;
        this.ratioHemicycle = ratioHemicycle;
        this.expectedPrecision = expectedPrecision;
        this.rows = rows;
        this.margin = margin;
        this.frontChairs = frontChairs;
    }
}
