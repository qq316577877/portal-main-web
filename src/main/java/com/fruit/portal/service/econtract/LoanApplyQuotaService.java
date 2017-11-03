package com.fruit.portal.service.econtract;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fruit.base.biz.common.BaseRuntimeConfig;
import com.fruit.portal.service.common.RuntimeConfigurationService;
import com.ovfintech.arch.web.mvc.interceptor.WebContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fruit.account.biz.dto.UserAccountDTO;
import com.fruit.account.biz.dto.UserEnterpriseDTO;
import com.fruit.account.biz.service.UserAccountService;
import com.fruit.base.biz.common.LoanSmsBizTypeEnum;
import com.fruit.base.biz.dto.AreaDTO;
import com.fruit.base.biz.dto.CityDTO;
import com.fruit.base.biz.dto.LoanSmsContactsConfigDTO;
import com.fruit.base.biz.service.AreaService;
import com.fruit.base.biz.service.CityService;
import com.fruit.base.biz.service.LoanSmsContactsConfigService;
import com.fruit.loan.biz.common.LoanUserCreditStatusEnum;
import com.fruit.loan.biz.dto.LoanUserAuthInfoDTO;
import com.fruit.loan.biz.dto.LoanUserCreditInfoDTO;
import com.fruit.loan.biz.service.LoanMessageService;
import com.fruit.loan.biz.service.LoanUserAuthInfoService;
import com.fruit.loan.biz.service.LoanUserCreditInfoService;
import com.fruit.portal.meta.MetadataProvider;
import com.fruit.portal.model.AjaxResult;
import com.fruit.portal.model.AjaxResultCode;
import com.fruit.portal.model.loan.LoanUserAuthInfoModel;
import com.fruit.portal.queue.PushBorrowContractProxy;
import com.fruit.portal.service.account.EnterpriseService;
import com.fruit.portal.service.common.EnvService;
import com.fruit.portal.service.loan.LoanAuthCreditService;
import com.fruit.portal.utils.BizUtils;
import com.fruit.portal.utils.MoneyUtil;
import com.fruit.portal.utils.NumberUtil;
import com.ovft.contract.api.bean.CreateEcontractBean;
import com.ovft.contract.api.bean.CreateEsealBean;
import com.ovft.contract.api.bean.EcontractCaptchaSignBean;
import com.ovft.contract.api.bean.EcontractSignBean;
import com.ovft.contract.api.bean.OpenAccountInfoBean;
import com.ovft.contract.api.bean.ResponseVo;
import com.ovft.contract.api.bean.SignWithoutCaptchaBean;
import com.ovft.contract.api.bean.TemplateVo;
import com.ovft.contract.api.service.AccountInfoService;
import com.ovft.contract.api.service.EcontractService;
import com.ovft.contract.api.service.ElectronicSealService;
import com.ovft.contract.api.service.TemplateService;

import javax.annotation.Resource;

@Service
public class LoanApplyQuotaService{

	private static final Logger logger = LoggerFactory.getLogger(LoanApplyQuotaService.class);

	@Resource
	private LoanAuthCreditService loanAuthCreditService;

	@Resource
	private LoanUserAuthInfoService loanUserAuthInfoService;

	@Resource
	private AccountInfoService accountInfoService;

	@Resource
	private ElectronicSealService electronicSealService;

	@Resource
	private TemplateService templateService;

	@Resource
	private EcontractService econtractService;

	@Resource
	private LoanUserCreditInfoService loanUserCreditInfoService;

	@Resource
	private RuntimeConfigurationService runtimeConfigurationService;

	@Resource
	private EnvService envService;

	@Resource
	private EnterpriseService enterpriseService;

	@Resource
	private MetadataProvider metadataProvider;

	@Resource
	private AreaService areaService;

	@Resource
	private CityService cityService;

	@Resource
	private UserAccountService userAccountService;

	@Resource
	private LoanSmsContactsConfigService loanSmsContactsConfigService;

	@Resource
	LoanMessageService loanMessageService;

	private static final String APP_NAME = "www.fruit.com";


	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("rawtypes")
	public AjaxResult openAccount(int userId, String path) {
		AjaxResult ajaxResult = new AjaxResult();
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		// 开通安心签账户前先确认是否开户
		if (loanUserAuthInfo != null && loanUserAuthInfo.getCustomerId() != 0) {
			logger.info("已完成开户  请勿重复开户");
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("已完成开户  请勿重复开户");
			return ajaxResult;
		}

		if(loanUserAuthInfo == null){
			logger.info("未完成会员认证  请先认证");
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("未完成会员认证  请先认证");
			return ajaxResult;
		}

		// 通过电子合同服务开通安心签账户
		OpenAccountInfoBean bean = new OpenAccountInfoBean();
		bean.setIdentityNo(loanUserAuthInfo.getIdentity());
		bean.setIdentityType("0");
		bean.setSource(getSource());
		bean.setCustomerName(loanUserAuthInfo.getUsername());
		bean.setMobile(loanUserAuthInfo.getMobile());
		ResponseVo response = accountInfoService.openAccount(bean);
		if (!response.isSuccess()) {
			logger.info("安心签开户失败 具体原因: {}", response.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("安心签开户失败 具体原因: " + response.getMessage());
			return ajaxResult;
		}
		// 开户成功将合同服务客户Id写入loan_user_auth_info表
		Integer customerId = (Integer) response.getData();
		LoanUserAuthInfoDTO model = new LoanUserAuthInfoDTO();
		BeanUtils.copyProperties(loanUserAuthInfo, model);
		model.setCustomerId(customerId);
		loanUserAuthInfoService.update(model);

		// 上传电子印章
		CreateEsealBean createEsealBean = new CreateEsealBean();
		createEsealBean.setCustomerId(customerId);
		createEsealBean.setPath(path);
		// 查询平台所有的模板
		ResponseVo templateResponse = templateService.queryTemplateInfoByAppName(APP_NAME);
		if (!templateResponse.isSuccess()) {
			logger.info("电子合同服务查询模板失败 具体原因: {}", templateResponse.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("电子合同服务查询模板失败 具体原因: " + templateResponse.getMessage());
			return ajaxResult;
		}
		@SuppressWarnings("unchecked")
		List<TemplateVo> templates = (List<TemplateVo>) templateResponse.getData();
		StringBuilder templateIds = new StringBuilder();
		for (TemplateVo template : templates) {
			templateIds.append(template.getTemplateId()).append(",");
		}
		createEsealBean.setSource(getSource());
		createEsealBean.setTemplateId(templateIds.substring(0, templateIds.length() - 1));
		ResponseVo sealResponse = electronicSealService.createEseal(createEsealBean);
		if (!sealResponse.isSuccess()) {
			logger.info("电子合同上传印章失败 具体原因: {}", sealResponse.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("电子合同上传印章失败 具体原因: " + sealResponse.getMessage());
			return ajaxResult;
		}
		return ajaxResult;
	}

	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("rawtypes")
	public AjaxResult openOrEsealAccount(int userId, String path) {
		AjaxResult ajaxResult = new AjaxResult();
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		// 开通安心签账户前先确认是否开户
		if (loanUserAuthInfo != null && loanUserAuthInfo.getCustomerId() > 0) {
			//查询印章
			ajaxResult = getEseal(loanUserAuthInfo.getCustomerId());
			if(ajaxResult.getCode() != AjaxResultCode.OK.getCode()){
				return ajaxResult;
			}
			Map sealMap= (Map)ajaxResult.getData();
			if(sealMap == null || sealMap.isEmpty()){
				//仅上传印章
				return createEseal(path,loanUserAuthInfo.getCustomerId());
			}else {
				logger.info("已完成开户  请勿重复开户");
				ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
				ajaxResult.setMsg("已完成开户  请勿重复开户");
				return ajaxResult;
			}
		}

		if(loanUserAuthInfo == null){
			logger.info("未完成会员认证  请先认证");
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("未完成会员认证  请先认证");
			return ajaxResult;
		}

		ajaxResult = openAccount(loanUserAuthInfo);
		if(AjaxResultCode.OK.getCode() != ajaxResult.getCode()){
			return ajaxResult;
		}

		//更新后重新加载一次
		loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);

		//然后上传印章
		ajaxResult = createEseal(path,loanUserAuthInfo.getCustomerId());

		return ajaxResult;
	}


	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("rawtypes")
	public AjaxResult getStatusOpenAccount(int userId) {
		AjaxResult ajaxResult = new AjaxResult();
		int status = 0;
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		// 开通安心签账户前先确认是否开户
		if (loanUserAuthInfo != null && loanUserAuthInfo.getCustomerId() > 0) {
			//查询印章
			ajaxResult = getEseal(loanUserAuthInfo.getCustomerId());
			if(ajaxResult.getCode() != AjaxResultCode.OK.getCode()){
				status = -1;
			}
			Map sealMap= (Map)ajaxResult.getData();
			if(sealMap == null || sealMap.isEmpty()){
				status = 1;
			}else {
				status = 2;
			}
		}

		ajaxResult.setData(status);

		return ajaxResult;
	}


	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("rawtypes")
	public AjaxResult createEseal(int userId,String path) {
		AjaxResult ajaxResult = new AjaxResult();

		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		// 开通安心签账户前先确认是否开户
		if (loanUserAuthInfo != null && loanUserAuthInfo.getCustomerId() > 0) {

			//查询印章
			ajaxResult = getEseal(loanUserAuthInfo.getCustomerId());
			if(ajaxResult.getCode() != AjaxResultCode.OK.getCode()){
				return ajaxResult;
			}
			Map sealMap= (Map)ajaxResult.getData();
			if(sealMap == null || sealMap.isEmpty()){
				//仅上传印章
				return createEseal(path,loanUserAuthInfo.getCustomerId());
			}else {
				logger.info("已上传  请勿重复上传");
				ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
				ajaxResult.setMsg("已上传  请勿重复上传");
				return ajaxResult;
			}

		}else {

			if (loanUserAuthInfo == null) {
				logger.info("未完成会员认证  请先认证");
				ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
				ajaxResult.setMsg("未完成会员认证  请先认证");
				return ajaxResult;
			}else{
				logger.info("未开户  请先完成安心签开户");
				ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
				ajaxResult.setMsg("未开户  请先完成安心签开户");
				return ajaxResult;
			}
		}
	}


	@Transactional(rollbackFor = Exception.class)
	@SuppressWarnings("rawtypes")
	public AjaxResult openAccount(int userId) {
		AjaxResult ajaxResult = new AjaxResult();

		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		// 开通安心签账户前先确认是否开户
		if (loanUserAuthInfo != null && loanUserAuthInfo.getCustomerId() > 0) {

			logger.info("已完成开户  请勿重复开户");
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("已完成开户  请勿重复开户");
			return ajaxResult;

		}else {

			if (loanUserAuthInfo == null) {
				logger.info("未完成会员认证  请先认证");
				ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
				ajaxResult.setMsg("未完成会员认证  请先认证");
				return ajaxResult;
			}

			ajaxResult = openAccount(loanUserAuthInfo);
			if (AjaxResultCode.OK.getCode() != ajaxResult.getCode()) {
				return ajaxResult;
			}

			//更新后重新加载一次
			loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);

			return ajaxResult;
		}
	}


	public long createBorrowContract(int userId) {
		// 判断借款合同是否已生成 没有生成就先生成借款合同
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(userId);
		UserEnterpriseDTO userEnterpriseDTO = enterpriseService.loadEnterpriseByUserId(userId);
		UserAccountDTO userAccountDTO = userAccountService.loadById(userId);


		if (loanUserCreditInfoDTO != null && loanUserCreditInfoDTO.getContractId() != 0) {
			return loanUserCreditInfoDTO.getContractId();
		}
		CreateEcontractBean createEcontractBean = new CreateEcontractBean();
		createEcontractBean.setCustomerId(loanUserAuthInfo.getCustomerId());

		createEcontractBean.setSource(getSource());
		createEcontractBean.setTemplateId(Integer.parseInt(envService.getConfig("borrow.template.id")));
		createEcontractBean.setParameters(buildBorrowParam(userAccountDTO, loanUserCreditInfoDTO, userEnterpriseDTO));
		ResponseVo response = econtractService.createEcontract(createEcontractBean);
		if (!response.isSuccess()) {
			logger.info("创建借款合同失败 具体原因: {}", response.getMessage());
			return 0L;
		}
		Long contractId = (Long) response.getData();
		if (contractId == null) {
			logger.info("创建借款合同失败 具体原因: 强制转换出现异常");
			return 0L;
		}
		// 合同创建后将合同Id写入表loan_user_credit_info
		LoanUserCreditInfoDTO model = new LoanUserCreditInfoDTO();
		BeanUtils.copyProperties(loanUserCreditInfoDTO, model);
		model.setContractId(contractId);
		model.setProjectCode(BizUtils.getUUID());
		loanUserCreditInfoService.update(model);
		return contractId;
	}

	@SuppressWarnings("rawtypes")
	public AjaxResult anxinsendCaptcha(int userId) {
		AjaxResult ajaxResult = new AjaxResult();
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(userId);
		if (loanUserAuthInfo == null || loanUserAuthInfo.getCustomerId() == 0) {
			logger.info("ID为{}的用户不存在或者安心签未开户", userId);
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("ID为" + userId + "的用户不存在或者安心签未开户");
			return ajaxResult;
		}

		String smsTemplateId = "";
		if("product".equals(EnvService.getEnv())){
			//生产环境的时候
			smsTemplateId = envService.getConfig("contract.sms.template.id");
		}

		ResponseVo response = econtractService.sendCaptcha(new EcontractSignBean(loanUserAuthInfo.getCustomerId(),
				loanUserCreditInfoDTO.getProjectCode(), 0, smsTemplateId,getSource()));
		if (!response.isSuccess()) {
			logger.info("合同服务发送短信失败 具体原因: {}", response.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("合同服务发送短信失败 具体原因： " + response.getMessage());
			return ajaxResult;
		}
		return ajaxResult;
	}

	@SuppressWarnings("rawtypes")
	public AjaxResult signBorrowContract(final int userId, long contractId, String captchaCode) {
		AjaxResult ajaxResult = new AjaxResult();
		LoanUserAuthInfoModel loanUserAuthInfo = loanAuthCreditService.loadloanUserAuthInfoByUserId(userId);
		if (loanUserAuthInfo == null || loanUserAuthInfo.getCustomerId() == 0) {
			logger.info("ID为{}的用户不存在或者安心签未开户", userId);
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("ID为" + userId + "的用户不存在或者安心签未开户");
			return ajaxResult;
		}
		LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(userId);
		if (loanUserCreditInfoDTO == null || loanUserCreditInfoDTO.getContractId() != contractId
				|| loanUserCreditInfoDTO.getStatus() != LoanUserCreditStatusEnum.UNAUTHORIZED.getStatus()) {
			logger.info("ID为{}的用户未完成银行授信", userId);
			ajaxResult.setCode(112001);
			ajaxResult.setMsg("用户未完成银行授信");
			return ajaxResult;
		}

		EcontractCaptchaSignBean bean = new EcontractCaptchaSignBean();
		bean.setCustomerId(loanUserAuthInfo.getCustomerId());
		bean.setContractId(contractId);
		bean.setSource(getSource());
		bean.setCaptchCode(captchaCode);
		bean.setProjectCode(loanUserCreditInfoDTO.getProjectCode());
		bean.setSignature("甲方：(签章)");
		bean.setXaxisOffset(50f);
		bean.setYaxisOffset(-50f);
		bean.setSignLocationIp(WebContext.getRequest().getRemoteHost());
		ResponseVo response = econtractService.signEcontract(bean);
		if (!response.isSuccess()) {
			logger.info("客户签署合同失败 具体原因: {}", response.getMessage());
			String errorMessage = response.getMessage();
			if (errorMessage.contains("短信验证码不正确")) {
				ajaxResult.setCode(60030501);
				ajaxResult.setMsg("短信验证码不正确");
				return ajaxResult;
			}
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("客户签署合同失败 具体原因： " + response.getMessage());
			return ajaxResult;
		}
		LoanUserCreditInfoDTO model = new LoanUserCreditInfoDTO();
		BeanUtils.copyProperties(loanUserCreditInfoDTO, model);
		model.setStatus(LoanUserCreditStatusEnum.TO_CONFIRM.getStatus());
		loanUserCreditInfoService.update(model);

		// 获取银行客户经理
		List<LoanSmsContactsConfigDTO> loanSmsContactsConfigDTOList = loanSmsContactsConfigService.loadByProjectAndBizType(
				"fruit", LoanSmsBizTypeEnum.BANKER_PHONE.getType());
		// 短信发送模板
		String template = "【九创金服】请知悉，有新的客户签署借款合同，已转入资金方签署环节，请尽快登录平台处理完成，感谢您的支持！";

		// 短信发送默认为当前签名的用户
		String mobile = "";// 待发送的手机号
		for (LoanSmsContactsConfigDTO loanSms : loanSmsContactsConfigDTOList) {
			if (loanSms != null) {
				mobile = getCurrentConfig(loanSms);
				if (StringUtils.isNotBlank(mobile) && NumberUtil.isNumber(mobile)) {
					loanMessageService.sendSms(userId, mobile, template);
				}
			}
		}

		return ajaxResult;
	}

	/**
	 * 银行签署借款合同
	 * 
	 * @param contractId
	 */
	@Transactional(rollbackFor = RuntimeException.class)
	public void bankSignContract(long contractId, int userId) {
		LoanUserCreditInfoDTO loanUserCreditInfoDTO = loanUserCreditInfoService.loadByUserId(userId);
		if (loanUserCreditInfoDTO == null || loanUserCreditInfoDTO.getContractId() != contractId
				|| loanUserCreditInfoDTO.getStatus() != LoanUserCreditStatusEnum.TO_CONFIRM.getStatus()) {
			logger.info("数据异常 请核查");
			return;
		}

		String projectCode = envService.getConfig("borrow.project.code");
		String customerId = envService.getConfig("bank.customer.id");
		SignWithoutCaptchaBean bean = new SignWithoutCaptchaBean();
		bean.setContractId(contractId);
		bean.setCustomerId(Integer.parseInt(customerId));
		bean.setProjectCode(projectCode);
		bean.setSource(getSource());
		bean.setSignature("乙方：(签章)");
		bean.setXaxisOffset(50f);
		bean.setYaxisOffset(-50f);
		ResponseVo response = econtractService.signEcontractWithoutCaptcha(bean);
		if (!response.isSuccess()) {
			logger.info("客户签署合同失败 具体原因: {}", response.getMessage());
			return;
		}
		// 银行签署成功修改表loan_user_credit_info的status为已授信
		LoanUserCreditInfoDTO model = new LoanUserCreditInfoDTO();
		BeanUtils.copyProperties(loanUserCreditInfoDTO, model);
		model.setStatus(LoanUserCreditStatusEnum.PASS.getStatus());
		loanUserCreditInfoService.update(model);
	}

	private Map<String, String> buildBorrowParam(UserAccountDTO userAccountDTO, LoanUserCreditInfoDTO loanUserCreditInfoDTO,
			UserEnterpriseDTO userEnterpriseDTO) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("borrower", loanUserCreditInfoDTO.getUsername());
		map.put("identityNo", loanUserCreditInfoDTO.getIdentity());
		map.put("contractNo", loanUserCreditInfoDTO.getCtrNo());
		// 通讯地址
		StringBuilder address = new StringBuilder();
		address.append(metadataProvider.getCountryName(userEnterpriseDTO.getCountryId()));
		address.append(metadataProvider.getProvinceName(userEnterpriseDTO.getProvinceId()));
		address.append(metadataProvider.getCityName(userEnterpriseDTO.getCityId()));
		address.append(metadataProvider.getAreaName(userEnterpriseDTO.getDistrictId()));
		address.append(userEnterpriseDTO.getAddress());
		map.put("borrowerAddress", address.toString());
		// 联系电话
		map.put("borrowerPhone", loanUserCreditInfoDTO.getMobile());
		// 借款额度
		DecimalFormat df = new DecimalFormat();
		String style = "0.000";
		df.applyPattern(style);
		map.put("creditLineZh", MoneyUtil.toChinese(df.format(loanUserCreditInfoDTO.getCreditLine())));

		// 借款期限
		Calendar calendar = Calendar.getInstance();
		Date nowDate = new Date();
		calendar.setTime(nowDate);
		// 默认为当前时间后退三年
		calendar.add(Calendar.YEAR, 3);
		String expireYear = String.valueOf(calendar.get(Calendar.YEAR));
		String expireMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String expireDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		// 数据库总到期日
		calendar.clear();
		calendar.setTime(loanUserCreditInfoDTO.getExpireTime());

		System.out.println("calendarYear:" + calendar.get(Calendar.YEAR));
		if (calendar.get(Calendar.YEAR) > 1) {
			// 数据库中到日期不为空时
			expireYear = String.valueOf(calendar.get(Calendar.YEAR));
			expireMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
			expireDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		}

		map.put("expireYear", expireYear);
		map.put("expireMonth", expireMonth);
		map.put("expireDay", expireDay);

		//年利率
		double yearInterestRate = Double.valueOf(runtimeConfigurationService.getConfig(RuntimeConfigurationService.RUNTIME_CONFIG_PROJECT_PORTAL, BaseRuntimeConfig.PERFORMANCE_RATE));
		String yearInterestRateStr = String.valueOf(yearInterestRate*100);
		map.put("performanceRate",yearInterestRateStr);

		// 银行账户
		map.put("savingAccount", "");// 默认为空
		if (StringUtils.isNotBlank(loanUserCreditInfoDTO.getCtrBankNo())) {
			map.put("savingAccount", loanUserCreditInfoDTO.getCtrBankNo());
		}

		map.put("guaranteeContractNo", String.valueOf(loanUserCreditInfoDTO.getInsureCtrNo()));
		// 送达地址
		map.put("noteDeliveryAddress", address.toString());
		// 邮编
		String zipCode = "0086";// 默认中国国际邮编
		AreaDTO areaDTO = areaService.loadById(userEnterpriseDTO.getDistrictId());
		CityDTO cityDTO = cityService.loadById(userEnterpriseDTO.getCityId());
		// 保证人合同ID
		if (areaDTO != null) {
			zipCode = String.valueOf(areaDTO.getZip());
		} else {
			if (cityDTO != null) {
				zipCode = String.valueOf(cityDTO.getZip());
			}
		}
		map.put("noteDeliveryZipcode", zipCode);

		// 收件人
		map.put("noteReceiver", loanUserCreditInfoDTO.getUsername());
		map.put("noteReceiverPhone", loanUserCreditInfoDTO.getMobile());
		// 邮箱
		map.put("noteReceiverEmail", "/");

		// 其他方式
		map.put("noteOtherType", "/");

		// 借款人签章时间
		calendar.clear();
		calendar.setTime(nowDate);
		map.put("borrowerSealYear", String.valueOf(calendar.get(Calendar.YEAR)));
		map.put("borrowerSealMonth", String.valueOf(calendar.get(Calendar.MONTH) + 1));// 月份从0开始的
		map.put("borrowerSealDay", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
		// 放款签章时间
		map.put("lenderSealYear", String.valueOf(calendar.get(Calendar.YEAR)));
		map.put("lenderSealMonth", String.valueOf(calendar.get(Calendar.MONTH) + 1));// 月份从0开始的
		map.put("lenderSealDay", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

		return map;
	}

	/**
	 * 获取短信发送手机号
	 * 
	 * @param config
	 */
	private String getCurrentConfig(LoanSmsContactsConfigDTO config) {
		String value;
		if ("product".equals(EnvService.getEnv())) {
			value = config.getProduct();
		} else if ("beta".equals(EnvService.getEnv())) {
			value = config.getBeta();
		} else if ("dev".equals(EnvService.getEnv())) {
			value = config.getDev();
		} else {
			value = config.getAlpha();
		}
		return value;
	}


	private AjaxResult openAccount( LoanUserAuthInfoModel loanUserAuthInfo) {
		AjaxResult ajaxResult = new AjaxResult();
		// 通过电子合同服务开通安心签账户
		OpenAccountInfoBean bean = new OpenAccountInfoBean();
		bean.setIdentityNo(loanUserAuthInfo.getIdentity());
		bean.setIdentityType("0");
		bean.setCustomerName(loanUserAuthInfo.getUsername());
		bean.setSource(getSource());
		bean.setMobile(loanUserAuthInfo.getMobile());
		ResponseVo response = accountInfoService.openAccount(bean);
		if (!response.isSuccess()) {
			logger.info("安心签开户失败 具体原因: {}", response.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("安心签开户失败 具体原因: " + response.getMessage());
			return ajaxResult;
		}
		// 开户成功将合同服务客户Id写入loan_user_auth_info表
		Integer customerId = (Integer) response.getData();
		LoanUserAuthInfoDTO model = new LoanUserAuthInfoDTO();
		BeanUtils.copyProperties(loanUserAuthInfo, model);
		model.setCustomerId(customerId);
		loanUserAuthInfoService.update(model);
		return ajaxResult;
	}

	public AjaxResult createEseal(String path, Integer customerId) {

		AjaxResult ajaxResult = new AjaxResult();

		// 上传电子印章
		CreateEsealBean createEsealBean = new CreateEsealBean();
		createEsealBean.setCustomerId(customerId);
		createEsealBean.setPath(path);
		// 查询平台所有的模板
		ResponseVo templateResponse = templateService.queryTemplateInfoByAppName(APP_NAME);
		if (!templateResponse.isSuccess()) {
			logger.info("电子合同服务查询模板失败 具体原因: {}", templateResponse.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("电子合同服务查询模板失败 具体原因: " + templateResponse.getMessage());
			return ajaxResult;
		}
		@SuppressWarnings("unchecked")
		List<TemplateVo> templates = (List<TemplateVo>) templateResponse.getData();
		StringBuilder templateIds = new StringBuilder();
		for (TemplateVo template : templates) {
			templateIds.append(template.getTemplateId()).append(",");
		}
		createEsealBean.setSource(getSource());
		createEsealBean.setTemplateId(templateIds.substring(0, templateIds.length() - 1));
		ResponseVo sealResponse = electronicSealService.createEseal(createEsealBean);
		if (!sealResponse.isSuccess()) {
			logger.info("电子合同上传印章失败 具体原因: {}", sealResponse.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("电子合同上传印章失败 具体原因: " + sealResponse.getMessage());
			return ajaxResult;
		}
		return ajaxResult;
	}

	public AjaxResult getEseal(Integer customerId) {

		AjaxResult ajaxResult = new AjaxResult();

		// 电子印章
		CreateEsealBean createEsealBean = new CreateEsealBean();
		createEsealBean.setCustomerId(customerId);
		// 查询平台所有的模板
		ResponseVo templateResponse = templateService.queryTemplateInfoByAppName(APP_NAME);
		if (!templateResponse.isSuccess()) {
			logger.info("电子合同服务查询模板失败 具体原因: {}", templateResponse.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("电子合同服务查询模板失败 具体原因: " + templateResponse.getMessage());
			return ajaxResult;
		}
		@SuppressWarnings("unchecked")
		List<TemplateVo> templates = (List<TemplateVo>) templateResponse.getData();
		StringBuilder templateIds = new StringBuilder();
		for (TemplateVo template : templates) {
			templateIds.append(template.getTemplateId()).append(",");
		}
		createEsealBean.setSource(getSource());
		createEsealBean.setTemplateId(templateIds.substring(0, templateIds.length() - 1));
		ResponseVo sealResponse = electronicSealService.getEseal(createEsealBean);
		if (!sealResponse.isSuccess()) {
			logger.info("电子合同查询印章失败 具体原因: {}", sealResponse.getMessage());
			ajaxResult.setCode(AjaxResultCode.REQUEST_INVALID.getCode());
			ajaxResult.setMsg("电子合同查询印章失败 具体原因: " + sealResponse.getMessage());
			return ajaxResult;
		}
		ajaxResult.setData(sealResponse.getData());
		return ajaxResult;
	}


	private String getSource()  {
		return  envService.getConfig("contract.source");
	}
}
