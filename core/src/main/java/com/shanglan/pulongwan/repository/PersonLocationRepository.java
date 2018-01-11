package com.shanglan.pulongwan.repository;

import com.shanglan.pulongwan.entity.PersonLocation;
import com.shanglan.pulongwan.entity.SafeMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by cuishiying on 2017/5/10.
 */
public interface PersonLocationRepository extends JpaRepository<PersonLocation,Integer>,JpaSpecificationExecutor<PersonLocation> {


}
