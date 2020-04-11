package net.studionotturno.backend_ICookbook;

import net.studionotturno.backend_ICookbook.controllers.SearcherController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BackendICookbookApplicationTests {


	
	@Test
	void contextLoads() {
		SearcherController searcherController=new SearcherController();
	}

}
