package com.playground.util;

import org.junit.Test;

// test
public class UtilTest {
    @Test
    public void testEncodeThenDecode()
    {
        String jsonString_basic = "{\n" +
                "    \"root_type\": \"Soldier\", \n" +
                "    \"name\": \"Mike\",\n" +
                "    \"hp\": 100,\n" +
                "    \"mana\": 20\n" +
                "}";

        String jsonString_basic_array = "{\n" +
                "    \"root_type\": \"Soldier\",\n" +
                "    \"name\": \"Mike\",\n" +
                "    \"hp\": 100,\n" +
                "    \"mana\": 20,\n" +
                "    \"weapons\": [\"Axe\",\"Sword\",\"Spear\"]\n" +
                "    }\n" +
                "}";

        String jsonString_external_class = "{\n" +
                "    \"root_type\": \"Soldier\",\n" +
                "    \"name\": \"Mike\",\n" +
                "    \"hp\": 100,\n" +
                "    \"mana\": 20,\n" +
                "    \"weapon\": {\n" +
                "        \"name\": \"Axe\",\n" +
                "        \"damage\": 50\n" +
                "    }\n" +
                "}";

        String jsonString_external_class_array = "{\n" +
                "    \"root_type\": \"Soldier\",\n" +
                "    \"name\": \"Mike\",\n" +
                "    \"hp\": 100,\n" +
                "    \"mana\": 20,\n" +
                "    \"weapons\": [\n" +
                "        {\n" +
                "            \"name\": \"Axe\",\n" +
                "            \"damage\": 50\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Sword\",\n" +
                "            \"damage\": 30\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"Spear\",\n" +
                "            \"damage\": 40\n" +
                "        }\n" +
                "    ]\n" +
                "}";
//        String encodedString = Util.encode(jsonString_basic);
//        String encodedString = Util.encode(jsonString_basic_array);
//        String encodedString = Util.encode(jsonString_external_class);
        String encodedString = Util.encode(jsonString_external_class_array);

        System.out.println("=================================");
        System.out.println("Encoded string is like following: ");
        System.out.println(encodedString);

        // Decode the encoded string back into a JSON string.
        String decodedString = Util.decode(encodedString);
        System.out.println(decodedString);
    }
}
