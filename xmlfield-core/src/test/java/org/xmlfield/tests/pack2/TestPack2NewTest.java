/*
 * Copyright 2010 Capgemini
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 * 
 */
package org.xmlfield.tests.pack2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlfield.core.XmlField;
import org.xmlfield.core.api.XmlFieldNode;

/**
 * Test basic xmlfield usage, with namespaces and attributes.
 * 
 * @author Nicolas Richeton <nicolas.richeton@capgemini.com>
 */
public class TestPack2NewTest {

	private final XmlField binder = new XmlField();

	Logger log = LoggerFactory.getLogger(TestPack2NewTest.class);

	/**
	 * Test adding element.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddCd() throws Exception {
		// we extract namespaces from xml becuse we cannot warranty the output
		// orders od the namespaces
		String ns1 = "xmlns:a=\"http://www.w3.org/2005/Atom\"";
		String ns2 = "xmlns=\"http://www.w3.org/1999/xhtml\"";
		String xml1 = "<a:title type=\"xhtml\">   <div>     <span class=\"name\">CD Catalog</span>     <span class=\"format\">Atom</span>   </div> </a:title> <a:id>12345</a:id> <a:updated>2003-12-14T18:30:02Z</a:updated> <a:author>   <a:name>15</a:name> </a:author> <a:content type=\"xhtml\">   <div>     <div class=\"cd\">       <span class=\"title\">01</span>       <span class=\"price\">999999</span>       <span class=\"artist\">QL0001</span>     </div>   </div> </a:content></a:entry>";
		String expected = "<a:title type=\"xhtml\">   <div>     <span class=\"name\">CD Catalog</span>     <span class=\"format\">Atom</span>   </div> </a:title> <a:id>12345</a:id> <a:updated>2003-12-14T18:30:02Z</a:updated> <a:author>   <a:name>15</a:name> </a:author> <a:content type=\"xhtml\">   <div>     <div class=\"cd\">       <span class=\"title\">01</span>       <span class=\"price\">999999</span>       <span class=\"artist\">QL0001</span>     </div>   <div class=\"cd\"><span class=\"title\">title</span></div></div> </a:content></a:entry>";

		final String xml2 = "<a:entry " + ns1 + " " + ns2 + " >" + xml1;

		final XmlFieldNode node = binder.xmlToNode(xml2);

		final AtomCatalog catalog = binder
				.nodeToObject(node, AtomCatalog.class);

		int numberOfCds = catalog.getCd().length;
		assertEquals(1, numberOfCds);
		AtomCd cd = catalog.addToCd();
		cd.setTitle("title");

		String result = binder.nodeToXml(node);
		log.info(result);
		assertTrue(result.contains(ns1));
		assertTrue(result.contains(ns2));
		assertTrue(result.contains(expected));

	}

	/**
	 * Check read and update abilities (get/set).
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSimple() throws Exception {

		final String xml = "<a:entry xmlns:a=\"http://www.w3.org/2005/Atom\" xmlns=\"http://www.w3.org/1999/xhtml\" > <a:title type=\"xhtml\">   <div>     <span class=\"name\">CD Catalog</span>     <span class=\"format\">Atom</span>   </div> </a:title> <a:id>12345</a:id> <a:updated>2003-12-14T18:30:02Z</a:updated> <a:author>   <a:name>15</a:name> </a:author> <a:content type=\"xhtml\">   <div>     <div class=\"cd\">       <span class=\"title\">01</span>       <span class=\"price\">999999</span>       <span class=\"artist\">QL0001</span>     </div>   </div> </a:content></a:entry>";

		final XmlFieldNode node = binder.xmlToNode(xml);

		final AtomCatalog catalog = binder
				.nodeToObject(node, AtomCatalog.class);

		assertEquals("CD Catalog", catalog.getName());
		int numberOfCds = catalog.getCd().length;
		assertEquals(1, numberOfCds);
		for (int i = 0; i < numberOfCds; i++) {
			AtomCd cd = catalog.getCd()[i];
			// log.info(cd.toString());
			assertNotNull(cd.getTitle());
			assertTrue(cd.getPrice() > 0);
			cd.setTitle("toto");
			assertEquals("toto", cd.getTitle());
		}

		String result = binder.nodeToXml(node);
		log.info(result);

	}
}
