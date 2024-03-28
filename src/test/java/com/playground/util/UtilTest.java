package com.playground.util;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testEncodeThenDecode()
    {
        String inputJsonString_basic = "{\n" +
                "    \"root_type\": \"Soldier\",\n" +
                "    \"name\": \"Mike\",\n" +
                "    \"hp\": 100,\n" +
                "    \"mana\": 20,\n" +
                "    \"weapons\": [\"Axe\",\"Sword\",\"Spear\"]\n" +
                "    }\n" +
                "}";


        String encodedString = Util.encode(inputJsonString_basic);
        System.out.println(encodedString);

        // Decode the encoded string back into a JSON string.
        String decodedString = Util.decode(encodedString);
        System.out.println(decodedString);
    }
}
