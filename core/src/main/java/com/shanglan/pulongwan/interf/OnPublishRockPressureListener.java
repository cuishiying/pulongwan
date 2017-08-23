package com.shanglan.pulongwan.interf;

import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.entity.RockPressure;

import java.util.List;

/**
 * Created by cuishiying on 2017/7/24.
 */
public interface OnPublishRockPressureListener {
    void publish(List<RockPressure> data);
}
