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
import io.hexaforce.history.MailSendHistory;
import io.hexaforce.history.MailSendHistoryRepository;
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

//	@Autowired
//	MailSendHistoryRepository mailSendHistoryRepository;

	@Test
	public void S3の暗号化アクセステスト() {
		for (Bucket bucket : SimpleStorageService.listBucket()) {
			assertThat(bucket).isNotNull();
		}
	}
//	
//	@Test
//	public void SESの暗号化アクセステスト() {
//		EmailObject o = new EmailObject();
//		o.setFrom("no-reply@bunkyo.dev-campus-gate.com");
//		o.setTo("bunkyo.opt@gmail.com");
//		o.setSubject("sendAsyncEmail");
//		o.setBody("BodyBodyBodyBodyBodyBodyBodyBodyBodyBody");
//		EmailObject result = SimpleEmailService.sendAsyncEmail(o);
//		assertThat(result).isNotNull();
//	}	
//	
//	@Test
//	public void SQSの暗号化アクセステスト() {
//		String requestUrl = "no-reply-mail-bunkyo";
//		for (QueueObject q :SimpleQueueService.receiveMessage(requestUrl)) {
//			SESNotificationAnalyzer.analyze(q.getBody());
//		}
//	}	

//	@Test
//	public void SNSの暗号化アクセステスト() {
//		mailSendHistoryRepository.save(new MailSendHistory(null, null, null, null, null, null, null, null));
////		assertThat(mailSendHistoryRepository.count()).isEqualTo(0);
//	}
}
