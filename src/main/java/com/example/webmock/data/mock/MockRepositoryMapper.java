package com.example.webmock.data.mock;

import com.example.webmock.business.mock.Mock;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MockRepositoryMapper {

    public Mock map(@NonNull MockEntity entity) {
        return Mock.builder()
                .id(entity.getId())
                .uri(entity.getUri())
                .method(entity.getMethod())
                .response(entity.getResponse())
                .status(entity.getStatus())
                .build();
    }

    public MockEntity map(@NonNull Mock model) {
        return MockEntity.builder()
                .id(model.getId())
                .uri(model.getUri())
                .method(model.getMethod())
                .response(model.getResponse())
                .status(model.getStatus())
                .build();
    }

}
