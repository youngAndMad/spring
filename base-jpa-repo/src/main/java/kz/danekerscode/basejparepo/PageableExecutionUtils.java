package kz.danekerscode.basejparepo;

import java.util.List;
import java.util.function.LongSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

public abstract class PageableExecutionUtils {
    private PageableExecutionUtils() {
    }

    public static <T> Page<T> getPage(List<T> content, Pageable pageable, LongSupplier totalSupplier) {
        Assert.notNull(content, "Content must not be null");
        Assert.notNull(pageable, "Pageable must not be null");
        Assert.notNull(totalSupplier, "TotalSupplier must not be null");
        if (pageable.isUnpaged()) {
            return new PageImpl(content, pageable, content.size());
        } else {
            if (isPartialPage(content, pageable)) {
                if (isFirstPage(pageable)) {
                    return new PageImpl(content, pageable, content.size());
                }

                if (!content.isEmpty()) {
                    return new PageImpl(content, pageable, pageable.getOffset() + (long)content.size());
                }
            }

            return new PageImpl(content, pageable, totalSupplier.getAsLong());
        }
    }

    private static <T> boolean isPartialPage(List<T> content, Pageable pageable) {
        return pageable.getPageSize() > content.size();
    }

    private static boolean isFirstPage(Pageable pageable) {
        return pageable.getOffset() == 0L;
    }
}
