package com.fruit.portal.action.account;

import com.fruit.account.biz.common.UserEnterpriseStatusEnum;
import com.fruit.account.biz.common.UserTypeEnum;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.portal.action.BaseAction;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.UserModel;
import com.fruit.portal.model.account.EnterpriseRequest;
import com.fruit.portal.service.account.EnterpriseService;
import com.fruit.portal.service.account.MemberService;
import com.fruit.portal.service.account.UserModifyService;
import com.fruit.portal.service.common.FileUploadService;
import com.fruit.portal.utils.UrlUtils;
import com.ovfintech.arch.web.mvc.dispatch.UriMapping;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业认证相关
 * <p/>
 * Create Author  : terry
 * Create  Time   : 2017-05-16
 * Project        : portal
 */
@Component
@UriMapping("/member/enterprise/auth")
public class UserEnterpiseAction extends BaseAction
{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEnterpiseAction.class);

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private FileUploadService fileUploadService;


    @UriMapping(value = "/", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String showPage()
    {
        try
        {
            String ftl = "";
            int userId = super.getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel, "账号异常!");
            UserEnterpriseDTO enterpriseDTO = enterpriseService.loadEnterpriseByUserId(userId);
            HttpServletRequest request = WebContext.getRequest();
            // 用户已填写企业认证
            if (null != enterpriseDTO)
            {
                // 已审核的用户，显示审核后的类型，未审核的显示申请的类型
                int type = enterpriseDTO.getType();
                if (UserEnterpriseStatusEnum.VERIFIED.getStatus() == userModel.getEnterpriseVerifyStatus())
                {
                    type = userModel.getType();
                }
                request.setAttribute("mobile", userModel.getMobile());
                request.setAttribute("identity", userModel.getIdentity());
                request.setAttribute("email", userModel.getMail());
                request.setAttribute("qq", userModel.getQQ());
                request.setAttribute("type_id", type);
                request.setAttribute("type_name", UserTypeEnum.get(type).getMessage());
                request.setAttribute("enterprise_name", enterpriseDTO.getEnterpriseName());
                request.setAttribute("credential", enterpriseDTO.getCredential());
                request.setAttribute("phone_num", enterpriseDTO.getPhoneNum());
                request.setAttribute("identity_front", enterpriseDTO.getIdentityFront());
                request.setAttribute("identity_back", enterpriseDTO.getIdentityBack());
                request.setAttribute("licence", enterpriseDTO.getLicence());
                request.setAttribute("attachment_one", enterpriseDTO.getAttachmentOne());
                request.setAttribute("attachment_two", enterpriseDTO.getAttachmentTwo());
                request.setAttribute("name", enterpriseDTO.getName());
                request.setAttribute("country_id", enterpriseDTO.getCountryId());
                request.setAttribute("country_name", metadataProvider.getCountryName(enterpriseDTO.getCountryId()));
                request.setAttribute("province_id", enterpriseDTO.getProvinceId());
                request.setAttribute("province_name", metadataProvider.getProvinceName(enterpriseDTO.getProvinceId()));
                request.setAttribute("city_id", enterpriseDTO.getCityId());
                request.setAttribute("city_name", metadataProvider.getCityName(enterpriseDTO.getCityId()));
                request.setAttribute("district_id", enterpriseDTO.getDistrictId());
                request.setAttribute("district_name", metadataProvider.getAreaName(enterpriseDTO.getDistrictId()));
                request.setAttribute("address", enterpriseDTO.getAddress());
                request.setAttribute("status", enterpriseDTO.getStatus());
                request.setAttribute("reject_note", enterpriseDTO.getRejectNote());
                ftl = "/account/enterprise_auth_show";
                if (UserEnterpriseStatusEnum.REJECTED.getStatus() == enterpriseDTO.getStatus()) // 被驳回的认证继续编辑
                {
                    ftl = "/account/enterprise_auth_edit";
                }
            }
            else
            {
                ftl = "/account/enterprise_auth_edit";
            }
            if ("/account/enterprise_auth_edit".equalsIgnoreCase(ftl))
            {
                Map<String, Object> data = new HashMap<String, Object>();
//                data.put("meta_three_city", metadataProvider.getProvinceIndexMap());城市信息直接通过json文件提供给前台，不再使用预埋信息
//                data.put("meta_country_only", metadataProvider.getCountryOnlyIndexMap());
                data.put("types", EnterpriseService.ENTER_TYPES);
                data.put("callback_url", UrlUtils.getMemberEnterpriseVerifyUrl(super.getDomain()));
                request.setAttribute("__DATA", super.toJson(data));
            }
            return ftl;
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/show].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    /**
     * 认证状态
     * @return
     */
    @UriMapping(value = "/auditStatus", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String auditStatusPage()
    {
        int userId = getLoginUserId();
        try
        {
            return "/account/member_audit_status";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/auditStatus].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    /**
     * 会员认证（空）
     * @return
     */
    @UriMapping(value = "/memberAuditNull", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String enterpriseAuditNullPage()
    {
        int userId = getLoginUserId();
        try
        {
            return "/account/member_audit_null";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/memberAuditNull].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }

    /**
     * 认证状态
     * @return
     */
    @UriMapping(value = "/auditSucceed", interceptors = {"userLoginCheckInterceptor", "userNavigatorInterceptor"})
    public String auditSucceedPage()
    {
        int userId = getLoginUserId();
        try
        {
            return "/account/member_audit_succeed";
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/auditSucceed].Exception:{}",e);
            return FTL_ERROR_400;
        }
    }


    @UriMapping(value = "/is_enter_name_available_ajax",interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult isEnterpriseNameAvailable()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        String enterpriseName = StringUtils.trimToEmpty((String) validationResults.get("enterpriseName"));

         try
        {
            int userId = getLoginUserId();

            if (enterpriseService.isEnterpriseNameAvailable(userId, enterpriseName))
            {
                return new AjaxResult();
            }
            else
            {
                return new AjaxResult(AjaxResultCode.REQUEST_MISSING_PARAM.getCode(), "企业名已存在!");
            }
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/is_enter_name_available_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 提交认证：个人
     * @return
     */
    @UriMapping(value = "/personal_auth_ajax", interceptors = { "userLoginCheckInterceptor","validationInterceptor"})
    public AjaxResult savePersonalAuth()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();

        String name = StringUtils.trimToEmpty((String) validationResults.get("name"));
        String identity = (String) validationResults.get("identity");//身份证
        int type = UserTypeEnum.PERSONAL.getType();//个人认证
        int countryId = (Integer) validationResults.get("countryId");
        int provinceId = (Integer) validationResults.get("provinceId");
        int cityId = (Integer) validationResults.get("cityId");
        int districtId = (Integer) validationResults.get("districtId");//行政区
        String address = (String) validationResults.get("address");
        String phoneNum = (String) validationResults.get("phoneNum");
        String identityFront = (String) validationResults.get("identityFront");
        String identityBack = (String) validationResults.get("identityBack");
        String attachmentOne = (String) validationResults.get("attachmentOne");
        String attachmentTwo = (String) validationResults.get("attachmentTwo");

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            EnterpriseRequest enterpriseRequest = new EnterpriseRequest();

            enterpriseRequest.setType(type);
            enterpriseRequest.setName(name);
            enterpriseRequest.setPhoneNum(phoneNum);
            enterpriseRequest.setIdentity(identity);
            enterpriseRequest.setCountryId(countryId);
            enterpriseRequest.setProvinceId(provinceId);
            enterpriseRequest.setCityId(cityId);
            enterpriseRequest.setDistrictId(districtId);
            enterpriseRequest.setAddress(address);
            enterpriseRequest.setIdentityFront(identityFront);
            enterpriseRequest.setIdentityBack(identityBack);
            enterpriseRequest.setAttachmentOne(attachmentOne);
            enterpriseRequest.setAttachmentTwo(attachmentTwo);

            enterpriseService.enterpriseAuth(userModel,  enterpriseRequest,super.getUserIp());

            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/personal_auth_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 提交认证  企业
     * @return
     */
    @UriMapping(value = "/enterprise_auth_ajax", interceptors = {"userLoginCheckInterceptor", "validationInterceptor"})
    public AjaxResult saveEnterpriseAuth()
    {
        Map<String, Object> validationResults = super.getParamsValidationResults();
        String enterpriseName = StringUtils.trimToEmpty((String) validationResults.get("enterpriseName"));
        String credential = (String) validationResults.get("credential");//证件号
        String name = StringUtils.trimToEmpty((String) validationResults.get("legalPerson"));//法人姓名
        String identity = (String) validationResults.get("identity");//法人身份证

        int type = UserTypeEnum.ENTERPRISE.getType();//企业认证

        int countryId = (Integer) validationResults.get("countryId");
        int provinceId = (Integer) validationResults.get("provinceId");
        int cityId = (Integer) validationResults.get("cityId");
        int districtId = (Integer) validationResults.get("districtId");//行政区
        String address = (String) validationResults.get("address");
        String phoneNum = (String) validationResults.get("phoneNum");
        String identityFront = (String) validationResults.get("identityFront");
        String identityBack = (String) validationResults.get("identityBack");
        String licence = (String) validationResults.get("licence");//营业执照 或 社会信用代码证

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            EnterpriseRequest enterpriseRequest = new EnterpriseRequest();

            enterpriseRequest.setType(type);
            enterpriseRequest.setEnterpriseName(enterpriseName);
            enterpriseRequest.setName(name);
            enterpriseRequest.setPhoneNum(phoneNum);
            enterpriseRequest.setIdentity(identity);
            enterpriseRequest.setCountryId(countryId);
            enterpriseRequest.setProvinceId(provinceId);
            enterpriseRequest.setCityId(cityId);
            enterpriseRequest.setDistrictId(districtId);
            enterpriseRequest.setAddress(address);
            enterpriseRequest.setIdentityFront(identityFront);
            enterpriseRequest.setIdentityBack(identityBack);
            enterpriseRequest.setCredential(credential);
            enterpriseRequest.setLicence(licence);

            enterpriseService.enterpriseAuth(userModel,  enterpriseRequest,super.getUserIp());

            return new AjaxResult();
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/enterprise_auth_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询会员认证信息(个人与企业通用)
     * @return
     */
    @UriMapping(value = "/get_user_auth_information_ajax", interceptors = { "userLoginCheckInterceptor"})
    public AjaxResult getUserAuthInformation()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            EnterpriseRequest enterpriseRequest = enterpriseService.loadEnterpriseByUserId(userId,userModel);


            AjaxResult ajaxResult = new AjaxResult(code, msg);

            ajaxResult.setData(enterpriseRequest);


            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/get_user_auth_information_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

    /**
     * 查询认证结果
     * @return
     */
    @UriMapping(value = "/enterprise_auth_status_ajax", interceptors = {"userLoginCheckInterceptor"})
    public AjaxResult getUserAuthStatus()
    {
        int code = AjaxResultCode.OK.getCode();
        String msg = SUCCESS;

        try
        {
            int userId = getLoginUserId();
            UserModel userModel = super.loadUserModel(userId);
            Validate.isTrue(null != userModel);

            UserEnterpriseDTO enterpriseDTO = enterpriseService.loadEnterpriseByUserId(userId);

            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("enterpriseVerifyStatus", enterpriseDTO.getStatus());
            dataMap.put("rejectNote", enterpriseDTO.getRejectNote());

            AjaxResult ajaxResult = new AjaxResult(code, msg);
            ajaxResult.setData(dataMap);

            return ajaxResult;

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("[/member/enterprise/auth/enterprise_auth_status_ajax].Exception:{}",e);
            return new AjaxResult(AjaxResultCode.REQUEST_BAD_PARAM.getCode(), e.getMessage());
        }
    }

}
