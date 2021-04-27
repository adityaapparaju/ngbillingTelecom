package com.ngbilling.core.server.service.util.impl;

import java.util.*;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.ngbilling.core.payload.request.metafield.DataType;
import com.ngbilling.core.payload.request.metafield.MetaFieldType;
import com.ngbilling.core.payload.request.order.ApplyToOrder;
import com.ngbilling.core.payload.request.order.OrderStatusFlag;
import com.ngbilling.core.payload.request.util.NotificationMediumType;
import com.ngbilling.core.server.persistence.dao.invoice.InvoiceDeliveryMethodDAO;
import com.ngbilling.core.server.persistence.dao.metafield.MetaFieldDAO;
import com.ngbilling.core.server.persistence.dao.metafield.ValidationRuleDAO;
import com.ngbilling.core.server.persistence.dao.notification.NotificationMessageDAO;
import com.ngbilling.core.server.persistence.dao.notification.NotificationMessageSectionDAO;
import com.ngbilling.core.server.persistence.dao.order.OrderChangeStatusDAO;
import com.ngbilling.core.server.persistence.dao.order.OrderPeriodDAO;
import com.ngbilling.core.server.persistence.dao.order.OrderStatusDAO;
import com.ngbilling.core.server.persistence.dao.payment.PaymentMethodDAO;
import com.ngbilling.core.server.persistence.dao.payment.PaymentMethodTemplateDAO;
import com.ngbilling.core.server.persistence.dao.pluggableTask.PluggableTaskDAO;
import com.ngbilling.core.server.persistence.dao.pluggableTask.PluggableTaskParameterDAO;
import com.ngbilling.core.server.persistence.dao.pluggableTask.PluggableTaskTypeDAO;
import com.ngbilling.core.server.persistence.dao.process.BillingProcessConfigurationDAO;
import com.ngbilling.core.server.persistence.dao.report.ReportDAO;
import com.ngbilling.core.server.persistence.dao.user.AccountTypeDAO;
import com.ngbilling.core.server.persistence.dao.util.CurrencyDAO;
import com.ngbilling.core.server.persistence.dao.util.EnumerationDAO;
import com.ngbilling.core.server.persistence.dao.util.EnumerationValueDAO;
import com.ngbilling.core.server.persistence.dao.util.InternationalDescriptionDAO;
import com.ngbilling.core.server.persistence.dao.util.JbillingTableDAO;
import com.ngbilling.core.server.persistence.dao.util.LanguageDAO;
import com.ngbilling.core.server.persistence.dao.util.PreferenceDAO;
import com.ngbilling.core.server.persistence.dto.invoice.InvoiceDeliveryMethodDTO;
import com.ngbilling.core.server.persistence.dto.metafield.MetaField;
import com.ngbilling.core.server.persistence.dto.metafield.ValidationRule;
import com.ngbilling.core.server.persistence.dto.notification.NotificationMessageDTO;
import com.ngbilling.core.server.persistence.dto.notification.NotificationMessageLineDTO;
import com.ngbilling.core.server.persistence.dto.notification.NotificationMessageSectionDTO;
import com.ngbilling.core.server.persistence.dto.notification.NotificationMessageTypeDTO;
import com.ngbilling.core.server.persistence.dto.order.OrderChangeStatusDTO;
import com.ngbilling.core.server.persistence.dto.order.OrderPeriodDTO;
import com.ngbilling.core.server.persistence.dto.order.OrderStatusDTO;
import com.ngbilling.core.server.persistence.dto.payment.PaymentMethodDTO;
import com.ngbilling.core.server.persistence.dto.payment.PaymentMethodTemplateDTO;
import com.ngbilling.core.server.persistence.dto.pluggableTask.PluggableTaskDTO;
import com.ngbilling.core.server.persistence.dto.pluggableTask.PluggableTaskParameterDTO;
import com.ngbilling.core.server.persistence.dto.pluggableTask.PluggableTaskTypeDTO;
import com.ngbilling.core.server.persistence.dto.process.BillingProcessConfigurationDTO;
import com.ngbilling.core.server.persistence.dto.process.PeriodUnitDTO;
import com.ngbilling.core.server.persistence.dto.process.ProratingType;
import com.ngbilling.core.server.persistence.dto.report.ReportDTO;
import com.ngbilling.core.server.persistence.dto.user.AccountTypeDTO;
import com.ngbilling.core.server.persistence.dto.user.CompanyDTO;
import com.ngbilling.core.server.persistence.dto.user.MainSubscriptionDTO;
import com.ngbilling.core.server.persistence.dto.user.UserDTO;
import com.ngbilling.core.server.persistence.dto.util.CurrencyDTO;
import com.ngbilling.core.server.persistence.dto.util.EntityType;
import com.ngbilling.core.server.persistence.dto.util.EnumerationDTO;
import com.ngbilling.core.server.persistence.dto.util.EnumerationValueDTO;
import com.ngbilling.core.server.persistence.dto.util.InternationalDescriptionDTO;
import com.ngbilling.core.server.persistence.dto.util.InternationalDescriptionId;
import com.ngbilling.core.server.persistence.dto.util.JbillingTable;
import com.ngbilling.core.server.persistence.dto.util.LanguageDTO;
import com.ngbilling.core.server.persistence.dto.util.PreferenceDTO;
import com.ngbilling.core.server.persistence.dto.util.PreferenceTypeDTO;
import com.ngbilling.core.server.service.util.UtilService;
import com.ngbilling.core.server.util.ServerConstants;
import com.ngbilling.core.server.validator.metafield.ValidationRuleType;

@Service
public class UtilServiceImpl implements UtilService{

	@Autowired
	private LanguageDAO languageDAO;
	
	@Autowired
	private CurrencyDAO currencyDAO;
	
	@Autowired
	private JbillingTableDAO jbillingTableDAO;
	
	@Autowired
	private OrderStatusDAO orderStatusDAO;
	
	@Autowired
	private OrderPeriodDAO orderPeriodDAO;
	
	@Autowired
	private OrderChangeStatusDAO orderChangeStatusDAO;
	
	@Autowired
    private AccountTypeDAO accountTypeDAO;
    
	@Autowired
    private PaymentMethodDAO paymentMethodTaskDAO;
    
    @Autowired
    private PluggableTaskDAO pluggableTaskDAO;
    
    @Autowired
    private PluggableTaskParameterDAO pluggableTaskParameterDAO;
    
    
    @Autowired
    private PluggableTaskTypeDAO pluggableTaskTypeDAO;
    
    @Autowired
    private MetaFieldDAO metaFieldDAO;
    
    @Autowired
    private PaymentMethodTemplateDAO paymentMethodTemplateDAO;
    
    @Autowired
    private ValidationRuleDAO validationRuleDAO;
    
    @Autowired
    private InternationalDescriptionDAO internationalDescriptionDAO;
    
    @Autowired
    private EnumerationDAO enumerationDAO;
    
    @Autowired
    private EnumerationValueDAO enumerationValueDAO;
    
    @Autowired
    private PreferenceDAO preferenceDAO;
    
    @Autowired
    private NotificationMessageDAO notificationMessageDAO;
    
    @Autowired
    private BillingProcessConfigurationDAO billingProcessConfigurationDAO;
    
    @Autowired
    private NotificationMessageSectionDAO notificationMessageSectionDAO;
    
    @Autowired
    private ReportDAO reportDAO;

    @Autowired
    private PaymentMethodDAO paymentMethodDAO;
    
    @Autowired
    private InvoiceDeliveryMethodDAO invoiceDeliveryMethodDAO;

	@Autowired
	private MessageSource messageSource;


	@Override
	public LanguageDTO findByLanguageCode(String code) {
		return languageDAO.findByCode(code);
	}

	@Override
	public CurrencyDTO findByCurrencyCode(String code) {
		return currencyDAO.findByCode(code);
	}

	@Override
	public JbillingTable findByName(String tableName) {
		// TODO Auto-generated method stub
		return jbillingTableDAO.findByName(tableName);
	}

	@Override
	public void initEntityDefault(CompanyDTO company, UserDTO rootUser, LanguageDTO language, Locale locale) {
		OrderStatusDTO invoiceOS = new OrderStatusDTO();
		invoiceOS.setOrderStatusFlag(OrderStatusFlag.INVOICE);
		invoiceOS.setEntity(company);
		invoiceOS.setDescription("Active", language.getId());
		orderStatusDAO.save(invoiceOS);
		
		invoiceOS = new OrderStatusDTO();
		invoiceOS.setOrderStatusFlag(OrderStatusFlag.FINISHED);      
		invoiceOS.setEntity(company);
		invoiceOS.setDescription("Finished", language.getId());
		orderStatusDAO.save(invoiceOS);
		invoiceOS = new OrderStatusDTO();
		invoiceOS.setOrderStatusFlag(OrderStatusFlag.NOT_INVOICE);
		invoiceOS.setEntity(company);
		invoiceOS.setDescription("Suspended", language.getId());
		orderStatusDAO.save(invoiceOS);
		invoiceOS = new OrderStatusDTO();
		invoiceOS.setOrderStatusFlag(OrderStatusFlag.SUSPENDED_AGEING);
		invoiceOS.setEntity(company);
		invoiceOS.setDescription("Suspended ageing(auto)", language.getId());
		orderStatusDAO.save(invoiceOS);	        

		company.getCurrency().getEntities_1().add(company);
		
		OrderPeriodDTO orderPeriodDTO = new OrderPeriodDTO(company, new PeriodUnitDTO(ServerConstants.PERIOD_UNIT_MONTH), 1);
    	orderPeriodDTO = orderPeriodDAO.save(orderPeriodDTO);
    	orderPeriodDTO.setDescription("Monthly");
    	
    	
    	Integer orderChangeStatusId = orderChangeStatusDAO.getMaxStatusId();
    	OrderChangeStatusDTO orderChangeStatusDTO = new OrderChangeStatusDTO();
    	orderChangeStatusDTO.setCompany(company);
    	orderChangeStatusDTO.setApplyToOrder(ApplyToOrder.YES);
    	orderChangeStatusDTO.setDeleted(0);
    	orderChangeStatusDTO.setOrder(1);
    	orderChangeStatusDTO.setId(orderChangeStatusId+1);
    	orderChangeStatusDTO.setDescription(getMessage("order.change.status.default.apply",locale),ServerConstants.LANGUAGE_ENGLISH_ID);
    	orderChangeStatusDAO.save(orderChangeStatusDTO);
    	
    	MainSubscriptionDTO mainSubscriptionDTO = new MainSubscriptionDTO(orderPeriodDTO, Integer.valueOf(1));
    	AccountTypeDTO accountTypeDTO = new AccountTypeDTO();
    	accountTypeDTO.setCompany(company);
    	accountTypeDTO.setBillingCycle(mainSubscriptionDTO);
    	accountTypeDTO.setDescription("description", ServerConstants.LANGUAGE_ENGLISH_ID, getMessage("default.account.type.name",locale));
    	accountTypeDTO = accountTypeDAO.save(accountTypeDTO);
    	
    	
    	PaymentMethodDTO paymentMethodDTO=paymentMethodDAO.findById(ServerConstants.PAYMENT_METHOD_CHEQUE).get();
    	paymentMethodDTO.getEntities().add(company);
    	
    	PaymentMethodDTO paymentMethodVisaDTO=paymentMethodDAO.findById(ServerConstants.PAYMENT_METHOD_VISA).get();
    	paymentMethodVisaDTO.getEntities().add(company);
    	
    	PaymentMethodDTO paymentMethodMasterCardDTO=paymentMethodDAO.findById(ServerConstants.PAYMENT_METHOD_MASTERCARD).get();
    	paymentMethodMasterCardDTO.getEntities().add(company);
    	
    	InvoiceDeliveryMethodDTO InvoiceDeliveryMethodDTOEmail=invoiceDeliveryMethodDAO.findById(ServerConstants.D_METHOD_EMAIL).get();
    	InvoiceDeliveryMethodDTOEmail.getEntities().add(company);
    	
    	InvoiceDeliveryMethodDTO InvoiceDeliveryMethodDTOPaper=invoiceDeliveryMethodDAO.findById(ServerConstants.D_METHOD_PAPER).get();
    	InvoiceDeliveryMethodDTOPaper.getEntities().add(company);
    	
    	InvoiceDeliveryMethodDTO InvoiceDeliveryMethodDTOEmailAndPaper=invoiceDeliveryMethodDAO.findById(ServerConstants.D_METHOD_EMAIL_AND_PAPER).get();
    	InvoiceDeliveryMethodDTOEmailAndPaper.getEntities().add(company);
    	
    	/* Reports code is blocked and added later
    	List<ReportDTO> reportDTOList =reportDAO.findAll();
    	for (ReportDTO reportDTO : reportDTOList)
    	{
    		//company.getReports().add(reportDTO);
    	}*/
    	
    	BillingProcessConfigurationDTO billingProcessConfigurationDTO = new BillingProcessConfigurationDTO();
    	billingProcessConfigurationDTO.setEntity(company);
    	billingProcessConfigurationDTO.setGenerateReport(1);
    	billingProcessConfigurationDTO.setRetries(0);
    	billingProcessConfigurationDTO.setDaysForRetry(1);
    	billingProcessConfigurationDTO.setDaysForReport(3);
    	billingProcessConfigurationDTO.setReviewStatus(1);
    	billingProcessConfigurationDTO.setDueDateUnitId(1);
    	billingProcessConfigurationDTO.setDueDateValue(1);
    	billingProcessConfigurationDTO.setOnlyRecurring(1);
    	billingProcessConfigurationDTO.setInvoiceDateProcess(0);
    	billingProcessConfigurationDTO.setMaximumPeriods(99);
    	billingProcessConfigurationDTO.setAutoPaymentApplication(1);
    	billingProcessConfigurationDTO.setProratingType(ProratingType.PRORATING_AUTO_OFF);
    	billingProcessConfigurationDTO.setPeriodUnit(new PeriodUnitDTO(ServerConstants.PERIOD_UNIT_MONTH));
    	billingProcessConfigurationDTO.setNextRunDate(new DateTime().plusMonths(1).toDate());
    	billingProcessConfigurationDAO.save(billingProcessConfigurationDTO);
    	
    	PluggableTaskDTO paymentPluggableTaskDTO = new PluggableTaskDTO();
    	paymentPluggableTaskDTO.setEntityId(company.getId());
    	paymentPluggableTaskDTO.setProcessingOrder(1);
    	paymentPluggableTaskDTO.setType(new PluggableTaskTypeDTO(21));
    	paymentPluggableTaskDTO = pluggableTaskDAO.save(paymentPluggableTaskDTO);
    	PluggableTaskParameterDTO pluggableTaskParameterDTO = new PluggableTaskParameterDTO("all", "yes", paymentPluggableTaskDTO);
    	pluggableTaskParameterDAO.save(pluggableTaskParameterDTO);
    	
    	PluggableTaskDTO emailPluggableTaskDTO = new PluggableTaskDTO();
    	emailPluggableTaskDTO.setEntityId(company.getId());
    	emailPluggableTaskDTO.setProcessingOrder(1);
    	emailPluggableTaskDTO.setType(new PluggableTaskTypeDTO(9));
    	Set<PluggableTaskParameterDTO> pluggableTaskParameterDTOSet = new HashSet<>();
    	pluggableTaskParameterDTOSet.add(new PluggableTaskParameterDTO("smtp_server", ""));
    	pluggableTaskParameterDTOSet.add(new PluggableTaskParameterDTO("port", ""));
    	pluggableTaskParameterDTOSet.add(new PluggableTaskParameterDTO("ssl_auth", "false"));
    	pluggableTaskParameterDTOSet.add(new PluggableTaskParameterDTO("tls", "false"));
    	pluggableTaskParameterDTOSet.add(new PluggableTaskParameterDTO("username", ""));
    	pluggableTaskParameterDTOSet.add(new PluggableTaskParameterDTO("password", ""));
    	emailPluggableTaskDTO.setParameters(pluggableTaskParameterDTOSet);
    	emailPluggableTaskDTO = pluggableTaskDAO.save(emailPluggableTaskDTO);
    	    	
    	
    	updateParametersForTask(emailPluggableTaskDTO);
    	
    	PluggableTaskDTO notificationPluggableTaskDTO = new PluggableTaskDTO();
    	notificationPluggableTaskDTO.setEntityId(company.getId());
    	notificationPluggableTaskDTO.setProcessingOrder(2);
    	notificationPluggableTaskDTO.setType(new PluggableTaskTypeDTO(12));
    	Set<PluggableTaskParameterDTO> notificationPluggableTaskParameterDTOSet = new HashSet<>();
    	notificationPluggableTaskParameterDTOSet.add(new PluggableTaskParameterDTO("design", "simple_invoice_b2b"));
    	notificationPluggableTaskDTO.setParameters(notificationPluggableTaskParameterDTOSet);
    	notificationPluggableTaskDTO = pluggableTaskDAO.save(notificationPluggableTaskDTO);
    	
    	updateParametersForTask(emailPluggableTaskDTO);
    	
    	
    	PluggableTaskDTO pluggableTaskBasicLineTotalTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBasicLineTotalTaskDTO.setEntityId(company.getId());
    	pluggableTaskBasicLineTotalTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.pluggableTask.BasicLineTotalTask"));
    	pluggableTaskBasicLineTotalTaskDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskBasicLineTotalTaskDTO);
    	
    	
    	PluggableTaskDTO pluggableTaskCalculateDueDateDTO = new PluggableTaskDTO();
    	pluggableTaskCalculateDueDateDTO.setEntityId(company.getId());
    	pluggableTaskCalculateDueDateDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.pluggableTask.CalculateDueDate"));
    	pluggableTaskCalculateDueDateDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskCalculateDueDateDTO);

    	PluggableTaskDTO pluggableTaskBasicCompositionTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBasicCompositionTaskDTO.setEntityId(company.getId());
    	pluggableTaskBasicCompositionTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.pluggableTask.BasicCompositionTask"));
    	pluggableTaskBasicCompositionTaskDTO.setProcessingOrder(2);
    	pluggableTaskDAO.save(pluggableTaskBasicCompositionTaskDTO);

    	PluggableTaskDTO pluggableTaskBasicOrderFilterTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBasicOrderFilterTaskDTO.setEntityId(company.getId());
    	pluggableTaskBasicOrderFilterTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.pluggableTask.BasicOrderFilterTask"));
    	pluggableTaskBasicOrderFilterTaskDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskBasicOrderFilterTaskDTO);

    	PluggableTaskDTO pluggableTaskBasicInvoiceFilterTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBasicInvoiceFilterTaskDTO.setEntityId(company.getId());
    	pluggableTaskBasicInvoiceFilterTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.pluggableTask.BasicInvoiceFilterTask"));
    	pluggableTaskBasicInvoiceFilterTaskDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskBasicInvoiceFilterTaskDTO);
    	
    	
    	PluggableTaskDTO pluggableTaskBasicOrderPeriodTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBasicOrderPeriodTaskDTO.setEntityId(company.getId());
    	pluggableTaskBasicOrderPeriodTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.pluggableTask.BasicOrderPeriodTask"));
    	pluggableTaskBasicOrderPeriodTaskDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskBasicOrderPeriodTaskDTO);
    	PluggableTaskDTO pluggableTaskBasicPaymentInfoTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBasicPaymentInfoTaskDTO.setEntityId(company.getId());
    	pluggableTaskBasicPaymentInfoTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.pluggableTask.BasicPaymentInfoTask"));
    	pluggableTaskBasicPaymentInfoTaskDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskBasicPaymentInfoTaskDTO);
    	PluggableTaskDTO pluggableTaskNoAsyncParametersDTO = new PluggableTaskDTO();
    	pluggableTaskNoAsyncParametersDTO.setEntityId(company.getId());
    	pluggableTaskNoAsyncParametersDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.payment.NoAsyncParameters"));
    	pluggableTaskNoAsyncParametersDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskNoAsyncParametersDTO);
    	PluggableTaskDTO pluggableTaskBasicItemManagerDTO = new PluggableTaskDTO();
    	pluggableTaskBasicItemManagerDTO.setEntityId(company.getId());
    	pluggableTaskBasicItemManagerDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.item.BasicItemManager"));
    	pluggableTaskBasicItemManagerDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskBasicItemManagerDTO);
    	PluggableTaskDTO pluggableTaskDynamicBalanceManagerTaskDTO = new PluggableTaskDTO();
    	pluggableTaskDynamicBalanceManagerTaskDTO.setEntityId(company.getId());
    	pluggableTaskDynamicBalanceManagerTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.user.DynamicBalanceManagerTask"));
    	pluggableTaskDynamicBalanceManagerTaskDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskDynamicBalanceManagerTaskDTO);
    	PluggableTaskDTO pluggableTaskBillingProcessTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBillingProcessTaskDTO.setEntityId(company.getId());
    	pluggableTaskBillingProcessTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.billing.BillingProcessTask"));
    	pluggableTaskBillingProcessTaskDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskBillingProcessTaskDTO);
    	PluggableTaskDTO pluggableTaskAgeingProcessTaskDTO = new PluggableTaskDTO();
    	pluggableTaskAgeingProcessTaskDTO.setEntityId(company.getId());
    	pluggableTaskAgeingProcessTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.process.AgeingProcessTask"));
    	pluggableTaskAgeingProcessTaskDTO.setProcessingOrder(2);
    	pluggableTaskDAO.save(pluggableTaskAgeingProcessTaskDTO);
    	PluggableTaskDTO pluggableTaskBasicAgeingTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBasicAgeingTaskDTO.setEntityId(company.getId());
    	pluggableTaskBasicAgeingTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.process.BasicAgeingTask"));
    	pluggableTaskBasicAgeingTaskDTO.setProcessingOrder(1);
    	pluggableTaskDAO.save(pluggableTaskBasicAgeingTaskDTO);
    	PluggableTaskDTO pluggableTaskBasicBillingProcessFilterTaskDTO = new PluggableTaskDTO();
    	pluggableTaskBasicBillingProcessFilterTaskDTO.setEntityId(company.getId());
    	pluggableTaskBasicBillingProcessFilterTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.process.BasicBillingProcessFilterTask"));
    	pluggableTaskBasicBillingProcessFilterTaskDTO.setProcessingOrder(2);
    	pluggableTaskDAO.save(pluggableTaskBasicBillingProcessFilterTaskDTO);
    	PluggableTaskDTO pluggableTaskCreateOrderForResellerTaskDTO = new PluggableTaskDTO();
    	pluggableTaskCreateOrderForResellerTaskDTO.setEntityId(company.getId());
    	pluggableTaskCreateOrderForResellerTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.order.CreateOrderForResellerTask"));
    	pluggableTaskCreateOrderForResellerTaskDTO.setProcessingOrder(2);
    	pluggableTaskDAO.save(pluggableTaskCreateOrderForResellerTaskDTO);
    	PluggableTaskDTO pluggableTaskDeleteResellerOrderTaskDTO = new PluggableTaskDTO();
    	pluggableTaskDeleteResellerOrderTaskDTO.setEntityId(company.getId());
    	pluggableTaskDeleteResellerOrderTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.invoice.DeleteResellerOrderTask"));
    	pluggableTaskDeleteResellerOrderTaskDTO.setProcessingOrder(3);
    	pluggableTaskDAO.save(pluggableTaskDeleteResellerOrderTaskDTO);
    	PluggableTaskDTO pluggableTaskOrderChangeApplyOrderStatusTaskDTO = new PluggableTaskDTO();
    	pluggableTaskOrderChangeApplyOrderStatusTaskDTO.setEntityId(company.getId());
    	pluggableTaskOrderChangeApplyOrderStatusTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.order.OrderChangeApplyOrderStatusTask"));
    	pluggableTaskOrderChangeApplyOrderStatusTaskDTO.setProcessingOrder(7);
    	pluggableTaskDAO.save(pluggableTaskOrderChangeApplyOrderStatusTaskDTO);
    	
    	PluggableTaskDTO pluggableTaskTestNotificationTaskDTO = new PluggableTaskDTO();
    	pluggableTaskTestNotificationTaskDTO.setEntityId(company.getId());
    	pluggableTaskTestNotificationTaskDTO.setType(pluggableTaskTypeDAO.findByClassName("com.ngbilling.core.server.persistence.dto.notification.TestNotificationTask"));
    	pluggableTaskTestNotificationTaskDTO.setProcessingOrder(3);
    	Set<PluggableTaskParameterDTO> notificationParameter = new HashSet<>();
    	notificationParameter.add(new PluggableTaskParameterDTO("from", "admin@jbilling.com"));
    	pluggableTaskTestNotificationTaskDTO.setParameters(notificationParameter);
    	pluggableTaskDAO.save(pluggableTaskTestNotificationTaskDTO);
    	
    	

        updateParametersForTask(pluggableTaskTestNotificationTaskDTO);
        
        
        JbillingTable jBillingTable = jbillingTableDAO.findByName(ServerConstants.TABLE_ENTITY);

        
        PreferenceDTO preferenceDTO = new PreferenceDTO();
    	preferenceDTO.setJbillingTable(jBillingTable);
    	preferenceDTO.setForeignId(company.getId());
    	preferenceDTO.setValue(1);
    	PreferenceTypeDTO preferenceTypeDTO = new PreferenceTypeDTO();
    	preferenceTypeDTO.setId(ServerConstants.PREFERENCE_SHOW_NOTE_IN_INVOICE);
    	preferenceDTO.setPreferenceType(preferenceTypeDTO);
    	preferenceDAO.save(preferenceDTO);
    	
    	PreferenceDTO preferenceInvoicePrefixDTO = new PreferenceDTO();
    	preferenceInvoicePrefixDTO.setJbillingTable(jBillingTable);
    	preferenceInvoicePrefixDTO.setForeignId(company.getId());
    	preferenceInvoicePrefixDTO.setValue(0);
    	PreferenceTypeDTO preferenceTypeInvoicePrefixDTO = new PreferenceTypeDTO();
    	preferenceTypeInvoicePrefixDTO.setId(ServerConstants.PREFERENCE_INVOICE_PREFIX);
    	preferenceInvoicePrefixDTO.setPreferenceType(preferenceTypeInvoicePrefixDTO);
    	preferenceDAO.save(preferenceInvoicePrefixDTO);
    	
    	PreferenceDTO preferenceInvoiceNumberDTO = new PreferenceDTO();
    	preferenceInvoiceNumberDTO.setJbillingTable(jBillingTable);
    	preferenceInvoiceNumberDTO.setForeignId(company.getId());
    	preferenceInvoiceNumberDTO.setValue(0);
    	PreferenceTypeDTO preferenceTypeInvoiceNumberDTO = new PreferenceTypeDTO();
    	preferenceTypeInvoiceNumberDTO.setId(ServerConstants.PREFERENCE_INVOICE_PREFIX);
    	preferenceInvoiceNumberDTO.setPreferenceType(preferenceTypeInvoiceNumberDTO);
    	preferenceDAO.save(preferenceInvoiceNumberDTO);
    	
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_INVOICE_EMAIL, "signup.notification.email.title", "signup.notification.email", company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_USER_REACTIVATED, "signup.notification.user.reactivated.title", "signup.notification.user.reactivated",company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_USER_OVERDUE, "signup.notification.overdue.title", "signup.notification.overdue",company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_ORDER_EXPIRE_1, "signup.notification.order.expire.1.title", "signup.notification.order.expire.1",company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_PAYMENT_SUCCESS, "signup.notification.payment.success.title", "signup.notification.payment.success",company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_PAYMENT_FAILED, "signup.notification.payment.failed.title", "signup.notification.payment.failed",company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_INVOICE_REMINDER, "signup.notification.invoice.reminder.title", "signup.notification.invoice.reminder",company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_CREDIT_CARD_UPDATE, "signup.notification.credit.card.update.title", "signup.notification.credit.card.update",company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_LOST_PASSWORD, "signup.notification.lost.password.update.title", "signup.notification.lost.password.update",company, language,locale);
    	createNotificationMessage(ServerConstants.NOTIFICATION_TYPE_INITIAL_CREDENTIALS, "signup.notification.initial.credentials.update.title", "signup.notification.initial.credentials.update",company, language,locale);

    	 PaymentMethodTemplateDTO paymentMethodTemplateCardDTO = paymentMethodTemplateDAO.findByName(ServerConstants.PAYMENT_CARD);
      	MetaField metaField = new MetaField();
      	metaField.setEntity(company);
      	metaField.setName("cc.cardholder.name");
      	metaField.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaField.setDataType(DataType.STRING);
      	metaField.setDisabled(false);
      	metaField.setMandatory(true);
      	metaField.setPrimary(true);
      	metaField.setDisplayOrder(1);
      	metaField.setFieldUsage(MetaFieldType.TITLE);
      	metaField = metaFieldDAO.save(metaField);
      	paymentMethodTemplateCardDTO.addPaymentTemplateMetaField(metaField);
      	
      	MetaField metaFieldPaymentCardNumber = new MetaField();
      	metaFieldPaymentCardNumber.setFieldUsage(MetaFieldType.PAYMENT_CARD_NUMBER);
      	metaFieldPaymentCardNumber.setEntity(company);
      	metaFieldPaymentCardNumber.setName("cc.number");
      	metaFieldPaymentCardNumber.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldPaymentCardNumber.setDataType(DataType.STRING);
      	metaFieldPaymentCardNumber.setDisabled(false);
      	metaFieldPaymentCardNumber.setMandatory(true);
      	metaFieldPaymentCardNumber.setPrimary(true);
      	metaFieldPaymentCardNumber.setDisplayOrder(2);
      	ValidationRule validationRule = new ValidationRule();
      	validationRule.setRuleType(ValidationRuleType.PAYMENT_CARD);
      	validationRule.setEnabled(true);
      	validationRule = validationRuleDAO.save(validationRule);
      	metaFieldPaymentCardNumber.setValidationRule(validationRule);
      	metaFieldPaymentCardNumber = metaFieldDAO.save(metaFieldPaymentCardNumber);

      	paymentMethodTemplateCardDTO.addPaymentTemplateMetaField(metaFieldPaymentCardNumber);
      	
      	
      	int valRulId = jbillingTableDAO.findByName(ServerConstants.TABLE_VALIDATION_RULE).getId();
      	InternationalDescriptionId internationalDescriptionId = new InternationalDescriptionId(valRulId, validationRule.getId(), 
      			"errorMessage", language.getId());
      	//internationalDescriptionDAO.save(internationalDescriptionId);
      	InternationalDescriptionDTO internationalDescriptionDTO = new InternationalDescriptionDTO(internationalDescriptionId,
      			getMessage("validation.payment.card.number.invalid",locale));
      	internationalDescriptionDAO.save(internationalDescriptionDTO);
      	
      	
      	MetaField metaFieldDate = new MetaField();
      	metaFieldDate.setFieldUsage(MetaFieldType.DATE);
      	metaFieldDate.setEntity(company);
      	metaFieldDate.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldDate.setDataType(DataType.STRING);
      	metaFieldDate.setDisabled(false);
      	metaFieldDate.setMandatory(true);
      	metaFieldDate.setPrimary(true);
      	SortedMap<String, String> attributes = new TreeMap<String, String>();
      	attributes.put("regularExpression", "(?:0[1-9]|1[0-2])/[0-9]{4}");
      	ValidationRule validationRuleRegex = new ValidationRule();
      	validationRuleRegex.setRuleType(ValidationRuleType.REGEX);
      	validationRuleRegex.setEnabled(true);
      	validationRuleRegex.setRuleAttributes(attributes);
      	validationRuleRegex = validationRuleDAO.save(validationRuleRegex);
      	metaFieldDate.setName("cc.expiry.date");
      	metaFieldDate.setDisplayOrder(3);

      	metaFieldDate.setValidationRule(validationRuleRegex);
      	metaFieldDate = metaFieldDAO.save(metaFieldDate);

      	paymentMethodTemplateCardDTO.addPaymentTemplateMetaField(metaFieldDate);
      	InternationalDescriptionId internationalDescriptionIdRegex = new InternationalDescriptionId(valRulId, validationRuleRegex.getId(), 
      			"errorMessage", language.getId());
      	
      	InternationalDescriptionDTO internationalDescriptionExpiryDTO = new InternationalDescriptionDTO(internationalDescriptionIdRegex,
      			getMessage("validation.payment.card.expiry.date.invalid",locale));
      	internationalDescriptionDAO.save(internationalDescriptionExpiryDTO);
      	
      	
      	MetaField metaFieldGatewayKey = new MetaField();
      	metaFieldGatewayKey.setEntity(company);
      	metaFieldGatewayKey.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldGatewayKey.setDataType(DataType.STRING);
      	metaFieldGatewayKey.setFieldUsage(MetaFieldType.GATEWAY_KEY);
      	metaFieldGatewayKey.setValidationRule(null);
      	metaFieldGatewayKey.setName("cc.gateway.key");
      	metaFieldGatewayKey.setDisplayOrder(4);
      	metaFieldGatewayKey.setDisabled(true);
      	metaFieldGatewayKey.setMandatory(false);
      	metaFieldGatewayKey.setPrimary(true);

      	paymentMethodTemplateCardDTO.addPaymentTemplateMetaField(metaFieldGatewayKey);
      	
      	MetaField metaFieldCCType = new MetaField();
      	metaFieldCCType.setFieldUsage(MetaFieldType.CC_TYPE);
      	metaFieldCCType.setName("cc.type");
      	metaFieldCCType.setDisplayOrder(5);
      	metaFieldCCType.setDataType(DataType.INTEGER);
      	metaFieldCCType.setDisabled(true);
      	metaFieldCCType.setMandatory(false);
      	metaFieldCCType.setPrimary(true);
      	metaFieldCCType.setEntity(company);
      	metaFieldCCType.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldCCType = metaFieldDAO.save(metaFieldCCType);

      	paymentMethodTemplateCardDTO.addPaymentTemplateMetaField(metaFieldCCType);
      	
      	
      	PaymentMethodTemplateDTO paymentMethodTemplateAchDTO = paymentMethodTemplateDAO.findByName(ServerConstants.ACH);
      	
      	MetaField metaFieldBankRoutingNumber = new MetaField();

      	metaFieldBankRoutingNumber.setDisabled(false);
      	metaFieldBankRoutingNumber.setMandatory(true);
      	metaFieldBankRoutingNumber.setFieldUsage(MetaFieldType.BANK_ROUTING_NUMBER);
      	metaFieldBankRoutingNumber.setName("ach.routing.number");
      	metaFieldBankRoutingNumber.setDisplayOrder(1);
      	metaFieldBankRoutingNumber.setDataType(DataType.STRING);
      	metaFieldBankRoutingNumber.setPrimary(true);
      	metaFieldBankRoutingNumber.setEntity(company);
      	metaFieldBankRoutingNumber.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);

      	SortedMap<String, String> attributesRouting = new TreeMap<String, String>();
      	attributesRouting.put("regularExpression", "(?<=\\\\s|^)\\\\d+(?=\\\\s|$)");
      	ValidationRule validationRuleRouting = new ValidationRule();
      	validationRuleRouting.setRuleType(ValidationRuleType.REGEX);
      	validationRuleRouting.setEnabled(true);
      	validationRuleRouting.setRuleAttributes(attributesRouting);
      	validationRuleRouting = validationRuleDAO.save(validationRuleRouting);
      	metaFieldBankRoutingNumber.setValidationRule(validationRuleRouting);
      	metaFieldBankRoutingNumber = metaFieldDAO.save(metaFieldBankRoutingNumber);
      	paymentMethodTemplateAchDTO.addPaymentTemplateMetaField(metaFieldBankRoutingNumber);
      	
      	
      	InternationalDescriptionId internationalDescriptionIdRouting = new InternationalDescriptionId(valRulId, validationRuleRouting.getId(), 
      			"errorMessage", language.getId());
      	InternationalDescriptionDTO internationalDescriptionRoutingDTO = new InternationalDescriptionDTO(internationalDescriptionIdRouting,
      			getMessage("validation.ach.aba.routing.number.invalid",locale));
      	internationalDescriptionDAO.save(internationalDescriptionRoutingDTO);
      	
      	MetaField metaFieldTitle= new MetaField();
      	metaFieldTitle.setDisabled(false);
      	metaFieldTitle.setMandatory(true);
      	metaFieldTitle.setPrimary(true);
      	metaFieldTitle.setEntity(company);
      	metaFieldTitle.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldTitle.setValidationRule(null);
      	metaFieldTitle.setFieldUsage(MetaFieldType.TITLE);
      	metaFieldTitle.setName("ach.customer.name");
      	metaFieldTitle.setDisplayOrder(2);
      	metaFieldTitle.setDataType(DataType.STRING);
      	metaFieldTitle = metaFieldDAO.save(metaFieldTitle);
      	paymentMethodTemplateAchDTO.addPaymentTemplateMetaField(metaFieldTitle);
      	
      	MetaField metaFieldBankAccountNumber= new MetaField();
      	metaFieldBankAccountNumber.setDisabled(false);
      	metaFieldBankAccountNumber.setMandatory(true);
      	metaFieldBankAccountNumber.setPrimary(true);
      	metaFieldBankAccountNumber.setEntity(company);
      	metaFieldBankAccountNumber.setDataType(DataType.STRING);
      	metaFieldBankAccountNumber.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldBankAccountNumber.setValidationRule(null);
      	metaFieldBankAccountNumber.setFieldUsage(MetaFieldType.BANK_ACCOUNT_NUMBER);
      	metaFieldBankAccountNumber.setName("ach.account.number");
      	metaFieldBankAccountNumber.setDisplayOrder(3);
      	metaFieldBankAccountNumber = metaFieldDAO.save(metaFieldBankAccountNumber);

      	paymentMethodTemplateAchDTO.addPaymentTemplateMetaField(metaFieldBankAccountNumber);
      	
      	MetaField metaFieldBankName= new MetaField();
      	metaFieldBankName.setDisabled(false);
      	metaFieldBankName.setMandatory(true);
      	metaFieldBankName.setPrimary(true);
      	metaFieldBankName.setEntity(company);
      	metaFieldBankName.setDataType(DataType.STRING);
      	metaFieldBankName.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldBankName.setValidationRule(null);
      	metaFieldBankName.setFieldUsage(MetaFieldType.BANK_NAME);
      	metaFieldBankName.setName("ach.bank.name");
      	metaFieldBankName.setDisplayOrder(4);
      	metaFieldBankName = metaFieldDAO.save(metaFieldBankName);

      	paymentMethodTemplateAchDTO.addPaymentTemplateMetaField(metaFieldBankName);
      	
      	MetaField metaFieldBankAccountType= new MetaField();
      	metaFieldBankAccountType.setDisabled(false);
      	metaFieldBankAccountType.setMandatory(true);
      	metaFieldBankAccountType.setPrimary(true);
      	metaFieldBankAccountType.setEntity(company);
      	metaFieldBankAccountType.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldBankAccountType.setValidationRule(null);

      	metaFieldBankAccountType.setFieldUsage(MetaFieldType.BANK_ACCOUNT_TYPE);
      	metaFieldBankAccountType.setName("ach.account.type");
      	metaFieldBankAccountType.setDisplayOrder(5);
      	metaFieldBankAccountType.setDataType(DataType.ENUMERATION);
      	metaFieldBankAccountType = metaFieldDAO.save(metaFieldBankAccountType);

      	paymentMethodTemplateAchDTO.addPaymentTemplateMetaField(metaFieldBankAccountType);
      	
      	
      	EnumerationDTO enumerationDTO = new EnumerationDTO();
      	enumerationDTO.setName("ach.account.type");
      	enumerationDTO.setEntity(company);
      	
      	enumerationDTO = enumerationDAO.save(enumerationDTO);
      	
      	EnumerationValueDTO enumerationValueCheckingDTO = new EnumerationValueDTO();
      	enumerationValueCheckingDTO.setValue("CHECKING");
      	enumerationValueCheckingDTO.setEnumeration(enumerationDTO);
      	enumerationValueDAO.save(enumerationValueCheckingDTO);
      	
      	EnumerationValueDTO enumerationValueSavingsDTO = new EnumerationValueDTO();
      	enumerationValueSavingsDTO.setValue("SAVINGS");
      	enumerationValueSavingsDTO.setEnumeration(enumerationDTO);
      	enumerationValueDAO.save(enumerationValueSavingsDTO);
      	
      	
      	MetaField metaFieldGatewayKeyAch= new MetaField();
      	metaFieldGatewayKeyAch.setPrimary(true);
      	metaFieldGatewayKeyAch.setEntity(company);
      	metaFieldGatewayKeyAch.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldGatewayKeyAch.setValidationRule(null);

      	metaFieldGatewayKeyAch.setDisabled(true);
      	metaFieldGatewayKeyAch.setMandatory(false);
      	
      	metaFieldGatewayKeyAch.setFieldUsage(MetaFieldType.GATEWAY_KEY);
      	metaFieldGatewayKeyAch.setName("ach.gateway.key");
      	metaFieldGatewayKeyAch.setDisplayOrder(6);
      	metaFieldGatewayKeyAch.setDataType(DataType.STRING);
      	metaFieldGatewayKeyAch = metaFieldDAO.save(metaFieldGatewayKeyAch);
      	paymentMethodTemplateAchDTO.addPaymentTemplateMetaField(metaFieldGatewayKeyAch);

      	PaymentMethodTemplateDTO paymentMethodTemplateChequeDTO = paymentMethodTemplateDAO.findByName(ServerConstants.CHEQUE);

      	MetaField metaFieldBankNameCheque= new MetaField();
      	metaFieldBankNameCheque.setPrimary(true);
      	metaFieldBankNameCheque.setEntity(company);
      	metaFieldBankNameCheque.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldBankNameCheque.setValidationRule(null);
      	metaFieldBankNameCheque.setDisabled(false);
      	metaFieldBankNameCheque.setMandatory(true);
      	
      	metaFieldBankNameCheque.setFieldUsage(MetaFieldType.BANK_NAME);
      	metaFieldBankNameCheque.setName("cheque.bank.name");
      	metaFieldBankNameCheque.setDisplayOrder(1);
      	metaFieldBankNameCheque.setDataType(DataType.STRING);
      	metaFieldBankNameCheque = metaFieldDAO.save(metaFieldBankNameCheque);

      	paymentMethodTemplateChequeDTO.addPaymentTemplateMetaField(metaFieldBankNameCheque);

      	MetaField metaFieldCheque= new MetaField();
      	metaFieldCheque.setPrimary(true);
      	metaFieldCheque.setEntity(company);
      	metaFieldCheque.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldCheque.setValidationRule(null);
      	metaFieldCheque.setDisabled(false);
      	metaFieldCheque.setMandatory(true);
      	metaFieldCheque.setFieldUsage(MetaFieldType.CHEQUE_NUMBER);
      	metaFieldCheque.setName("cheque.number");
      	metaFieldCheque.setDisplayOrder(2);
      	metaFieldCheque.setDataType(DataType.STRING);
      	metaFieldCheque = metaFieldDAO.save(metaFieldCheque);


      	paymentMethodTemplateChequeDTO.addPaymentTemplateMetaField(metaFieldCheque);

      	MetaField metaFieldDateCheque= new MetaField();
      	metaFieldDateCheque.setPrimary(true);
      	metaFieldDateCheque.setEntity(company);
      	metaFieldDateCheque.setEntityType(EntityType.PAYMENT_METHOD_TEMPLATE);
      	metaFieldDateCheque.setValidationRule(null);
      	metaFieldDateCheque.setDisabled(false);
      	metaFieldDateCheque.setMandatory(true);
      	metaFieldDateCheque.setFieldUsage(MetaFieldType.DATE);
      	metaFieldDateCheque.setName("cheque.date");
      	metaFieldDateCheque.setDisplayOrder(3);
      	metaFieldDateCheque = metaFieldDAO.save(metaFieldDateCheque);

      	paymentMethodTemplateChequeDTO.addPaymentTemplateMetaField(metaFieldDateCheque);

      	paymentMethodTemplateDAO.save(paymentMethodTemplateChequeDTO);
      	paymentMethodTemplateDAO.save(paymentMethodTemplateAchDTO);
      	paymentMethodTemplateDAO.save(paymentMethodTemplateCardDTO);
 
	}

	public void updateParametersForTask(PluggableTaskDTO taskDTO) {
        for (PluggableTaskParameterDTO parameter : taskDTO.getParameters()) {
            parameter.setTask(taskDTO);
            pluggableTaskParameterDAO.save(parameter);
        }
    }
	public void createNotificationMessage(Integer typeId, String titleCode, String bodyCode,CompanyDTO company, LanguageDTO language,Locale locale)
    {
    	NotificationMessageDTO notificationMessageDTO = new NotificationMessageDTO();
    	notificationMessageDTO.setEntity(company);
    	notificationMessageDTO.setLanguage(language);
    	notificationMessageDTO.setUseFlag((short) 1);
    	NotificationMessageTypeDTO notificationMessageTypeDTO= new NotificationMessageTypeDTO();
    	notificationMessageDTO.setNotificationMessageType(new NotificationMessageTypeDTO(typeId));
    	
    	List<NotificationMediumType> notificationMediumTypeList = new ArrayList<NotificationMediumType>(Arrays.asList(NotificationMediumType.values()));
    	notificationMessageDTO.setMediumTypes(notificationMediumTypeList);
    	notificationMessageDTO = notificationMessageDAO.save(notificationMessageDTO);
    	
    	NotificationMessageSectionDTO notificationMessageSectionDTO = new NotificationMessageSectionDTO();
    	notificationMessageSectionDTO.setNotificationMessage(notificationMessageDTO);
    	notificationMessageSectionDTO.setSection(1);

    	NotificationMessageLineDTO notificationMessageLineDTO = new NotificationMessageLineDTO();
    	notificationMessageLineDTO.setNotificationMessageSection(notificationMessageSectionDTO);
    	notificationMessageLineDTO.setContent(getMessage(titleCode,locale));
    	notificationMessageSectionDTO.getNotificationMessageLines().add(notificationMessageLineDTO);
    	notificationMessageDTO.getNotificationMessageSections().add(notificationMessageSectionDTO);
    	notificationMessageSectionDAO.save(notificationMessageSectionDTO);
    	
    	NotificationMessageSectionDTO notificationMessageSectionBodyDTO = new NotificationMessageSectionDTO();
    	notificationMessageSectionBodyDTO.setNotificationMessage(notificationMessageDTO);
    	notificationMessageSectionBodyDTO.setSection(2);

    	NotificationMessageLineDTO notificationMessageLineBodyDTO = new NotificationMessageLineDTO();
    	notificationMessageLineBodyDTO.setNotificationMessageSection(notificationMessageSectionBodyDTO);
    	notificationMessageLineBodyDTO.setContent(getMessage(bodyCode,locale));
    	notificationMessageSectionBodyDTO.getNotificationMessageLines().add(notificationMessageLineBodyDTO);
    	notificationMessageDTO.getNotificationMessageSections().add(notificationMessageSectionBodyDTO);
    	notificationMessageSectionDAO.save(notificationMessageSectionBodyDTO);
    }
	
	public String getMessage(String code, Locale locale) {
		return messageSource.getMessage(code, new Object[0], code, locale);
	}
	 
	    
}
