package asakichy.architecture.domain_logic.domain_model;

import static asakichy.architecture.domain_logic.DateUtils.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Test;

public class ContractTest {
	@Test
	public void ある時点での収益認識() throws Exception {
		Product wordProcessor = Product.newWordProcessor("ワープロ１");
		Product database = Product.newDatabase("データベース１");
		Product spreadSheet = Product.newSpreadSheet("表計算１");

		Date signedDate = getDate(2012, Calendar.JANUARY, 1);

		// 商品「ワープロ」の契約
		Contract wordProcessorContract = new Contract(0, wordProcessor, 100, signedDate);
		wordProcessorContract.calculateRecognitions();
		// 契約時全額計上
		assertThat(wordProcessorContract.recognizedRevenue(signedDate), is(100L));

		// 商品「データベース」の契約
		Contract databaseContract = new Contract(1, database, 100, signedDate);
		databaseContract.calculateRecognitions();
		// 契約時は1/3
		assertThat(databaseContract.recognizedRevenue(signedDate), is(33L));
		// 60日後に1/3
		assertThat(databaseContract.recognizedRevenue(addDays(signedDate, 60)), is(66L));
		// 90日後に残り
		assertThat(databaseContract.recognizedRevenue(addDays(signedDate, 90)), is(100L));

		// 商品「表計算」の契約
		Contract spreadSheetContract = new Contract(2, spreadSheet, 100, signedDate);
		spreadSheetContract.calculateRecognitions();
		// 契約時は1/3
		assertThat(spreadSheetContract.recognizedRevenue(signedDate), is(33L));
		// 30日後に1/3
		assertThat(spreadSheetContract.recognizedRevenue(addDays(signedDate, 30)), is(66L));
		// 60日後に残り
		assertThat(spreadSheetContract.recognizedRevenue(addDays(signedDate, 60)), is(100L));
	}
}
