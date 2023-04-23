package openassemblee.web.rest.dto;

import java.util.stream.DoubleStream;

public class ChairRendu {

    public int number;
    public double baseX1, baseY1, baseX2, baseY2;
    public double x1, y1, x2, y2, x3, y3, x4, y4;
    // attention à son utilisation, bien pour positionner les sièges, pas forcément pr autre chose
    public double centerAngle;
    public double chairBaseX, chairBaseY;
    public double centerX, centerY;

    public ChairRendu(
        int number,
        double baseX1,
        double baseY1,
        double baseX2,
        double baseY2,
        double x1,
        double y1,
        double x2,
        double y2,
        double x3,
        double y3,
        double x4,
        double y4,
        double centerAngle,
        double chairBaseX,
        double chairBaseY,
        double centerX,
        double centerY
    ) {
        this.number = number;
        this.baseX1 = baseX1;
        this.baseY1 = baseY1;
        this.baseX2 = baseX2;
        this.baseY2 = baseY2;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
        this.centerAngle = centerAngle;
        this.chairBaseX = chairBaseX;
        this.chairBaseY = chairBaseY;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int minX() {
        return (int) DoubleStream.of(x()).min().getAsDouble();
    }

    public int maxX() {
        return (int) DoubleStream.of(x()).max().getAsDouble();
    }

    public int minY() {
        return (int) DoubleStream.of(y()).min().getAsDouble();
    }

    public int maxY() {
        return (int) DoubleStream.of(y()).max().getAsDouble();
    }

    private double[] x() {
        return new double[] { x1, x2, x3, x4 };
    }

    private double[] y() {
        return new double[] { y1, y2, y3, y4 };
    }
}
