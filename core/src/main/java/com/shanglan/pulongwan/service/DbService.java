package com.shanglan.pulongwan.service;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.dto.QueryDTO;
import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.entity.TopicDetail;
import com.shanglan.pulongwan.repository.DataRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private FieldService fieldService;

    public AjaxResponse delOldData(LocalDateTime delTime){
        int i = dataRepository.delOldData(delTime);
        return AjaxResponse.success(i);
    }

    public void delAll(){
        dataRepository.deleteAllInBatch();
    }

    /**
     * 获取历史数据
     * @param queryDTO
     * @return
     */
    public List<TopicDetail> findHistoryData(Integer id, QueryDTO queryDTO){
        List<TopicDetail> list = queryHistory(id, queryDTO);
        return list;
    }

    public List<TopicDetail> queryHistory(Integer id, QueryDTO queryDTO){
        Field field = fieldService.findById(id);
        if(queryDTO.getQueryDate()==null){
            queryDTO.setQueryDate(LocalDate.now());
        }
        LocalDateTime begin = LocalDateTime.of(queryDTO.getQueryDate(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(queryDTO.getQueryDate(), LocalTime.MAX);

        List<TopicDetail> list = dataRepository.queryHistoryData(id, begin, end);
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
