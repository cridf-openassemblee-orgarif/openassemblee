package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.service.util.DichotomyUtil;
import openassemblee.web.rest.dto.HemicycleChairDTO;
import openassemblee.web.rest.dto.HemicycleDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.*;

@RestController
@RequestMapping("/api")
public class HemicycleResource {

    class ChairBaseLine {
        public double startAngle;
        public double endAngle;

        public ChairBaseLine(double startAngle, double endAngle) {
            this.startAngle = startAngle;
            this.endAngle = endAngle;
        }

        public void inverseAngles() {
            startAngle = -startAngle;
            endAngle = -endAngle;
        }
    }

    private int baseX = 500;
    private int baseY = 600;

//    private double largeur = 24.0;
//    private double prof = 14.0;
//    private double largeur = 30.0;
//    private double prof = 20.0;
    private double largeur = 26.0;
    private double prof = 16.0;

    //    private double prof = 30.0;
    private double ratioHemicycle = 1.5;

    private double demieLargeur = largeur / 2;
    double demieProf = prof / 2;

    // revient un peu à une précision graphique en pixel
    // TODO tester d'autres values ;)
    private double expectedPrecision = 1;

    enum LineOrientation {
        LEFT, CENTER, RIGHT
    }

    class LineDefinition {
        LineOrientation lineOrientation;
        double ry;
        double startAngle;
        boolean firstIsCentralChair;
        int chairsNumber;

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

    @RequestMapping(value = "/hemicycle",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    // FIXMENOW ne pas tout recalculer
    public HemicycleDTO hemicycle() {
        List<LineDefinition> lines = Arrays.asList(
            new LineDefinition(LineOrientation.LEFT, 160, -26.2, false, 5),
            new LineDefinition(LineOrientation.CENTER, 160.0, 0.0, true, 5),
            new LineDefinition(LineOrientation.RIGHT, 160, 26.2, false, 5),

            new LineDefinition(LineOrientation.LEFT, 205, -25.0, false, 8),
            new LineDefinition(LineOrientation.CENTER, 205.0, 0.0, true, 7),
            new LineDefinition(LineOrientation.RIGHT, 205, 25.0, false, 8),

            new LineDefinition(LineOrientation.LEFT, 250, -24.5, false, 11),
            new LineDefinition(LineOrientation.CENTER, 250.0, 0.0, true, 9),
            new LineDefinition(LineOrientation.RIGHT, 250, 24.5, false, 9),

            new LineDefinition(LineOrientation.LEFT, 295, -24.0, false, 10),
            new LineDefinition(LineOrientation.CENTER, 295.0, 0.0, true, 11),
            new LineDefinition(LineOrientation.RIGHT, 295, 24.0, false, 8),

            new LineDefinition(LineOrientation.LEFT, 340, -23.5, false, 9),
            new LineDefinition(LineOrientation.CENTER, 340.0, 0.0, true, 13),
            new LineDefinition(LineOrientation.RIGHT, 340, 23.5, false, 8),

            new LineDefinition(LineOrientation.LEFT, 385, -23.2, false, 8),
            new LineDefinition(LineOrientation.CENTER, 385.0, 0.0, true, 15),
            new LineDefinition(LineOrientation.RIGHT, 385, 23.2, false, 7),

            new LineDefinition(LineOrientation.LEFT, 430, -23.0, false, 8),
            new LineDefinition(LineOrientation.CENTER, 430.0, 0.0, false, 16),
            new LineDefinition(LineOrientation.RIGHT, 430, 23.0, false, 7),

            new LineDefinition(LineOrientation.LEFT, 475, -22.8, false, 7),
            new LineDefinition(LineOrientation.CENTER, 475.0, 0.0, false, 18),
            new LineDefinition(LineOrientation.RIGHT, 475, 22.8, false, 6),

            new LineDefinition(LineOrientation.LEFT, 520, -22.6, false, 6),
            new LineDefinition(LineOrientation.CENTER, 520.0, 0.0, false, 20),
            new LineDefinition(LineOrientation.RIGHT, 520, 22.6, false, 6)
        );

        List<HemicycleChairDTO> chairs = lines.stream()
            .map(this::calculateChairs)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        List<HemicycleChairDTO> numberedChairs = IntStream.range(0, chairs.size())
            .mapToObj(i -> {
                HemicycleChairDTO c = chairs.get(i);
                c.setNumber(i + 1);
                return c;
            })
            .collect(Collectors.toList());
        return new HemicycleDTO(numberedChairs);
    }

    private List<HemicycleChairDTO> calculateChairs(LineDefinition line) {
        double ry = line.ry;
        double rx = ry * ratioHemicycle;
        return chairBaseLines(line, rx, ry)
            .stream()
            .map(l -> calcChair(l, rx, ry))
            .map(this::positionChair)
            .collect(Collectors.toList());
    }

    private List<ChairBaseLine> chairBaseLines(LineDefinition line, double rx, double ry) {
        switch (line.lineOrientation) {
            case CENTER:
                return centerBaseLines(line, rx, ry);
            case LEFT:
            case RIGHT:
                return sideBaseLines(line, rx, ry);
        }
        throw new RuntimeException();
    }

    private List<ChairBaseLine> centerBaseLines(LineDefinition line, double rx, double ry) {
        List<ChairBaseLine> baseLines = new ArrayList<>();
        if (!line.firstIsCentralChair) {
            baseLines.add(firstBaseLine(rx, ry));
        } else {
            baseLines.add(firstCentralBaseLine(rx));
            baseLines.add(new ChairBaseLine(last(baseLines).endAngle, secondAngle(last(baseLines).endAngle, rx, ry)));
        }
        for (int i = 1; i < (line.chairsNumber - 1) / 2; i++) {
            baseLines.add(new ChairBaseLine(last(baseLines).endAngle, nextAngle(last(baseLines).endAngle, last(baseLines).startAngle, rx, ry)));
        }
        Collections.reverse(baseLines);
        for (int i = line.firstIsCentralChair ? baseLines.size() - 2 : baseLines.size() - 1; i >= 0; i--) {
            ChairBaseLine l = baseLines.get(i);
            baseLines.add(new ChairBaseLine(-l.endAngle, -l.startAngle));
        }
        Collections.reverse(baseLines);
        return baseLines;
    }

    private ChairBaseLine firstBaseLine(double rx, double ry) {
        // bon point de départ pour dichotomy
        double startAngle = atan(largeur / ry);
        double angle = DichotomyUtil.dichotomiseMe(startAngle, startAngle / 2,
            a -> resolveEquation(0, ry, a, rx, ry), expectedPrecision, 0);
        return new ChairBaseLine(0, angle);
    }

    private ChairBaseLine firstCentralBaseLine(double rx) {
        double angle = asin(demieLargeur / rx);
        return new ChairBaseLine(-angle, angle);
    }

    private double secondAngle(double firstAngle, double rx, double ry) {
        // bon point de départ pour la dicho
        double maxNextAngleDiff = firstAngle * 2;
        double x1 = rx * sin(firstAngle);
        double y1 = ry * cos(firstAngle);
        return DichotomyUtil.dichotomiseMe(firstAngle, maxNextAngleDiff,
            angle -> resolveEquation(x1, y1, angle, rx, ry), expectedPrecision, 0);
    }

    private List<ChairBaseLine> sideBaseLines(LineDefinition line, double rx, double ry) {
        List<ChairBaseLine> baseLines = new ArrayList<>();
        double angle = Math.abs(line.startAngle) / 360 * 2 * PI;
        baseLines.add(new ChairBaseLine(angle, secondAngle(angle, rx, ry)));
        for (int i = 1; i < line.chairsNumber; i++) {
            baseLines.add(new ChairBaseLine(last(baseLines).endAngle, nextAngle(last(baseLines).endAngle, last(baseLines).startAngle, rx, ry)));
        }
        if (line.lineOrientation == LineOrientation.LEFT) {
            baseLines.forEach(l -> l.inverseAngles());
            Collections.reverse(baseLines);
        }
        return baseLines;
    }

    private double nextAngle(double baseAngle, double previousAngle, double rx, double ry) {
        double x1 = rx * sin(baseAngle);
        double y1 = ry * cos(baseAngle);
        double diff = baseAngle - previousAngle;
        return DichotomyUtil.dichotomiseMe(baseAngle, diff,
            angle -> resolveEquation(x1, y1, angle, rx, ry), expectedPrecision, 0);
    }

    private double resolveEquation(double x1, double y1, double angle, double rx, double ry) {
        return pow(rx * sin(angle) - x1, 2) + pow(ry * cos(angle) - y1, 2) - pow(largeur, 2);
    }

    private ChairBaseLine last(List<ChairBaseLine> lines) {
        return lines.get(lines.size() - 1);
    }

    private HemicycleChairDTO calcChair(ChairBaseLine baseLine, double rx, double ry) {
        double startSin = sin(baseLine.startAngle);
        double startCos = cos(baseLine.startAngle);
        double endSin = sin(baseLine.endAngle);
        double endCos = cos(baseLine.endAngle);

        double startX = rx * startSin;
        double startY = ry * startCos;
        double endX = rx * endSin;
        double endY = ry * endCos;

        double x1 = startX + demieProf * startSin;
        double y1 = startY + demieProf * startCos;
        double x2 = endX + demieProf * endSin;
        double y2 = endY + demieProf * endCos;
        double x3 = endX - demieProf * endSin;
        double y3 = endY - demieProf * endCos;
        double x4 = startX - demieProf * startSin;
        double y4 = startY - demieProf * startCos;
        double centerAngle = (baseLine.startAngle + baseLine.endAngle) / 2;
        double chairBaseX = x1 + (x2 - x1) / 2;
        double chairBaseY = y1 + (y2 - y1) / 2;
        return new HemicycleChairDTO(-1,
            startX,
            startY,
            endX,
            endY,
            x1,
            y1,
            x2,
            y2,
            x3,
            y3,
            x4,
            y4,
            centerAngle,
            chairBaseX,
            chairBaseY,
            ((startX + endX) / 2 + chairBaseX) / 2,
            ((startY + endY) / 2 + chairBaseY) / 2);
    }

    private HemicycleChairDTO positionChair(HemicycleChairDTO d) {
        double x = baseX;
        double y = baseY;
        return new HemicycleChairDTO(d.number,
            x + d.baseX1,
            y - d.baseY1,
            x + d.baseX2,
            y - d.baseY2,
            x + d.x1,
            y - d.y1,
            x + d.x2,
            y - d.y2,
            x + d.x3,
            y - d.y3,
            x + d.x4,
            y - d.y4,
            d.centerAngle,
            x + d.chairBaseX,
            y - d.chairBaseY,
            x + d.centerX,
            y - d.centerY);
    }

}
