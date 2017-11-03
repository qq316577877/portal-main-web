package com.fruit.portal.action.account;

import com.fruit.account.biz.dto.UserDeliveryAddressDTO;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.meta.MetaCountry;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserDeliveryAddressRequest;
import com.fruit.portal.service.account.DeliveryAddressService;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.vo.account.AddressVo;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户收件地址相关
 * <p/>
 * Create Author  : paul
 * Create  Time   : 2017-05-18
 * Project        : portal
 */
@Component
@UriMapping("/member/delivery_address")
public class UserDeliveryAddressAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDeliveryAddressAction.class);

    @Autowired
    private DeliveryAddressService delieryAddressService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;


    @UriMapping(value = "/", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String addressList()
    {
        int userId = getLoginUserId();
        try
        {
            List<UserDeliveryAddressDTO> deliveryAddressDTOs = delieryAddressService.loadAllAddress(userId);

            List<AddressVo> receiveAddress = this.delieryAddressService.wrapVOs(deliveryAddressDTOs);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("receiveAddress", receiveAddress);


            Collection<MetaCountry> countryList = this.metadataProvider.getCountryOnlyIndexMap().values();
            data.put("codeList", countryList);//国家list自带areaCode

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("__DATA", super.toJson(data));

            return "/account/delivery_address_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/delivery_address/list].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    @UriMapping(value = "/add_user_receive_address_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult addAddress()
    {
        int userId = getLoginUserId();
        Map<String, Object> params = getParamsValidationResults();
        String receiver = (String) params.get("receiver");
        int countryId = (Integer) params.get("countryId");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");
        String address = (String) params.get("address");
        String zipCode = (String) params.get("zipCode");
        String cellPhone = (String) params.get("cellPhone");
        String phoneNum = (String) params.get("phoneNum");
        int selected = (Integer) params.get("selected");

        try
        {
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            UserDeliveryAddressRequest userDeliveryAddressRequest = new UserDeliveryAddressRequest();


            userDeliveryAddressRequest.setUserId(userId);
            userDeliveryAddressRequest.setReceiver(receiver);
            userDeliveryAddressRequest.setProvinceId(provinceId);
            userDeliveryAddressRequest.setCountryId(countryId);
            userDeliveryAddressRequest.setCityId(cityId);
            userDeliveryAddressRequest.setDistrictId(districtId);
            userDeliveryAddressRequest.setAddress(address);
            userDeliveryAddressRequest.setZipCode(zipCode);
            userDeliveryAddressRequest.setCellPhone(cellPhone);
            userDeliveryAddressRequest.setPhoneNum(phoneNum);
            userDeliveryAddressRequest.setSelected(selected);

            delieryAddressService.addAddress(userModel,userDeliveryAddressRequest);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/delivery_address/add_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/update_user_receive_address_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult updateAddress()
    {
        Map<String, Object> params = getParamsValidationResults();
        int id = (Integer) params.get("id");
        String receiver = (String) params.get("receiver");
        int countryId = (Integer) params.get("countryId");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");
        String address = (String) params.get("address");
        String zipCode = (String) params.get("zipCode");
        String cellPhone = (String) params.get("cellPhone");
        String phoneNum = (String) params.get("phoneNum");
        int selected = ((Integer) params.get("selected"));

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            UserDeliveryAddressDTO addressDto = delieryAddressService.loadAddressById(id);
            if (null != addressDto)
            {
                addressDto.setReceiver(receiver);
                addressDto.setCountryId(countryId);
                addressDto.setProvinceId(provinceId);
                addressDto.setCityId(cityId);
                addressDto.setDistrictId(districtId);
                addressDto.setAddress(address);
                addressDto.setZipCode(zipCode);
                addressDto.setCellPhone(cellPhone);
                addressDto.setPhoneNum(phoneNum);
                addressDto.setSelected(selected);
                delieryAddressService.updateAddress(userModel,addressDto);
            }
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/delivery_address/update_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/set_default_address_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult setDefaultAddress()
    {
        int userId = getLoginUserId();
        int id = super.getIntParameter("id", 0);
        try
        {
            Validate.isTrue(id > 0, "id must bigger then 0.");

            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);


            delieryAddressService.setDefaultAddress(userModel,userId, id);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/delivery_address/set_default_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/delete_user_receive_address_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult deleteAddress()
    {
        int id = super.getIntParameter("id", 0);
        try
        {
            Validate.isTrue(id > 0, "id must bigger then 0.");

            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            delieryAddressService.deleteAddress(userModel,id);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/delivery_address/delete_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }



    /**
     * 查询会员收件地址信息
     * @return
     */
    @UriMapping(value = "/get_user_receive_address_ajax", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult getUserReceiveAddress()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);


            List<UserDeliveryAddressDTO> deliveryAddressDTOs = delieryAddressService.loadAllAddress(userId);

            List<AddressVo> addressVoList = this.delieryAddressService.wrapVOs(deliveryAddressDTOs);

            Map<String, Object> dataMap = new HashMap<String, Object>();

            AjaxResult ajaxResult = new AjaxResult(code, msg);

            dataMap.put("receiveAddress", addressVoList);
            ajaxResult.setData(dataMap);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/delivery_address/get_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }





}
