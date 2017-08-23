package com.shanglan.pulongwan.service;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.dto.QueryDTO;
import com.shanglan.pulongwan.entity.Topic;
import com.shanglan.pulongwan.entity.TopicDetail;
import com.shanglan.pulongwan.repository.DataRepository;
import com.shanglan.pulongwan.repository.ManageRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuishiying on 2017/5/19.
 */
@Service
@Transactional
public class DbService {
    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private ManageRepository manageRepository;

    public AjaxResponse delOldData(LocalDateTime delTime){
        int i = dataRepository.delOldData(delTime);
        return AjaxResponse.success(i);
    }

    /**
     * 获取历史数据
     * @param queryDTO
     * @return
     */
    public List<TopicDetail> findHistoryData(Integer id, QueryDTO queryDTO){
        Topic topic = manageRepository.findById(id);
        queryDTO.setTopic(topic.getTopic());
        if(queryDTO.getQueryDate()==null){
            queryDTO.setQueryDate(LocalDate.now());
        }
        Specification<TopicDetail> spec = this.getWhereClause(queryDTO);
        List<TopicDetail> list = dataRepository.findAll(spec);
        return list;
    }


    /**
     * 查询条件
     * @param queryVo
     * @return
     */
    private Specification<TopicDetail> getWhereClause(QueryDTO queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();

            //主题
            if(queryVo!=null&& StringUtils.isNotBlank(queryVo.getTopic())){
                predicate.add(cb.equal(root.<String>get("topic"),"%" + queryVo.getTopic().trim() + "%"));
            }

            //时间
            if (queryVo!=null&&queryVo.getQueryDate() != null) {
                LocalDateTime begin = LocalDateTime.of(queryVo.getQueryDate(), LocalTime.MIN);
                LocalDateTime end = LocalDateTime.of(queryVo.getQueryDate(), LocalTime.MAX);
                Predicate date = cb.and(cb.greaterThanOrEqualTo(root.<LocalDateTime>get("delTime"), begin), cb.lessThanOrEqualTo(root.<LocalDateTime>get("delTime"), end));
                predicate.add(date);
            }
            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }
}
