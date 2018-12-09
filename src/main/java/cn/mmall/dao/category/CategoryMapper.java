package cn.mmall.dao.category;

import cn.mmall.pojo.Category;

import java.util.List;

/**
 * @Author WJ
 * @Date 2018/8/26 12:47
 */
public interface CategoryMapper {

    /**
     * 增加分类
     * @param category
     * @return
     */
    int insert(Category category);

    /**
     * 更新分类
     * @param category
     * @return
     */
    int updateByPrimaryKeySelective(Category category);

    /**
     *
     * @param parentId
     * @return
     */
    List<Category> selectCategoryChildrenByParentId(Integer parentId);

    /**
     *
     * @param id
     * @return
     */
    Category selectByPrimaryKey(Integer id);




}
