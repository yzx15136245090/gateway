package com.shaopeng.gateway;

import com.shaopeng.gateway.dto.Api;
import com.shaopeng.gateway.framework.invoke.GenericDubboInvoke;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= GatewayApplicationTests.class)
@WebAppConfiguration
public class GatewayApplicationTests  extends TestCase {

//	@Resource
	private GenericDubboInvoke genericDubboInvoke;
	@Test
	public void contextLoads() throws Exception {

//		Api api = new Api();
//		api.setRpcInterface("com.shaopeng.gateway.test.IUserService");
//		api.setRpcVersion("1.0.0");
//		api.setRpcTimeout(200);
//		api.setRpcMethod("addUser");
//		api.setRpcParamType("java.lang.String");
//		genericDubboInvoke.invoke(api, Arrays.asList("q","w"));

		JAXBContext context = JAXBContext.newInstance(Api.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

	}

}
