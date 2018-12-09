package cn.mmall.dao.product;

import cn.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author WJ
 * @Date 2018/8/26 21:27
 */
public interface ProductMapper {

    /**
     * 新增
     * @param product
     * @return
     */
    int insert(Product product);

    /**
     * 修改
     * @param product
     * @return
     */
    int updateByPrimaryKey(Product product);

    /**
     *
     * @param product
     * @return
     */
    int updateByPrimaryKeySelective(Product product);

    /**
     *
     * @param id
     * @return
     */
    Product selectByPrimaryKey(Integer id);

    /**
     *
     * @return
     */
    List<Product> selectList();

    /**
     * 模糊查询
     * @param productName
     * @param productId
     * @return
     */
    List<Product> selectByNameAndProductId(@Param("productName") String productName,@Param("productId") Integer productId);

    /**
     * 前台搜索，列表，动态查询分页
     * @param productName
     * @param categoryIdList
     * @return
     */
    List<Product> selectByNameAndCategoryIds(@Param("productName")String productName,@Param("categoryIdList")List<Integer> categoryIdList);
}
