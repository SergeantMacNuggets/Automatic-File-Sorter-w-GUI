import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Main {
    public static void main(String[] args) throws IOException {
        new Window();
//        ObjectMapper objectMapper = new ObjectMapper();
//        File fileJsonFile = new File("src/Data.json");
//        String jsonString = Files.readString(fileJsonFile.toPath());
//        ArrayNode arrNode = objectMapper.createArrayNode();
//        JsonNode jsonNode = objectMapper.readTree(jsonString);
//        if(jsonNode.isArray()) {
//            for(JsonNode js: jsonNode) {
//                ObjectNode ob = objectMapper.createObjectNode();
//                ob.put("file",js.get("file").asText());
//                ob.put("dest",js.get("dest").asText());
//                ob.put("src",js.get("src").asText());
//                ob.put("date",js.get("date").asText());
//                arrNode.add(ob);
//            }
//        }
//
//        ObjectNode ob = objectMapper.createObjectNode();
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Input File: ");
//        ob.put("file",scanner.nextLine());
//        System.out.print("Input Dest: ");
//        ob.put("dest",scanner.nextLine());
//        System.out.print("Input SRC: ");
//        ob.put("src",scanner.nextLine());
//        System.out.print("Input Date: ");
//        ob.put("date",scanner.nextLine());
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        arrNode.add(ob);
//        objectMapper.writeValue(fileJsonFile,arrNode);

    }
}