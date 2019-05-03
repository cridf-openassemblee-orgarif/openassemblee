package openassemblee.service.util;

import openassemblee.domain.Elu;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

public class EluNomComparator {
    public static <T extends Object> Comparator<T> comparing(
        Function<T, Elu> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
            (e1, e2) -> StringUtils.stripAccents(keyExtractor.apply(e1).getNom())
                .compareTo(StringUtils.stripAccents(keyExtractor.apply(e2).getNom()));
    }
}
