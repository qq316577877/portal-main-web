package com.fruit.portal.action.wechat.account;

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
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@UriMapping("/wechat/delivery_address")
public class UserDeliveryAddressWeChatAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDeliveryAddressWeChatAction.class);

    @Autowired
    private DeliveryAddressService delieryAddressService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;


    @UriMapping(value = "/", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult addressList()
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

            return new AjaxResult(data);
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/wechat/delivery_address/list].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode() ,"参数不合法!");
        }
    }


    @UriMapping(value = "/add_user_receive_address_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult addAddress()
    {
        int userId = getLoginUserId();
       // Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        String receiver = (String) requesMap.get("receiver");
        int countryId = Integer.parseInt(String.valueOf( requesMap.get("countryId")));
        int provinceId = Integer.parseInt(String.valueOf( requesMap.get("provinceId")));
        int cityId = Integer.parseInt(String.valueOf( requesMap.get("cityId")));
        int districtId = Integer.parseInt(String.valueOf( requesMap.get("districtId")));
        String address = (String) requesMap.get("address");
        String zipCode = (String) requesMap.get("zipCode");
        String cellPhone = (String) requesMap.get("cellPhone");
        String phoneNum = (String) requesMap.get("phoneNum");
        int selected = Integer.parseInt(String.valueOf( requesMap.get("selected")));

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
            LOGGER.error("[/wechat/delivery_address/add_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/update_user_receive_address_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult updateAddress()
    {
        //Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        int id = Integer.parseInt(String.valueOf(requesMap.get("id")));
        String receiver = (String)requesMap.get("receiver");
        int countryId = Integer.parseInt(String.valueOf( requesMap.get("countryId")));
        int provinceId = Integer.parseInt(String.valueOf(requesMap.get("provinceId")));
        int cityId = Integer.parseInt(String.valueOf( requesMap.get("cityId")));
        int districtId = Integer.parseInt(String.valueOf(requesMap.get("districtId")));
        String address = (String)requesMap.get("address");
        String zipCode = (String)requesMap.get("zipCode");
        String cellPhone = (String)requesMap.get("cellPhone");
        String phoneNum = (String)requesMap.get("phoneNum");
        int selected = Integer.parseInt(String.valueOf( requesMap.get("selected")));

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
            LOGGER.error("[/wechat/delivery_address/update_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/set_default_address_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult setDefaultAddress()
    {
        int userId = getLoginUserId();
       // Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        int id = Integer.parseInt(String.valueOf( requesMap.get("id")));
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
            LOGGER.error("[/wechat/delivery_address/set_default_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/delete_user_receive_address_ajax", interceptors = "userLoginCheckInterceptor")
    public AjaxResult deleteAddress()
    {

        //Map requesMap = (Map) WebContext.getRequest().getAttribute(BizConstants.ATRR_PARAMETER_REQUEST);//super.getBodyObject(HashMap.class);
        Map requesMap  = super.getBodyObject(HashMap.class);

        int id = Integer.parseInt(String.valueOf( requesMap.get("id")));
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
            LOGGER.error("[/wechat/delivery_address/delete_user_receive_address_ajax].Exception:{}",e);
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
            LOGGER.error("[/wechat/delivery_address/get_user_receive_address_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }





}
