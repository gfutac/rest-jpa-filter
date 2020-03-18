package com.gfutac.restfilter.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class GenericSpecificationBuilder<T> {

    private List<SearchOperation> criteria;

    public GenericSpecificationBuilder() {
        this.criteria = new ArrayList<>();
    }

    public GenericSpecificationBuilder<T> with(String key, String operation, Object value) {
        this.criteria.add(new SearchOperation(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (this.criteria.size() == 0) return null;

        var specifications = this.criteria.stream()
                .map((Function<SearchOperation, Specification<T>>)GenericSpecification::new)
                .collect(Collectors.toList());

        var result = specifications.get(0);
        result = specifications.stream()
                .skip(1)
                .reduce(result, (tmp, element) -> Specification.where(tmp).and(element));


        return result;
    }
}
