package openassemblee.service;

import openassemblee.service.util.DichotomyUtil;
import openassemblee.web.rest.dto.HemicycleChairDTO;
import openassemblee.web.rest.dto.HemicycleDTO;
import openassemblee.web.rest.dto.HemicycleDefinition;
import openassemblee.web.rest.dto.HemicycleDefinition.LineDefinition;
import openassemblee.web.rest.dto.HemicycleDefinition.LineOrientation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.*;

@Service
public class HemicycleService {

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

    public HemicycleDTO hemicycle(HemicycleDefinition hd) {
        List<HemicycleChairDTO> chairs = hd.lines.stream()
            .map(l -> calculateChairs(l, hd))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        List<HemicycleChairDTO> numberedChairs = IntStream.range(0, chairs.size())
            .mapToObj(i -> {
                HemicycleChairDTO c = chairs.get(i);
                c.setNumber(i + hd.numerotationDebut);
                return c;
            })
            .collect(Collectors.toList());
        int minX = numberedChairs.stream().map(c -> c.minX()).sorted().findFirst().get();
        int maxX = -numberedChairs.stream().map(c -> -c.maxX()).sorted().findFirst().get();
        int minY = numberedChairs.stream().map(c -> c.minY()).sorted().findFirst().get();
        int maxY = -numberedChairs.stream().map(c -> -c.maxY()).sorted().findFirst().get();
        return new HemicycleDTO(numberedChairs, minX - hd.margin, minY - hd.margin,
            (maxX - minX + 2 * hd.margin), (maxY - minY + 2 * hd.margin));
    }

    private List<HemicycleChairDTO> calculateChairs(LineDefinition line, HemicycleDefinition hd) {
        double ry = line.ry;
        double rx = ry * hd.ratioHemicycle;
        return chairBaseLines(line, rx, ry, hd)
            .stream()
            .map(l -> calcChair(l, rx, ry, hd))
            .map(c -> positionChair(c, hd))
            .collect(Collectors.toList());
    }

    private List<ChairBaseLine> chairBaseLines(LineDefinition line, double rx, double ry, HemicycleDefinition hd) {
        switch (line.lineOrientation) {
            case CENTER:
                return centerBaseLines(line, rx, ry, hd);
            case LEFT:
            case RIGHT:
                return sideBaseLines(line, rx, ry, hd);
        }
        throw new RuntimeException();
    }

    private List<ChairBaseLine> centerBaseLines(LineDefinition line, double rx, double ry, HemicycleDefinition hd) {
        List<ChairBaseLine> baseLines = new ArrayList<>();
        if (!line.firstIsCentralChair) {
            baseLines.add(firstBaseLine(rx, ry, hd));
        } else {
            baseLines.add(firstCentralBaseLine(rx, hd));
            baseLines.add(new ChairBaseLine(last(baseLines).endAngle,
                secondAngle(last(baseLines).endAngle, rx, ry, hd)));
        }
        for (int i = 1; i < (line.chairsNumber - 1) / 2; i++) {
            baseLines.add(new ChairBaseLine(last(baseLines).endAngle,
                nextAngle(last(baseLines).endAngle, last(baseLines).startAngle, rx, ry, hd)));
        }
        Collections.reverse(baseLines);
        for (int i = line.firstIsCentralChair ? baseLines.size() - 2 : baseLines.size() - 1; i >= 0; i--) {
            ChairBaseLine l = baseLines.get(i);
            baseLines.add(new ChairBaseLine(-l.endAngle, -l.startAngle));
        }
        Collections.reverse(baseLines);
        return baseLines;
    }

    private ChairBaseLine firstBaseLine(double rx, double ry, HemicycleDefinition hd) {
        // bon point de départ pour dichotomy
        double startAngle = atan(hd.largeur / ry);
        double angle = DichotomyUtil.dichotomiseMe(startAngle, startAngle / 2,
            a -> resolveEquation(0, ry, a, rx, ry, hd), hd.expectedPrecision, 0);
        return new ChairBaseLine(0, angle);
    }

    private ChairBaseLine firstCentralBaseLine(double rx, HemicycleDefinition hd) {
        double angle = asin(hd.largeur / 2 / rx);
        return new ChairBaseLine(-angle, angle);
    }

    private double secondAngle(double firstAngle, double rx, double ry, HemicycleDefinition hd) {
        // bon point de départ pour la dicho
        double maxNextAngleDiff = firstAngle * 2;
        double x1 = rx * sin(firstAngle);
        double y1 = ry * cos(firstAngle);
        return DichotomyUtil.dichotomiseMe(firstAngle, maxNextAngleDiff,
            angle -> resolveEquation(x1, y1, angle, rx, ry, hd), hd.expectedPrecision, 0);
    }

    private List<ChairBaseLine> sideBaseLines(LineDefinition line, double rx, double ry, HemicycleDefinition hd) {
        List<ChairBaseLine> baseLines = new ArrayList<>();
        double angle = Math.abs(line.startAngle) / 360 * 2 * PI;
        baseLines.add(new ChairBaseLine(angle, secondAngle(angle, rx, ry, hd)));
        for (int i = 1; i < line.chairsNumber; i++) {
            baseLines.add(new ChairBaseLine(last(baseLines).endAngle,
                nextAngle(last(baseLines).endAngle, last(baseLines).startAngle, rx, ry, hd)));
        }
        if (line.lineOrientation == LineOrientation.LEFT) {
            baseLines.forEach(l -> l.inverseAngles());
            Collections.reverse(baseLines);
        }
        return baseLines;
    }

    private double nextAngle(double baseAngle, double previousAngle, double rx, double ry, HemicycleDefinition hd) {
        double x1 = rx * sin(baseAngle);
        double y1 = ry * cos(baseAngle);
        double diff = baseAngle - previousAngle;
        return DichotomyUtil.dichotomiseMe(baseAngle, diff,
            angle -> resolveEquation(x1, y1, angle, rx, ry, hd), hd.expectedPrecision, 0);
    }

    private double resolveEquation(double x1, double y1, double angle, double rx, double ry, HemicycleDefinition hd) {
        return pow(rx * sin(angle) - x1, 2) + pow(ry * cos(angle) - y1, 2) - pow(hd.largeur, 2);
    }

    private ChairBaseLine last(List<ChairBaseLine> lines) {
        return lines.get(lines.size() - 1);
    }

    private HemicycleChairDTO calcChair(ChairBaseLine baseLine, double rx, double ry, HemicycleDefinition hd) {
        double startSin = sin(baseLine.startAngle);
        double startCos = cos(baseLine.startAngle);
        double endSin = sin(baseLine.endAngle);
        double endCos = cos(baseLine.endAngle);

        double startX = rx * startSin;
        double startY = ry * startCos;
        double endX = rx * endSin;
        double endY = ry * endCos;

        double demieProf = hd.prof / 2;

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

    private HemicycleChairDTO positionChair(HemicycleChairDTO d, HemicycleDefinition hd) {
        double x = hd.baseX;
        double y = hd.baseY;
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
