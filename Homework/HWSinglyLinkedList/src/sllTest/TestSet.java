package sllTest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import sll.ILinkedList;
import sll.SinglyLinkedList;

@RunWith(RunAllTestsTestRunner.class)

public class TestSet {

	@Test
	public void testSetN01() {
		ILinkedList list1 = new SinglyLinkedList();	

		list1.add(89);	
		Integer originalSize = list1.size();
		
		assertEquals((Integer)89, list1.set(0, 99));
		assertEquals(originalSize, list1.size());
		assertEquals("[99] first=[99] last=[99]", list1.toString());
	} // testSetN01
	
	@Test
	public void testSetN02() {
		ILinkedList list1 = new SinglyLinkedList();	

		list1.add(89);	
		list1.add(99);
		list1.add(109);
		list1.add(119);
		list1.add(129);
		Integer originalSize = list1.size();
		
		assertEquals((Integer)89, list1.set(0, -99));
		assertEquals(originalSize, list1.size());
		assertEquals("[-99, 99, 109, 119, 129] first=[-99] last=[129]", list1.toString());
	} // testSetN02
	
	@Test
	public void testSetN03() {
		ILinkedList list1 = new SinglyLinkedList();	

		list1.add(89);	
		list1.add(99);
		list1.add(109);
		list1.add(119);
		list1.add(129);
		Integer originalSize = list1.size();
		
		assertEquals((Integer)109, list1.set(2, -109));
		assertEquals(originalSize, list1.size());
		assertEquals("[89, 99, -109, 119, 129] first=[89] last=[129]", list1.toString());
	} // testSetN03
	
	@Test
	public void testSetN04() {
		ILinkedList list1 = new SinglyLinkedList();	

		list1.add(89);	
		list1.add(99);
		list1.add(109);
		list1.add(119);
		list1.add(129);
		Integer originalSize = list1.size();
		
		assertEquals((Integer)129, list1.set(4, -129));
		assertEquals(originalSize, list1.size());
		assertEquals("[89, 99, 109, 119, -129] first=[89] last=[-129]", list1.toString());
	} // testSetN04
	
} // end class TestSet
