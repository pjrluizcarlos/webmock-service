package br.com.pjrluizcarlos.webmockservice.data.mock;

import br.com.pjrluizcarlos.webmockservice.business.mock.Mock;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MockRepository {

    private final MockDAO dao;
    private final MockRepositoryMapper mapper;

    public List<Mock> findAll() {
        return dao.findAll().stream().map(mapper::map).toList();
    }

    public Optional<Mock> findByUriAndMethod(@NonNull String uri, @NonNull String method) {
        return dao.findByUriAndMethod(uri, method).map(mapper::map);
    }

    public boolean existsByUriAndMethod(@NonNull String uri, @NonNull String method) {
        return dao.existsByUriAndMethod(uri, method);
    }

    public Mock save(@NonNull Mock model) {
        MockEntity entityToSave = dao.findByUriAndMethod(model.getUri(), model.getMethod())
                .map(entity -> mapper.map(entity, model))
                .orElse(mapper.map(model));

        MockEntity entitySaved = dao.save(entityToSave);

        return mapper.map(entitySaved);
    }

    public void deleteAllByUriAndMethod(@NonNull String uri, @NonNull String method) {
        dao.deleteAllByUriAndMethod(uri, method);
    }

    public void deleteById(@NonNull Long id) {
        dao.deleteById(id);
    }

}
