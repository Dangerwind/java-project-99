package hexlet.code.controller.api;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;

import hexlet.code.dto.task.TaskStatusDTO;

import hexlet.code.repository.TaskStatusRepository;

import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

 //   @Autowired
 //   private UsersController usersController;

 //   @Autowired
 //   private UserService userService;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TaskStatusRepository userRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;

    private TaskStatus testTaskStatus;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    public void setUp() {
        taskStatusRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                // .apply(springSecurity())
                .build();

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();

        taskStatusRepository.save(testTaskStatus);
        // token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @AfterEach
    public void garbageDbDelete() {
        taskStatusRepository.deleteAll();
    }


    @Test
    public void testIndexTaskStatus() throws Exception {
        var response = mockMvc.perform(get("/api/task_statuses")) //.with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<TaskStatusDTO> taskStatusDTOS = om.readValue(body, new TypeReference<>() { });

        var actual = taskStatusDTOS.stream()
                .map((m) -> taskStatusMapper.map(m))
                .toList();
        var expected = userRepository.findAll();
        Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testCreateTaskStatus() throws Exception {
        var data = taskStatusMapper.mapCreate(Instancio.of(modelGenerator.getTaskStatusModel()).create());

        var request = post("/api/task_statuses")
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
        // .andDo(print());

        var tsakStatus = taskStatusRepository.findBySlug(data.getSlug()).orElse(null);
        assertNotNull(tsakStatus);
        assertThat(tsakStatus.getName()).isEqualTo(data.getName());
    }

    @Test
    public void testCreateNoValidSlugPTaskStatus() throws Exception {

        var data = taskStatusMapper.mapCreate(Instancio.of(modelGenerator.getTaskStatusModel()).create());
        data.setSlug(null);


        var request = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateNoValidNamePTaskStatus() throws Exception {
        var data = taskStatusMapper.mapCreate(Instancio.of(modelGenerator.getTaskStatusModel()).create());
        data.setName(null);

        var request = post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateTaskStatus() throws Exception {

        var data = taskStatusMapper.mapCreate(Instancio.of(modelGenerator.getTaskStatusModel()).create());

        System.out.println(data + " --------------------- ");
        var request = put("/api/task_statuses/" + testTaskStatus.getId())
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var user = taskStatusRepository.findById(testTaskStatus.getId()).orElse(null);
        assertNotNull(user);
        assertThat(user.getName()).isEqualTo(data.getName());
        assertThat(user.getSlug()).isEqualTo(data.getSlug());
    }

    @Test
    public void testPartUpdateTaskStatus() throws Exception {

        var data = new HashMap<>();
       // data.put("name", faker.lorem().words(2));
        data.put("slug", faker.lorem().words(2).stream()
                .map(w -> w.toLowerCase().replaceAll("[^a-z0-9]", ""))
                .collect(Collectors.joining("_")));

        data.put("email", faker.internet().emailAddress());
        var request = put("/api/task_statuses/" + testTaskStatus.getId())
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var user = taskStatusRepository.findById(testTaskStatus.getId()).orElse(null);
        assertNotNull(user);
        assertThat(user.getName()).isEqualTo(testTaskStatus.getName());
        assertThat(user.getSlug()).isEqualTo(data.get("slug"));
    }

    @Test
    public void testShowTaskStatus() throws Exception {
        var result = mockMvc.perform(get("/api/task_statuses/" + testTaskStatus.getId()))
                .andExpect(status().isOk())
                .andReturn();
        //.getResponse(); //.with(jwt());

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTaskStatus.getName()),
                v -> v.node("slug").isEqualTo(testTaskStatus.getSlug()));
    }

    @Test
    public void testDeleteTaskStatus() throws Exception {

        mockMvc.perform(delete("/api/task_statuses/" + testTaskStatus.getId()))
                .andExpect(status().isNoContent());

        var user = userRepository.findById(testTaskStatus.getId()).orElse(null);
        assertThat(user).isNull();

    }
}
