package cn.mmall.dao.cart;

import cn.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author WJ
 * @Date 2018/9/3 10:44
 */
public interface CartMapper {

    int insert(Cart record);

    int updateByPrimaryKeySelective(Cart record);

    /**
     * 根据用户id和产品id查询购物车
     *
     * @param userId
     * @param productId
     * @return
     */
    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     * 根据用户id查购物车的集合
     * @param userId
     * @return
     */
    List<Cart> selectCartByUserId(Integer userId);

    /**
     * 判断这个用户在这个表示是否有未勾选，如果有就不是勾选，如果没有就是全选
     *
     * @param userId
     * @return
     */
    int selectCartProductCheckedStatusByUserId(Integer userId);


    /**
     * 删除购物车产品
     *
     * @param userId
     * @param productIdList
     * @return
     */
    int deleteCartByUserIdProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);


    /**
     * 根据controller传的不同的参数来判断是否是全选还是全反选
     *
     * @param userId
     * @param checked
     * @return
     */
    int checkedOrUncheckedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    /**
     *
     * @param userId
     * @return
     */
    int getCartProductCount(@Param("userId") Integer userId);
    List<Cart> selectCheckedCartByUserId(Integer userId);
    int deleteByPrimaryKey(Integer id);

}
