package dev.sorokin.eventmanager.utils;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecificationUtils {

    public static <E> Specification<E> equal(Object value, String paramName) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || (value instanceof String str && str.isBlank())) {
                return null;
            }
            return criteriaBuilder.equal(resolvePath(root, paramName), value);
        };
    }

    public static <E, T extends Comparable<T>> Specification<E> between(T min, T max, String paramName) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(resolvePath(root, paramName), max);
            }
            if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(resolvePath(root, paramName), min);
            }
            return criteriaBuilder.between(resolvePath(root, paramName), min, max);
        };

    }

    private static <E, T> Path<T> resolvePath(Root<E> root, String pathToField) {
        String[] pathParts = pathToField.split("\\.");
        Path<Object> path = null;
        for (String part : pathParts) {
            path = path == null ? root.get(part) : path.get(part);
        }
        return (Path<T>) path;
    }

}
