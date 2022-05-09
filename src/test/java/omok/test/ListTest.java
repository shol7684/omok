package omok.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class ListTest {

	@Test
	public void list() {
		Map<String, Integer> map =new HashMap();
		
		map.put("a",1);
		map.put("b",2);
		map.put("c",3);
		

		map.remove("a");
		System.out.println(map.keySet());
		
		Iterator<String> it = map.keySet().iterator();

		while(it.hasNext()) {
			System.out.println(map.get(it.next()));
		}
		
		
		
		
		
		
		
	}
}
