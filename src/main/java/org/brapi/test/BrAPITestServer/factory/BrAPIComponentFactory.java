package org.brapi.test.BrAPITestServer.factory;

import io.swagger.model.core.BatchDeleteTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BrAPIComponentFactory {
    private final Map<BatchDeleteTypes, BrAPIComponent> componentMap;

    @Autowired
    public BrAPIComponentFactory(List<BrAPIComponent> components) {
        this.componentMap = components.stream()
                .collect(Collectors.toMap(BrAPIComponent::getBatchDeleteType, component -> component));
    }

    public BrAPIComponent getComponent(BatchDeleteTypes type) {
        return componentMap.get(type);
    }
}