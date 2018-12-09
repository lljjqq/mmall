package cn.mmall.service;

import cn.mmall.common.ServerResponse;
import cn.mmall.pojo.Product;
import cn.mmall.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * @Author LJQ
 * @Date 2018/9/16 18:59
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse setSaleStatus(Integer productId,Integer status);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
