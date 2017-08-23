package com.shanglan.pulongwan.repository;

import com.shanglan.pulongwan.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by cuishiying on 2017/5/10.
 */
public interface ManageRepository extends JpaRepository<Topic,Integer>,JpaSpecificationExecutor<Topic> {

    Topic findById(Integer id);

}
