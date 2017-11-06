package com.shanglan.pulongwan.repository;

import com.shanglan.pulongwan.entity.RockPressure;
import com.shanglan.pulongwan.entity.Topic;
import com.shanglan.pulongwan.entity.TopicDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by cuishiying on 2017/5/10.
 */
public interface RockPressureRepository extends JpaRepository<RockPressure,Integer>,JpaSpecificationExecutor<RockPressure> {


}
