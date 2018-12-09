package cn.mmall.service.impl;

import cn.mmall.common.ServerResponse;
import cn.mmall.dao.category.CategoryMapper;
import cn.mmall.pojo.Category;
import cn.mmall.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * @Author LJQ
 * @Date 2018/9/12 15:27
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger =  LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;


    public ServerResponse addCategory(String categoryName ,Integer parentId ){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//分类可用

        int rowCount = categoryMapper.insert(category);
        if(rowCount>0){
            return  ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
            List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
            if(CollectionUtils.isEmpty(categoryList)){
                        //为空
                logger.info("未找到当前分类子分类");
            }
            return ServerResponse.createBySuccess(categoryList);
    }

    //递归方法
    public ServerResponse<List<Integer>>  selectCategoryAndChildrenById(Integer categoryId){
            Set<Category> categorySet = Sets.newHashSet();
            findChildCategory(categorySet,categoryId);
            List<Integer> categoryIdList = Lists.newArrayList();
            if(categoryId != null){
                for(Category categoryItem : categorySet){
                    categoryIdList.add(categoryItem.getId());
                }
            }
            logger.info("+++++++++++++++++++++++++++++"+categorySet);
            logger.debug("========="+categoryIdList);
            logger.info("+++++++++++++++++++++++++"+categoryIdList);
            return  ServerResponse.createBySuccess(categoryIdList);
    }
    //递归方法
    /*
    HashSet类按照哈希算法来存取集合中的对象，存取速度比较快。
         1.Set中是不能出现重复数据的。
         2.Set中可以出现空数据。
         3.Set中的数据是无序的。
     */
    private Set<Category> findChildCategory(Set<Category> categorySet , Integer categoryId){
        //以当前id取对应分类
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        //当前分类不为空存在Set中
        if(category != null){
                categorySet.add(category);
        }
        //以当前id为父id取对应子分类  不存在子分类list集合为空跳出递归返回Set集合
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        //遍历当前list集合取list里每一个分类id 调自身方法 以当前id取对应分类  不为空存Set
        for(Category category1Item : categoryList){
                findChildCategory(categorySet,category1Item.getId());
        }
        return  categorySet;
    }
}
