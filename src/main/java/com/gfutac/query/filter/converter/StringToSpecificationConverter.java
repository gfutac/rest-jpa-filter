package com.gfutac.query.filter.converter;

import com.gfutac.query.filter.specification.FilterSpecificationBuilder;
import com.gfutac.query.filter.specification.SpecificationBuildingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class StringToSpecificationConverter implements Converter<String, Specification<?>> {
    @Override
    public Specification<?> convert(String source) {
        try {
            var builder = new FilterSpecificationBuilder<>();
            return builder.build(source);
        } catch (SpecificationBuildingException e) {
            log.error("Could not convert string to FilterSpecification.", e);
            return null;
        }
    }
}
