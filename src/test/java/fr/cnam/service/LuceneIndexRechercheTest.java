package fr.cnam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fr.cnam.SpringBootWebApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringBootWebApplication.class)
public class LuceneIndexRechercheTest {

	@Autowired
	private LuceneIndexRecherche service;
	
	@Test
	public void test1() {
		service.rechercher("Angine");
	}
}
