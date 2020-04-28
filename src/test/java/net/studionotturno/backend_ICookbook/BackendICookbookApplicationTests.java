package net.studionotturno.backend_ICookbook;

import net.studionotturno.backend_ICookbook.security.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class BackendICookbookApplicationTests {

	@Test
	void contextLoads() {
		JwtTokenUtil jwt=new JwtTokenUtil();
		System.out.println(jwt.getJwt());
		System.out.println(TimeUnit.DAYS.toSeconds(356));

	}

}
