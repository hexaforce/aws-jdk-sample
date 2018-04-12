package io.hexaforce.aws;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.model.Bucket;

import io.hexaforce.aws.S3.SimpleStorageService;
import io.hexaforce.aws.SES.EmailObject;
import io.hexaforce.aws.SES.SimpleEmailService;
import io.hexaforce.aws.SNS.SESNotificationAnalyzer;
import io.hexaforce.aws.SQS.QueueObject;
import io.hexaforce.aws.SQS.SimpleQueueService;
import io.hexaforce.history.HistoryManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tantaka
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class FreudeAwsIntegrationTest {

	@Autowired
	private SimpleStorageService SSS;
	
	@Autowired
	private SimpleEmailService SES;
	
	@Autowired
	private SimpleQueueService SQS;
	
//	@Test
//	public void S3のアクセステスト() {
//		for (Bucket bucket : SSS.listBucket()) {
//			assertThat(bucket).isNotNull();
//		}
//	}
//	
//	@Test
//	public void SESのアクセステスト() {
//		EmailObject o = new EmailObject();
//		o.setFrom("no-reply@bunkyo.dev-campus-gate.com");
//		o.setTo("bunkyo.opt@gmail.com");
//		o.setSubject("sendAsyncEmail");
//		o.setBody("BodyBodyBodyBodyBodyBodyBodyBodyBodyBody");
//		EmailObject result = SES.sendAsyncEmail(o);
//		assertThat(result).isNotNull();
//	}
//	
//	
//	@Test
//	public void SQSのアクセステスト() {
//		String requestUrl = "no-reply-mail-bunkyo";
//		for (QueueObject q :SQS.receiveMessage(requestUrl)) {
//			SESNotificationAnalyzer.analyze(q.getBody());
//		}
//	}
	
	@Test
	public void HistoryManagerのテスト() throws InterruptedException {
		new HistoryManager().updateHistory();;
	}
}
