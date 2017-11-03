package com.fruit.portal.action.account;


import com.fruit.account.biz.dto.UserSupplierDTO;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.meta.*;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.UserSupplierRequest;
import com.fruit.portal.service.account.SupplierService;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.vo.account.SupplierVo;
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
 * 用户供应商相关
 * <p/>
 * Create Author  : paul
 * Create  Time   : 2017-05-18
 * Project        : portal
 */
@Component
@UriMapping("/member/supplier")
public class UserSupplierAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSupplierAction.class);

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private MemberService memberService;

    @UriMapping(value = "/", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String supplierList()
    {
        int userId = getLoginUserId();
        try
        {
            List<UserSupplierDTO> supplierDTOs = supplierService.loadAllSupplier(userId);

            List<SupplierVo> supplierList = this.supplierService.wrapVOs(supplierDTOs);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("supplierList", supplierList);

            Collection<MetaCountry> countryList = this.metadataProvider.getCountryOnlyIndexMap().values();
            data.put("codeList", countryList);//国家list自带areaCode

            HttpServletRequest request = WebContext.getRequest();
            request.setAttribute("__DATA", super.toJson(data));
            return "/account/supplier_list";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/supplier/list].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    @UriMapping(value = "/add_user_supplier_information_ajax", interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult addSupplier()
    {
        int userId = getLoginUserId();
        Map<String, Object> params = getParamsValidationResults();
        String supplierName = (String) params.get("supplierName");
        String supplierContact = (String) params.get("supplierContact");


        int countryId = (Integer) params.get("countryId");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");

        String address = (String) params.get("address");
        String zipCode = (String) params.get("zipCode");
        String cellPhone = (String) params.get("cellPhone");
        String phoneNum = (String) params.get("phoneNum");

        try
        {
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            UserSupplierRequest userSupplierRequest = new UserSupplierRequest();


            userSupplierRequest.setUserId(userId);
            userSupplierRequest.setSupplierName(supplierName);
            userSupplierRequest.setSupplierContact(supplierContact);

            userSupplierRequest.setCountryId(countryId);
            userSupplierRequest.setProvinceId(provinceId);
            userSupplierRequest.setCityId(cityId);
            userSupplierRequest.setDistrictId(districtId);
            userSupplierRequest.setAddress(address);
            userSupplierRequest.setZipCode(zipCode);
            userSupplierRequest.setCellPhone(cellPhone);
            userSupplierRequest.setPhoneNum(phoneNum);

            supplierService.addSupplier(userModel,userSupplierRequest);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/supplier/add_user_supplier_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/update_user_supplier_information_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult updateSupplier()
    {
        Map<String, Object> params = getParamsValidationResults();
        int id = (Integer) params.get("id");
        String supplierName = (String) params.get("supplierName");
        String supplierContact = (String) params.get("supplierContact");

        int countryId = (Integer) params.get("countryId");
        int provinceId = (Integer) params.get("provinceId");
        int cityId = (Integer) params.get("cityId");
        int districtId = (Integer) params.get("districtId");

        String address = (String) params.get("address");
        String zipCode = (String) params.get("zipCode");
        String cellPhone = (String) params.get("cellPhone");
        String phoneNum = (String) params.get("phoneNum");

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            UserSupplierDTO supplierDto = supplierService.loadSupplierById(id);
            if (null != supplierDto)
            {
                supplierDto.setSupplierName(supplierName);
                supplierDto.setSupplierContact(supplierContact);
                supplierDto.setCountryId(countryId);
                supplierDto.setProvinceId(provinceId);
                supplierDto.setCityId(cityId);
                supplierDto.setDistrictId(districtId);
                supplierDto.setAddress(address);
                supplierDto.setZipCode(zipCode);
                supplierDto.setCellPhone(cellPhone);
                supplierDto.setPhoneNum(phoneNum);
                supplierService.updateSupplier(userModel,supplierDto);
            }
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/supplier/update_user_supplier_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    @UriMapping(value = "/delete_user_supplier_information_ajax" ,interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult deleteSupplier()
    {
        int id = super.getIntParameter("id", 0);
        try
        {
            Validate.isTrue(id > 0, "id must bigger then 0.");

            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            supplierService.deleteSupplier(userModel,id);
            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/supplier/delete_user_supplier_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询会员供应商信息
     * @return
     */
    @UriMapping(value = "/get_user_supplier_information_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getUserSuppliersInformation()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {

            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            List<UserSupplierDTO> supplierDTOs = supplierService.loadAllSupplier(userId);

            List<SupplierVo> supplierVoList = this.supplierService.wrapVOs(supplierDTOs);

            Map<String, Object> dataMap = new HashMap<String, Object>();

            AjaxResult ajaxResult = new AjaxResult(code, msg);

            dataMap.put("supplierList", supplierVoList);
            ajaxResult.setData(dataMap);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/supplier/get_user_supplier_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

}
