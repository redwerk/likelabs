package com.redwerk.likelabs.domain.model.settings;

import java.util.List;

public interface SystemParameterRepository {

    SystemParameter find(SystemParameterType type);

    List<SystemParameter> findAll();

    void add(SystemParameter parameter);

    void remove(SystemParameter parameter);

}
