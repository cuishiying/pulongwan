package com.shanglan.pulongwan.repository;

import com.shanglan.pulongwan.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by cuishiying on 2017/7/26.
 */
public interface FieldRepository extends JpaRepository<Field,Integer>,JpaSpecificationExecutor<Field> {

    Field findByTelemetrySignal(String data);


    @Query("select f from Field f where f.describer like ?1")
    List<Field> findAll(String keyword);

    @Query("select f from Field f where f.topTopic like ?1")
    List<Field> findAll(Integer id);

}
