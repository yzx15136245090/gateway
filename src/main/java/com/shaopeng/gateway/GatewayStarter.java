package com.shaopeng.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CountDownLatch;

//=======================================================
//		          .----.
//		       _.'__    `.
//		   .--(^)(^^)---/!\
//		 .' @          /!!!\
//		 :         ,    !!!!
//		  `-..__.-' _.-\!!!/
//		        `;_:    `"'
//		      .'"""""`.
//		     /,  ya ,\\
//		    //  保佑  \\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//
//=======================================================
@SpringBootApplication
@MapperScan("com.shaopeng.gateway.dao.mapper")
public class GatewayStarter {

	public static void main(String[] args)  throws InterruptedException{
		SpringApplication application = new SpringApplication(GatewayStarter.class);
		ApplicationContext ctx = application.run(args);
		final CountDownLatch closeLatch = ctx.getBean(CountDownLatch.class);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> closeLatch.countDown()));
		closeLatch.await();
	}

	@Bean
	public CountDownLatch closeLatch() {
		return new CountDownLatch(1);
	}
}
