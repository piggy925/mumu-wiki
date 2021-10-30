package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mumu.wiki.model.mapper.CategoryMapper;
import com.mumu.wiki.model.pojo.Category;
import com.mumu.wiki.req.CategoryQueryReq;
import com.mumu.wiki.req.CategorySaveReq;
import com.mumu.wiki.resp.CategoryResp;
import com.mumu.wiki.service.CategoryService;
import com.mumu.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryResp> getAllCategory() {
        List<Category> categoryList = categoryMapper.selectCategoryList();
        return getCategoryResp(categoryList);
    }

    @Override
    public List<CategoryResp> searchByName(CategoryQueryReq categoryQueryReq) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.like("name", categoryQueryReq.getName());
        List<Category> categoryList = categoryMapper.selectList(wrapper);
        return getCategoryResp(categoryList);
    }

    @Override
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (category.getId() != null) {
            //分类id不为空，执行更新操作
            categoryMapper.updateByPrimaryKeySelective(category);
        } else {
            //分类id为空，执行插入操作
            categoryMapper.insertSelective(category);
        }
    }

    @Override
    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    private List<CategoryResp> getCategoryResp(List<Category> categoryList) {
        List<CategoryResp> categoryRespList = CopyUtil.copyList(categoryList, CategoryResp.class);
        return categoryRespList;
    }
}