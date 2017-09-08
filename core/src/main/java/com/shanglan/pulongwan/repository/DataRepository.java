package com.shanglan.pulongwan.repository;

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
public interface DataRepository extends JpaRepository<TopicDetail,Integer>,JpaSpecificationExecutor<TopicDetail> {

    Topic findById(Integer id);

    //删除指定日期前数据
    @Modifying
    @Query("DELETE FROM TopicDetail t WHERE t.delTime < ?1")
    int delOldData(LocalDateTime delTime);

    @Query("SELECT t FROM TopicDetail t WHERE t.topicId = ?1 and t.delTime > ?2 and t.delTime < ?3")
    List<TopicDetail> queryHistoryData(Integer topicId, LocalDateTime startTime, LocalDateTime endTime);

}
