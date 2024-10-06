package kz.danekerscode.debezium.utils;

import java.util.*;

public class FieldListHolder implements Iterable<String> {
    public static final String SPLIT_BY_COMMA = "\\s*,\\s*";
    private final Set<String> fields;

    public FieldListHolder(String singleRepresentation) {
        this(Arrays.asList(Objects
                .requireNonNull(singleRepresentation, "Single representation of field path list cannot be null")
                .split(SPLIT_BY_COMMA))
        );
    }

    public FieldListHolder(Collection<String> fields) {
        fields.forEach(FieldNameHelper::validateFieldPath);
        this.fields = new HashSet<>(fields);
    }

    public boolean exists(String fieldPath) {
        return fields.contains(fieldPath);
    }


    @Override
    public Iterator<String> iterator() {
        return fields.iterator();
    }

    @Override
    public String toString() {
        return "Fields = " + fields;
    }
}