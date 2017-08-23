package com.shanglan.pulongwan.repository;

import com.shanglan.pulongwan.entity.Topic;
import com.shanglan.pulongwan.entity.TopicDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

/**
 * Created by cuishiying on 2017/5/10.
 */
public interface DataRepository extends JpaRepository<TopicDetail,Integer>,JpaSpecificationExecutor<TopicDetail> {

    Topic findById(Integer id);

    //删除指定日期前数据
    @Modifying
    @Query("DELETE FROM TopicDetail WHERE delTime < ?1")
    int delOldData(LocalDateTime delTime);

}
