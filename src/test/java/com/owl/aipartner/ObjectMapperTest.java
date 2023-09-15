package com.owl.aipartner;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ObjectMapperTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class Book {
        private String id;

        @JsonProperty("bookName")
        private String name;
        private int price;
        @JsonIgnore
        private String isbn;
        @JsonFormat(pattern = "yyyyMMdd")
        private Date createdTime;
        @JsonUnwrapped
        private Publisher publisher;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static class Publisher {
        private String companyName;
        private String address;
        @JsonProperty("telephone")
        private String tel;
    }

    @Test
    public void testSerializeBookToJSON() throws Exception {
        Book book = Book.builder()
                .id("B0001")
                .name("Computer Science")
                .price(350)
                .isbn("978-986-123-456-7")
                .createdTime(new Date())
                .build();

        String bookJSON = mapper.writeValueAsString(book);
        System.out.println(bookJSON);

        JSONObject jsonObject = new JSONObject(bookJSON);

        Assert.assertEquals(book.getId(), jsonObject.getString("id"));
        Assert.assertEquals(book.getName(), jsonObject.getString("bookName"));
        Assert.assertEquals(book.getPrice(), jsonObject.getInt("price"));
        // Assert.assertEquals(book.getIsbn(), jsonObject.getString("isbn"));
        Assert.assertEquals(new SimpleDateFormat("yyyyMMdd").format(book.getCreatedTime()), jsonObject.getString("createdTime"));

        Publisher publisher = Publisher.builder()
                .address("Taipei")
                .companyName("")
                .tel("02-1234-5678")
                .build();
        book.setPublisher(publisher);
        System.out.println(mapper.writeValueAsString(book));
    }

    @Test
    public void testDeserializeJSONToPublisher() throws Exception {
        JSONObject publisherJSON = new JSONObject()
                .put("companyName", "Taipei Company")
                .put("address", "Taipei")
                .put("tel", "02-1234-5678");

        String json = publisherJSON.toString();
        Publisher publisher = mapper.readValue(json, Publisher.class);

        Assert.assertEquals(publisherJSON.getString("companyName"), publisher.getCompanyName());
        Assert.assertEquals(publisherJSON.getString("address"), publisher.getAddress());
        Assert.assertEquals(publisherJSON.getString("tel"), publisher.getTel());
    }
}
