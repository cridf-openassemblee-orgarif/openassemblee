package openassemblee.web.rest;

import com.codahale.metrics.annotation.Timed;
import openassemblee.service.SearchService;
import openassemblee.service.util.DichotomyUtil;
import openassemblee.web.rest.dto.AssembleeChairDTO;
import openassemblee.web.rest.dto.AssembleeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@RestController
@RequestMapping("/search")
public class SearchResource {

    class ChairBaseLine {
        public double startAngle;
        public double endAngle;

        public ChairBaseLine(double startAngle, double endAngle) {
            this.startAngle = startAngle;
            this.endAngle = endAngle;
        }
    }

    @Autowired
    private SearchService searchService;

    private int baseX = 500;
    private int baseY = 600;

    private int i = 0;

    // FIXME .0 needed ?
//    private double largeur = 24.0;
//    private double prof = 14.0;
    private double largeur = 30.0;
    private double prof = 20.0;
    private double ratio = 1.5;

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
            this.lineOrientation = lineOrientation;
            this.ry = ry;
            this.startAngle = startAngle;
            this.firstIsCentralChair = firstIsCentralChair;
            this.chairsNumber = chairsNumber;
        }
    }

    @RequestMapping(value = "/{searchToken}", method = RequestMethod.GET)
    @Timed
//    public ResponseEntity<List<SearchResultDTO>> search(@PathVariable String searchToken) {
//        return ResponseEntity.ok(searchService.search(searchToken));
    public AssembleeDTO search(@PathVariable String searchToken) {
        // TODO les re-order
        List<LineDefinition> lines = new ArrayList<>();
        List<LineDefinition> centerLines = Arrays.asList(
            new LineDefinition(LineOrientation.CENTER, 160.0, 0.0, true, 5),
            new LineDefinition(LineOrientation.CENTER, 205.0, 0.0, true, 7),
            new LineDefinition(LineOrientation.CENTER, 250.0, 0.0, true, 9),
            new LineDefinition(LineOrientation.CENTER, 295.0, 0.0, true, 11),
            new LineDefinition(LineOrientation.CENTER, 340.0, 0.0, true, 13),
            new LineDefinition(LineOrientation.CENTER, 385.0, 0.0, true, 15),
            new LineDefinition(LineOrientation.CENTER, 430.0, 0.0, false, 16),
            new LineDefinition(LineOrientation.CENTER, 475.0, 0.0, false, 18),
            new LineDefinition(LineOrientation.CENTER, 520.0, 0.0, false, 20)
        );
        lines.addAll(centerLines);
        List<LineDefinition> rightLines = Arrays.asList(
            new LineDefinition(LineOrientation.RIGHT, 160, 26.2, false, 5),
            new LineDefinition(LineOrientation.RIGHT, 205, 25.0, false, 8),
            new LineDefinition(LineOrientation.RIGHT, 250, 24.5, false, 9),
            new LineDefinition(LineOrientation.RIGHT, 295, 24.0, false, 8),
            new LineDefinition(LineOrientation.RIGHT, 340, 23.5, false, 8),
            new LineDefinition(LineOrientation.RIGHT, 385, 23.2, false, 7),
            new LineDefinition(LineOrientation.RIGHT, 430, 23.0, false, 7),
            new LineDefinition(LineOrientation.RIGHT, 475, 22.8, false, 6),
            new LineDefinition(LineOrientation.RIGHT, 520, 22.6, false, 6)
        );
        lines.addAll(rightLines);
        List<LineDefinition> leftLines = rightLines.stream().map(l ->
            new LineDefinition(LineOrientation.LEFT, l.ry, l.startAngle, false, l.chairsNumber)
        ).collect(Collectors.toList());
        lines.addAll(leftLines);
        return new AssembleeDTO(lines.stream()
            .map(this::calculateChairs)
            .flatMap(c -> {
                return c.stream();
            })
            .collect(Collectors.toList()));
    }

    private List<AssembleeChairDTO> calculateChairs(LineDefinition line) {
        List<ChairBaseLine> baseLines = new ArrayList<>();

        double ry = line.ry;
        double rx = ry * ratio;

        ChairBaseLine baseLine = null;
        switch (line.lineOrientation) {
            case CENTER:
                if (line.startAngle != 0.0) {
                    throw new IllegalArgumentException();
                }
                if (line.firstIsCentralChair) {
                    baseLine = firstCentralBaseLine(rx);
                    baseLines.add(baseLine);
                    baseLine = new ChairBaseLine(baseLine.endAngle, secondAngle(baseLine.endAngle, rx, ry));
                } else {
                    double angle = firstAngleFromZero(rx, ry);
                    baseLine = new ChairBaseLine(-angle, 0);
                    baseLines.add(baseLine);
                    baseLine = new ChairBaseLine(0, angle);
                }
                break;
            case LEFT:
            case RIGHT:
                if (line.firstIsCentralChair) {
                    throw new IllegalArgumentException();
                }
                double angle = line.startAngle / 360 * 2 * PI;
                baseLine = new ChairBaseLine(angle, secondAngle(angle, rx, ry));
                break;
        }
        baseLines.add(baseLine);

        int iteration = line.lineOrientation == LineOrientation.CENTER ? line.chairsNumber / 2 : line.chairsNumber;
        for (int i = 1; i < iteration; i++) {
            baseLine = new ChairBaseLine(baseLine.endAngle, nextAngle(baseLine.endAngle, baseLine.startAngle, rx, ry));
            baseLines.add(baseLine);
        }
        if (line.lineOrientation == LineOrientation.LEFT) {
            baseLines = baseLines.stream().map(l -> new ChairBaseLine(-l.startAngle, -l.endAngle))
                .collect(Collectors.toList());
        }
        if (line.lineOrientation == LineOrientation.CENTER) {
            for (int i = line.firstIsCentralChair ? 1 : 0; i <= iteration; i++) {
                ChairBaseLine mirror = baseLines.get(i);
                baseLines.add(new ChairBaseLine(-mirror.startAngle, -mirror.endAngle));
            }
        }

        return baseLines.stream()
            .map(l -> calcChair(l, rx, ry))
            .map(this::positionChair)
            .collect(Collectors.toList());
    }

    private ChairBaseLine firstCentralBaseLine(double rx) {
        double angle = asin(demieLargeur / rx);
        return new ChairBaseLine(-angle, angle);
    }

    private double firstAngleFromZero(double rx, double ry) {
        // bon point de départ pour dichotomy
        double startAngle = atan(largeur / ry);
        return DichotomyUtil.dichotomiseMe(startAngle, startAngle / 2,
            angle -> resolveEquation(0, ry, angle, rx, ry), expectedPrecision, 0);
    }

    private double secondAngle(double firstAngle, double rx, double ry) {
        // bon point de départ pour la dicho
        double maxNextAngleDiff = firstAngle * 2;
        double x1 = rx * sin(firstAngle);
        double y1 = ry * cos(firstAngle);
        return DichotomyUtil.dichotomiseMe(firstAngle, maxNextAngleDiff,
            angle -> resolveEquation(x1, y1, angle, rx, ry), expectedPrecision, 0);
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

    private AssembleeChairDTO calcChair(ChairBaseLine baseLine, double rx, double ry) {
        i++;

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
        return new AssembleeChairDTO(0,
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
            x1 + (x2 - x1) / 2,
            y1 + (y2 - y1) / 2);
    }

    private AssembleeChairDTO positionChair(AssembleeChairDTO d) {
        double x = baseX;
        double y = baseY;
        return new AssembleeChairDTO(0,
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
            y - d.chairBaseY);
    }

}
