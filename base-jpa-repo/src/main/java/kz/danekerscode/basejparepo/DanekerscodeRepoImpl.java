package kz.danekerscode.basejparepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DanekerscodeRepoImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> {

    public DanekerscodeRepoImpl(JpaEntityInformation<T, ?>
                                          entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        TypedQuery<T> query = this.getQuery(spec, pageable);
        return (pageable.isUnpaged() ? new PageImpl<>(query.getResultList()) : this.readPageAsync(query, this.getDomainClass(), pageable, spec).join());
    }

    <S extends T> CompletableFuture<Page<S>> readPageAsync(
            TypedQuery<S> query,
            final Class<S> domainClass,
            Pageable pageable,
            @Nullable Specification<S> spec
    ) {
        if (pageable.isPaged()) {
            query.setFirstResult(PageableUtils.getOffsetAsInteger(pageable));
            query.setMaxResults(pageable.getPageSize());
        }

        var resultFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread = " + Thread.currentThread().getName());
            return query.getResultList();
        });
        var countFuture = executeCountQueryAsync(this.getCountQuery(spec, domainClass));

        return resultFuture.thenCombine(countFuture, (results, totalCount) ->
                PageableExecutionUtils.getPage(results, pageable, () -> totalCount)
        );
    }


    private static CompletableFuture<Long> executeCountQueryAsync(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null");

        return CompletableFuture.supplyAsync(() -> {
            List<Long> totals = query.getResultList();
            long total = 0L;
            for (Long element : totals) {
                total += (element == null ? 0L : element);
            }
            System.out.println("Total count: " + total + " thread = " + Thread.currentThread().getName());
            return total;
        });
    }

}