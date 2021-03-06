package blog.thoughts.on.java.jpa21.enc.entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import blog.thoughts.on.java.jpa21.enc.converter.CryptoConverter;

@RunWith(Arquillian.class)
public class TestCryptoConverter {

	@PersistenceContext
	private EntityManager em;

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap
				.create(JavaArchive.class)
				.addClasses(CreditCard.class, CryptoConverter.class,
						TestCryptoConverter.class)
				.addAsManifestResource("META-INF/persistence.xml",
						"persistence.xml")
				.addAsManifestResource("META-INF/orm.xml", "orm.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	@ShouldMatchDataSet("data/cc.yml")
	public void testEncryption() {
		CreditCard cc = new CreditCard();
		cc.setName("My Name");
		cc.setCcNumber("123456789");

		this.em.persist(cc);
	}

	@Test
	@UsingDataSet("data/cc.yml")
	public void testRead() {
		CreditCard cc = this.em.find(CreditCard.class, 1);
		Assert.assertEquals("My Name", cc.getName());
		Assert.assertEquals("123456789", cc.getCcNumber());
	}
}
