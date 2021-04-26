package com.scu.lcw.backsystem.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 11:38
 */
@Component
public class PageUtils<T> {

    public List<T> listPagination(List<T> selectList, int currentPage, int pageSize) {
        return selectList.stream()
                .skip((currentPage - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

    }
}
