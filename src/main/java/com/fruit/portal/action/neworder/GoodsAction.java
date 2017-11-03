package com.fruit.portal.action.neworder;

import com.fruit.newOrder.biz.dto.GoodsCategoryDTO;
import com.fruit.newOrder.biz.dto.GoodsVarietyDTO;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.service.neworder.GoodsInfoShowService;
import com.fruit.portal.vo.neworder.GoodsCommodityInfoVO;
import com.fruit.portal.vo.neworder.GoodsProductInfoVO;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@UriMapping("/neworder")
public class GoodsAction extends BaseAction {

	@Autowired
	private GoodsInfoShowService goodsInfoShowService;

	private static final Logger logger = LoggerFactory.getLogger(GoodsAction.class);

	/**
	 * 查询所有商品类别
	 * 
	 * @return
	 */
	@UriMapping(value = "/find_all_goods_categories")
	public AjaxResult findAllGoodsCategories() {
		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsCategoryDTO> goodsCategories = goodsInfoShowService.loadAllGoodsCategories();
			dataMap.put("goodsCategories", goodsCategories);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/findAllGoodsCategories].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


	/**
	 * 根据商品类别获取商品品种
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_varieties_by_category" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsVarietiesByCategory() {
		Map<String, Object> params = getParamsValidationResults();
		int categoryId = (Integer) params.get("categoryId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsVarietyDTO> goodsVarieties = goodsInfoShowService.loadGoodsVarietiesByCategory(categoryId);
			dataMap.put("goodsVarieties", goodsVarieties);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/findGoodsVarietiesByCategory].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}

	/**
	 * 根据商品品种，获取所有产品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_products_by_variety" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsProductsByVariety() {
		Map<String, Object> params = getParamsValidationResults();
		int varietyId = (Integer) params.get("varietyId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsProductInfoVO> goodsProductInfos = goodsInfoShowService.loadGoodsProductInfosByVariety(varietyId,false);
			dataMap.put("goodsProductInfos", goodsProductInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/findGoodsProductsByCategory].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


	/**
	 * 根据产品获取所有商品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_commodities_by_product" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsCommoditiesByProduct() {
		Map<String, Object> params = getParamsValidationResults();
		int productId = (Integer) params.get("productId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsCommodityInfoVO> goodsCommodityInfos = goodsInfoShowService.loadGoodsCommodityInfosByProduct(productId);
			dataMap.put("goodsCommodityInfos", goodsCommodityInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/findGoodsCommoditiesByProduct].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


	/**
	 * 根据商品品种，获取所有产品，产品中包含有商品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_products_commodities_by_variety" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsProductsCommoditiesByVariety() {
		Map<String, Object> params = getParamsValidationResults();
		int varietyId = (Integer) params.get("varietyId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsProductInfoVO> goodsProductInfos = goodsInfoShowService.loadGoodsProductInfosByVariety(varietyId,true);
			dataMap.put("goodsProductInfos", goodsProductInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/wechat/neworder/findGoodsProductsCommoditiesByVariety].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}

	/**
	 * 根据商品类别获取所有商品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_commodities_by_category" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsCommoditiesByCategory() {
		Map<String, Object> params = getParamsValidationResults();
		int categoryId = (Integer) params.get("categoryId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsCommodityInfoVO> goodsCommodityInfos = goodsInfoShowService.loadGoodsCommodityInfosByCategory(categoryId);
			dataMap.put("goodsCommodityInfos", goodsCommodityInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/findGoodsCommoditiesByVariety].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


	/**
	 * 根据商品品种获取所有商品
	 *
	 * @return
	 */
	@UriMapping(value = "/find_goods_commodities_by_variety" , interceptors = {"validationInterceptor"})
	public AjaxResult findGoodsCommoditiesByVariety() {
		Map<String, Object> params = getParamsValidationResults();
		int varietyId = (Integer) params.get("varietyId");

		int code = AjaxResultCode.OK.getCode();
		String msg = SUCCESS;
		try
		{
			Map<String, Object> dataMap = new HashMap<String, Object>();
			AjaxResult ajaxResult = new AjaxResult(code, msg);

			List<GoodsCommodityInfoVO> goodsCommodityInfos = goodsInfoShowService.loadGoodsCommodityInfosByVariety(varietyId);
			dataMap.put("goodsCommodityInfos", goodsCommodityInfos);
			ajaxResult.setData(dataMap);

			return ajaxResult;
		}
		catch (IllegalArgumentException e)
		{
			logger.error("[/neworder/findGoodsCommoditiesByVariety].Exception:{}",e);
			return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
		}
	}


}