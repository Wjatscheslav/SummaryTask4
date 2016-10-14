package ua.nure.tarasenko.summary4.util;

import ua.nure.tarasenko.summary4.sort.AccountSortOrder;
import ua.nure.tarasenko.summary4.sort.PaymentSortOrder;
import ua.nure.tarasenko.summary4.sort.SortOrder;

public class Constants {

	public static int LANGUAGE_ID = 1;
	public static final String EN_PROPERTIES_PATH = "src/ua/nure/tarasenko/summary4/resources/resources_en.properties";
	public static final String RU_PROPERTIES_PATH = "src/ua/nure/tarasenko/summary4/resources/resources_ru.properties";
	public static AccountSortOrder ACCOUNT_ORDER = AccountSortOrder.ID;
	public static PaymentSortOrder PAYMENT_ORDER = PaymentSortOrder.PAYMENT_ID;
	public static SortOrder ORDER = SortOrder.ASC;

}
