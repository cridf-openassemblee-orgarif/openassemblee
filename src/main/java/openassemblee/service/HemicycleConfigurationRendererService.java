package openassemblee.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import openassemblee.web.rest.dto.ChairRendu;
import openassemblee.web.rest.dto.HemicycleConfigurationDefinition;
import openassemblee.web.rest.dto.HemicycleConfigurationDefinition.RowDefinition;
import openassemblee.web.rest.dto.HemicycleConfigurationDefinition.RowOrientation;
import openassemblee.web.rest.dto.HemicycleConfigurationRendu;
import org.springframework.stereotype.Service;

@Service
public class HemicycleConfigurationRendererService {

    double spaceBetweenChairs = 0.5;

    public HemicycleConfigurationRendu hemicycle(
        HemicycleConfigurationDefinition hd
    ) {
        List<ChairRendu> chairs = hd.rows
            .stream()
            .collect(Collectors.groupingBy(row -> row.line))
            .values()
            .stream()
            .map(l -> calculateChairs(l, hd))
            .flatMap(Collection::stream)
            .map(chair -> translateCenter(chair, hd))
            .collect(Collectors.toList());
        List<ChairRendu> numberedChairs = IntStream
            .range(0, chairs.size())
            .mapToObj(i -> {
                ChairRendu c = chairs.get(i);
                c.setNumber(i + hd.frontChairs + 1);
                return c;
            })
            .collect(Collectors.toList());
        int minX = numberedChairs
            .stream()
            .map(c -> c.minX())
            .sorted()
            .findFirst()
            .get();
        int maxX = -numberedChairs
            .stream()
            .map(c -> -c.maxX())
            .sorted()
            .findFirst()
            .get();
        int minY = numberedChairs
            .stream()
            .map(c -> c.minY())
            .sorted()
            .findFirst()
            .get();
        int frontChairsShift = (int) (1.5 * hd.prof);
        int maxY =
            -numberedChairs
                .stream()
                .map(c -> -c.maxY())
                .sorted()
                .findFirst()
                .get() +
            frontChairsShift;

        numberedChairs.addAll(
            0,
            getFrontChairs(hd, hd.lineBase - frontChairsShift + hd.lineHeight)
                .stream()
                .map(chair -> translateCenter(chair, hd))
                .collect(Collectors.toList())
        );

        List<Integer> numbers = numberedChairs
            .stream()
            .map(c -> c.number)
            .sorted()
            .collect(Collectors.toList());
        return new HemicycleConfigurationRendu(
            numberedChairs,
            minX - hd.margin,
            minY - hd.margin,
            (maxX - minX + 2 * hd.margin),
            (maxY - minY + 2 * hd.margin),
            numbers.get(0),
            numbers.get(numbers.size() - 1)
        );
    }

    private List<ChairRendu> calculateChairs(
        List<RowDefinition> rows,
        HemicycleConfigurationDefinition hd
    ) {
        if (
            rows.size() != 3 ||
            rows
                .stream()
                .filter(row -> row.rowOrientation == RowOrientation.LEFT)
                .count() !=
            1 ||
            rows
                .stream()
                .filter(row -> row.rowOrientation == RowOrientation.CENTER)
                .count() !=
            1 ||
            rows
                .stream()
                .filter(row -> row.rowOrientation == RowOrientation.RIGHT)
                .count() !=
            1
        ) {
            throw new IllegalArgumentException("Hemicycle definition is KO");
        }
        RowDefinition left = rows
            .stream()
            .filter(row -> row.rowOrientation == RowOrientation.LEFT)
            .findFirst()
            .get();
        RowDefinition center = rows
            .stream()
            .filter(row -> row.rowOrientation == RowOrientation.CENTER)
            .findFirst()
            .get();
        RowDefinition right = rows
            .stream()
            .filter(row -> row.rowOrientation == RowOrientation.RIGHT)
            .findFirst()
            .get();
        int totalChairSpace =
            rows.stream().mapToInt(row -> row.chairsNumber).sum() +
            (int) (2 * spaceBetweenChairs);
        double lineBaseX = -totalChairSpace * hd.largeur / 2;
        double lineBaseY = hd.lineBase + hd.lineHeight * rows.get(0).line;
        List<ChairRendu> chairs = new ArrayList<>();
        for (int i = 0; i < left.chairsNumber; i++) {
            chairs.add(
                calcChair(lineBaseX, lineBaseY, i, 0, hd.largeur, hd.prof, -1)
            );
        }
        for (int i = 0; i < center.chairsNumber; i++) {
            chairs.add(
                calcChair(
                    lineBaseX,
                    lineBaseY,
                    left.chairsNumber + i,
                    1,
                    hd.largeur,
                    hd.prof,
                    -1
                )
            );
        }
        for (int i = 0; i < right.chairsNumber; i++) {
            chairs.add(
                calcChair(
                    lineBaseX,
                    lineBaseY,
                    left.chairsNumber + center.chairsNumber + i,
                    2,
                    hd.largeur,
                    hd.prof,
                    -1
                )
            );
        }
        return chairs;
    }

    private ChairRendu calcChair(
        double lineBaseX,
        double lineBaseY,
        int chairPosition,
        int offset,
        double largeur,
        double prof,
        int chairNumber
    ) {
        double baseX =
            lineBaseX +
            chairPosition *
            largeur +
            offset *
            largeur *
            spaceBetweenChairs;
        double baseX2 = baseX + largeur;
        double baseY = lineBaseY - prof / 2;
        double baseY2 = baseY + prof;
        return new ChairRendu(
            chairNumber,
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
            (baseY + baseY2) / 2
        );
    }

    private ChairRendu translateCenter(
        ChairRendu d,
        HemicycleConfigurationDefinition hd
    ) {
        double x = hd.baseX;
        double y = hd.baseY;
        return new ChairRendu(
            d.number,
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
            y - d.centerY
        );
    }

    private List<ChairRendu> getFrontChairs(
        HemicycleConfigurationDefinition hd,
        int baseY
    ) {
        double lineBaseX = -hd.frontChairs * hd.largeur / 2;
        List<ChairRendu> chairs = new ArrayList<>();
        int halfNumber = (hd.frontChairs - 1) / 2; // is 2 for 6 chairs, 3 for 7 chairs
        for (int i = 0; i < hd.frontChairs; i++) {
            // half number = 3 avec frontChairs = 7
            // donc la chaise i=3 prend le numéro 1
            // les chaise 0 1 2 prennent 2 3 4
            // les chaises 4 5 6 prennent 5 6 7
            int chairNumber = i == halfNumber
                ? 1
                : i < halfNumber ? i + 2 : i + 1;
            chairs.add(
                calcChair(
                    lineBaseX,
                    baseY,
                    i,
                    0,
                    hd.largeur,
                    hd.prof,
                    chairNumber
                )
            );
        }
        return chairs;
    }
}
