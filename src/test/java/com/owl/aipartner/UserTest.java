package com.owl.aipartner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.owl.aipartner.model.po.User;
import com.owl.aipartner.repository.mongo.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

        private HttpHeaders httpHeaders;

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Before
        public void init() {
                httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }

        @After
        public void clear() {
                userRepository.deleteAll();
        }

        @Test
        public void testCreateUser() throws Exception {
                JSONObject request = new JSONObject()
                                .put("id", "1")
                                .put("name", "Mike")
                                .put("age", 18);

                RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                                .headers(httpHeaders)
                                .content(request.toString());
                // 初次新增
                mockMvc.perform(requestBuilder)
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").hasJsonPath())
                                .andExpect(jsonPath("$.name").value(request.get("name")))
                                .andExpect(jsonPath("$.age").value(request.get("age")))
                                .andExpect(header().exists(HttpHeaders.LOCATION))
                                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
                // 若 ID 重複
                // mockMvc.perform(requestBuilder)
                //                 .andExpect(status().isUnprocessableEntity());
        }

        @Test
        public void testGetUser() throws Exception {
                User user = new User(null, "Mike", 22);
                userRepository.insert(user);

                mockMvc.perform(get("/users/" + user.getId())
                                .headers(httpHeaders))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(user.getId()))
                                .andExpect(jsonPath("$.name").value(user.getName()))
                                .andExpect(jsonPath("$.age").value(user.getAge()));
        }

        @Test
        public void testReplaceUser() throws Exception {
                User user = new User(null, "Mike", 22);
                userRepository.insert(user);

                JSONObject request = new JSONObject()
                                .put("name", "Ted")
                                .put("age", 14);

                mockMvc.perform(put("/users/" + user.getId())
                                .headers(httpHeaders)
                                .content(request.toString()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(user.getId()))
                                .andExpect(jsonPath("$.name").value(request.getString("name")))
                                .andExpect(jsonPath("$.age").value(request.getInt("age")));
        }

        @Test(expected = RuntimeException.class)
        public void testDeleteUser() throws Exception {
                User user = new User(null, "Mike", 22);
                userRepository.insert(user);

                mockMvc.perform(delete("/users/" + user.getId())
                                .headers(httpHeaders))
                                .andExpect(status().isNoContent());

                userRepository.findById(user.getId())
                                .orElseThrow(RuntimeException::new);
        }

        @Test
        public void testSearchUsersSortByNameAsc() throws Exception {
                User u1 = new User("1", "Apple", 26);
                User u2 = new User("2", "Banana", 100);
                User u3 = new User("3", "Cat", 91);
                User u4 = new User("4", "Dog", 72);
                User u5 = new User("5", "Eagle", 51);
                userRepository.insert(List.of(u1, u2, u3, u4, u5));

                MvcResult result = mockMvc.perform(get("/users")
                                .headers(httpHeaders)
                                .param("name", "l")
                                .param("orderBy", "name")
                                .param("sortRule", "asc"))
                                .andReturn();
                MockHttpServletResponse mockHttpResp = result.getResponse();
                String respJson = mockHttpResp.getContentAsString();
                JSONArray userJsonArray = new JSONArray(respJson);

                Assert.assertEquals(2, userJsonArray.length());
                Assert.assertEquals(u1.getId(), userJsonArray.getJSONObject(0).getString("id"));
                Assert.assertEquals(u5.getId(), userJsonArray.getJSONObject(1).getString("id"));

                Assert.assertEquals(HttpStatus.OK.value(), mockHttpResp.getStatus());
                Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE,
                                mockHttpResp.getHeader(HttpHeaders.CONTENT_TYPE));

        }

        @Test
        public void get400WhenCreateUserWithEmptyName() throws Exception {
                JSONObject request = new JSONObject()
                                .put("name", "")
                                .put("age", 14);
                mockMvc.perform(post("/users")
                        .headers(httpHeaders)
                        .content(request.toString()))
                        .andExpect(status().isBadRequest());
        }

        @Test
        public void get400WhenReplaceUserWithTooLargeAge() throws Exception {
                                JSONObject request = new JSONObject()
                                .put("name", "")
                                .put("age", 200);
                mockMvc.perform(put("/users/" + 1)
                        .headers(httpHeaders)
                        .content(request.toString()))
                        .andExpect(status().isBadRequest());
        }

}
