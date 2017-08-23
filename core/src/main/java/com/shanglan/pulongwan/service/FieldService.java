package com.shanglan.pulongwan.service;

import com.shanglan.pulongwan.base.AjaxResponse;
import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.repository.FieldRepository;
import com.shanglan.pulongwan.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuishiying on 2017/7/26.
 */
@Service
@Transactional
public class FieldService {

    @Autowired
    private FieldRepository fieldRepository;

    public AjaxResponse importService(InputStream in, MultipartFile file) throws Exception {


        List<List<Object>> listob = ExcelUtils.getBankListByExcel(in,file.getOriginalFilename());
        List<Field> list=new ArrayList<>();

        for (int i = 0; i < listob.size(); i++) {
            List<Object> lo = listob.get(i);
            Field field = new Field();
            try{
                String s0 = String.valueOf(lo.get(0));
                String s1 = String.valueOf(lo.get(1));
                String s2 = String.valueOf(lo.get(2));


                field.setTelemetrySignal(String.valueOf(lo.get(0)));
                field.setDescriber(String.valueOf(lo.get(1)));
                field.setRatio(Float.parseFloat(String.valueOf(lo.get(2))));
                list.add(field);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        fieldRepository.save(list);

        return AjaxResponse.success();
    }

    public Field findByTelemetrySignal(String data){
        Field field = fieldRepository.findByTelemetrySignal(data);
        return field;
    }

    public Field findById(Integer id){
        Field one = fieldRepository.findOne(id);
        return one;
    }

    public List<Field> findAll(){
        List<Field> all = fieldRepository.findAll();
        return all;
    }
    public List<Field> findAll(String keyword){
        if(keyword==null){
            keyword = "";
        }
        List<Field> all = fieldRepository.findAll("%"+keyword+"%");
        return all;
    }
    public List<Field> findAll(Integer id){
        List<Field> all = fieldRepository.findAll(id);
        return all;
    }

}
