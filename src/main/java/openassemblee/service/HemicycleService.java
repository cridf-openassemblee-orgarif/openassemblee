package openassemblee.service;

import openassemblee.web.rest.dto.HemicycleChairDTO;
import openassemblee.web.rest.dto.HemicycleDTO;
import openassemblee.web.rest.dto.HemicycleDefinition;
import openassemblee.web.rest.dto.HemicycleDefinition.RowDefinition;
import openassemblee.web.rest.dto.HemicycleDefinition.RowOrientation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HemicycleService {

    double spaceBetweenChairs = 0.5;

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
        List<HemicycleChairDTO> chairs = hd.rows.stream()
            .collect(Collectors.groupingBy(row -> row.line))
            .values()
            .stream()
            .map(l -> calculateChairs(l, hd))
            .flatMap(Collection::stream)
            .map(chair -> translateCenter(chair, hd))
            .collect(Collectors.toList());
        List<HemicycleChairDTO> numberedChairs = IntStream.range(0, chairs.size())
            .mapToObj(i -> {
                HemicycleChairDTO c = chairs.get(i);
                c.setNumber(i + hd.frontChairs + 1);
                return c;
            })
            .collect(Collectors.toList());
        int minX = numberedChairs.stream().map(c -> c.minX()).sorted().findFirst().get();
        int maxX = -numberedChairs.stream().map(c -> -c.maxX()).sorted().findFirst().get();
        int minY = numberedChairs.stream().map(c -> c.minY()).sorted().findFirst().get();
        int frontChairsShift = (int) (1.5 * hd.prof);
        int maxY = -numberedChairs.stream().map(c -> -c.maxY()).sorted().findFirst().get() + frontChairsShift;

        numberedChairs.addAll(0, getFrontChairs(hd, hd.lineBase - frontChairsShift + hd.lineHeight)
            .stream().map(chair -> translateCenter(chair, hd)).collect(Collectors.toList()));

        List<Integer> numbers = numberedChairs.stream().map(c -> c.number).sorted().collect(Collectors.toList());
        return new HemicycleDTO(numberedChairs,
            minX - hd.margin, minY - hd.margin,
            (maxX - minX + 2 * hd.margin), (maxY - minY + 2 * hd.margin), numbers.get(0),
            numbers.get(numbers.size() - 1));
    }

    private List<HemicycleChairDTO> calculateChairs(List<RowDefinition> rows, HemicycleDefinition hd) {
        if (rows.size() != 3 ||
            rows.stream().filter(row -> row.rowOrientation == RowOrientation.LEFT).count() != 1 ||
            rows.stream().filter(row -> row.rowOrientation == RowOrientation.CENTER).count() != 1 ||
            rows.stream().filter(row -> row.rowOrientation == RowOrientation.RIGHT).count() != 1) {
            throw new IllegalArgumentException("Hemicycle definition is KO");
        }
        RowDefinition left = rows.stream().filter(row -> row.rowOrientation == RowOrientation.LEFT).findFirst().get();
        RowDefinition center = rows.stream().filter(row -> row.rowOrientation == RowOrientation.CENTER).findFirst().get();
        RowDefinition right = rows.stream().filter(row -> row.rowOrientation == RowOrientation.RIGHT).findFirst().get();
        int totalChairSpace = rows.stream().mapToInt(row -> row.chairsNumber).sum() + (int) (2 * spaceBetweenChairs);
        double lineBaseX = -totalChairSpace * hd.largeur / 2;
        double lineBaseY = hd.lineBase + hd.lineHeight * rows.get(0).line;
        List<HemicycleChairDTO> chairs = new ArrayList<>();
        for (int i = 0; i < left.chairsNumber; i++) {
            chairs.add(calcChair(lineBaseX, lineBaseY, i, 0, hd.largeur, hd.prof, -1));
        }
        for (int i = 0; i < center.chairsNumber; i++) {
            chairs.add(calcChair(lineBaseX, lineBaseY, left.chairsNumber + i, 1, hd.largeur, hd.prof, -1));
        }
        for (int i = 0; i < right.chairsNumber; i++) {
            chairs.add(calcChair(lineBaseX, lineBaseY, left.chairsNumber + center.chairsNumber + i, 2, hd.largeur, hd.prof, -1));
        }
        return chairs;
    }

    private HemicycleChairDTO calcChair(double lineBaseX, double lineBaseY, int chairPosition, int offset, double largeur,
                                        double prof, int chairNumber) {
        double baseX = lineBaseX + chairPosition * largeur + offset * largeur * spaceBetweenChairs;
        double baseX2 = baseX + largeur;
        double baseY = lineBaseY - prof / 2;
        double baseY2 = baseY + prof;
        return new HemicycleChairDTO(chairNumber,
            baseX,
            baseY,
            baseX2,
            baseY,
            baseX,
            baseY2,
            baseX2,
            baseY2,
            baseX2,
            baseY,
            baseX,
            baseY,
            0,
            (baseX + baseX2) / 2,
            baseY2,
            (baseX + baseX2) / 2,
            (baseY + baseY2) / 2);
    }

    private HemicycleChairDTO translateCenter(HemicycleChairDTO d, HemicycleDefinition hd) {
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

    private List<HemicycleChairDTO> getFrontChairs(HemicycleDefinition hd, int baseY) {
        double lineBaseX = -hd.frontChairs * hd.largeur / 2;
        List<HemicycleChairDTO> chairs = new ArrayList<>();
        for (int i = 0; i < hd.frontChairs; i++) {
            chairs.add(calcChair(lineBaseX, baseY, i, 0, hd.largeur, hd.prof, i));
        }
        return chairs;
    }

//    private HemicycleChairDTO frontChair(HemicycleDefinition hd, int maxY, int chairNumber, int chairPosition,
//                                         double baseShift) {
//        double x0 = hd.baseX + baseShift + (hd.largeur * chairPosition);
//        double x1 = x0 + hd.largeur;
//        double y0 = maxY - hd.prof;
//        double y1 = maxY;
//        double centerX = (x0 + x1) / 2;
//        double centerY = (y0 + y1) / 2;
//        return new HemicycleChairDTO(chairNumber,
//            x0,
//            centerY,
//            x1,
//            centerY,
//            x0, y0, x1, y0, x1, y1, x0, y1,
//            0,
//            centerX,
//            y0,
//            centerX,
//            centerY);
//    }

}
