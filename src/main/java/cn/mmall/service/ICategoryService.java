package cn.mmall.service;

import cn.mmall.common.ServerResponse;
import cn.mmall.pojo.Category;

import java.util.List;

/**
 * @Author LJQ
 * @Date 2018/9/12 15:26
 */
public interface ICategoryService {

    ServerResponse addCategory(String categoryName , Integer parentId );

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);



}
