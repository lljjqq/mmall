package cn.mmall.dao.shipping;

import cn.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author WJ
 * @Date 2018/9/5 9:20
 */
public interface ShippingMapper {

    /**
     * 增加地址
     *
     * @param record
     * @return
     */
    int insert(Shipping record);

    /**
     * 根据userId和shippingId删除地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    int deleteByShippingIdUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    /**
     * 更新地址
     *
     * @param record
     * @return
     */
    int updateByShipping(Shipping record);

    /**
     * 查询地址
     * @param userId
     * @param shippingId
     * @return
     */
    Shipping selectByShippingIdUserId(@Param("userId")Integer userId,@Param("shippingId") Integer shippingId);

    /**
     * 根据用户id查询地址
     * @param userId
     * @return
     */
    List<Shipping> selectByUserId(@Param("userId")Integer userId);
    Shipping selectByPrimaryKey(Integer shippingId);
}
