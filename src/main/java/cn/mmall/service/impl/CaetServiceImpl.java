package cn.mmall.service.impl;

import cn.mmall.common.Const;
import cn.mmall.common.ResponseCode;
import cn.mmall.common.ServerResponse;
import cn.mmall.dao.cart.CartMapper;
import cn.mmall.dao.category.CategoryMapper;
import cn.mmall.dao.product.ProductMapper;
import cn.mmall.pojo.Cart;
import cn.mmall.pojo.Product;
import cn.mmall.service.ICartService;
import cn.mmall.util.BigDecimalUtil;
import cn.mmall.util.PropertiesUtil;
import cn.mmall.vo.CartProductVo;
import cn.mmall.vo.CartVo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author LJQ
 * @Date 2018/9/22 23:02
 */
@Service("iCartService")
public class CaetServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 购物车新增
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null){
            //产品不再购物车需新增
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);//数量
            cartItem.setChecked(Const.Cart.CHECKED);//默认选中
            cartItem.setProductId(productId);//商品id
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);//购物车增加
        }else{
            //产品已存在 数量加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);//更新购物车
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);

    }

    public ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);

    }

    public ServerResponse<CartVo> deleteProduct(Integer userId,String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteCartByUserIdProductIds(userId,productList);
        return this.list(userId);
    }


    public ServerResponse<CartVo> list (Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }


    public ServerResponse<CartVo> selectOrUnSelect (Integer userId,Integer productId,Integer checked){
        cartMapper.checkedOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }

    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null){
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.getCartProductCount(userId));
    }








    //根据userid查当前用户购物车里的商品
    private CartVo getCartVoLimit(Integer userId){
        CartVo cartVo = new CartVo();//查购物车中的商品
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);

        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        //Java在java.math包中提供的API类BigDecimal，用来对超过16位有效位的数进行精确的运算
         BigDecimal cartTotalPrice = new BigDecimal("0");

         if(CollectionUtils.isNotEmpty(cartList)){
                for(Cart cartItem : cartList){
                    CartProductVo cartProductVo = new CartProductVo();
                    cartProductVo.setId(cartItem.getId());
                    cartProductVo.setUserId(cartItem.getUserId());
                    cartProductVo.setProductId(cartItem.getProductId());
                    //根据商品id查商品
                    Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                    if(product != null){
                        //赋值vo
                        cartProductVo.setProductMainImage(product.getMainImage());
                        cartProductVo.setProductName(product.getName());
                        cartProductVo.setProductSubtitle(product.getSubtitle());
                        cartProductVo.setProductStatus(product.getStatus());
                        cartProductVo.setProductPrice(product.getPrice());
                        cartProductVo.setProductStock(product.getStock());
                        //判断库存
                        int buyLimitCount = 0;
                        //product.getStock()数据库数量 cartItem.getQuantity()购物车数量
                        if(product.getStock() >= cartItem.getQuantity()){
                            //库存充足的时候
                            buyLimitCount = cartItem.getQuantity();
                            cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                        }else{
                            buyLimitCount = product.getStock();
                            cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                            //购物车中更新有效库存
                            Cart cartForQuantity = new Cart();
                            cartForQuantity.setId(cartItem.getId());
                            cartForQuantity.setQuantity(buyLimitCount);
                            cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                        }
                        cartProductVo.setQuantity(buyLimitCount);
                        //计算总价  再调用这个对象的doubleValue()方法返回其对应的double数值
                        //某一个产品总价
                        cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity()));
                        //设置状态勾选不勾选
                        cartProductVo.setProductChecked(cartItem.getChecked());
                    }
                    //价格计算
                    if(cartItem.getChecked() == Const.Cart.CHECKED){
                        //如果已经勾选,增加到整个的购物车总价中 以自己去加上面初始化为0
                        cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                    }
                    cartProductVoList.add(cartProductVo);
                }
         }

        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

         return cartVo;
    }
    //判断购物车是否全选
    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;

    }




}
