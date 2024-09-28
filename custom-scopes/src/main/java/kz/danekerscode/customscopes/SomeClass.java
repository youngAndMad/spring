package kz.danekerscode.customscopes;

import lombok.Getter;

@Getter
public class SomeClass {
    private final Long createdTime = System.currentTimeMillis();
}