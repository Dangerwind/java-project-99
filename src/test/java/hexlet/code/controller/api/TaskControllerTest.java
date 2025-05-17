package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;

import org.instancio.Instancio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import java.util.Set;

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
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    private Task testTask;
    private TaskStatus testTaskStatus;
    private User testUser;
    private Label testLabel;
    @Autowired
    private LabelRepository labelRepository;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();


        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                // .apply(springSecurity())
                .build();
        var internalUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(internalUser);

        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);

        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);

        Set<Label> labelSet = Set.of(testLabel);

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setAssignee(internalUser);
        testTask.setTaskStatus(testTaskStatus);
        testTask.setLabels(labelSet);
        // taskRepository.save(testTask);
        // token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @AfterEach
    public void garbageDbDelete() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void testIndexTask() throws Exception {

        taskRepository.save(testTask);

        var response = mockMvc.perform(get("/api/tasks")) //.with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var tester = assertThatJson(response.getContentAsString()).isArray().first();

        tester.node("title").isEqualTo(testTask.getName());
        tester.node("content").isEqualTo(testTask.getDescription());
        tester.node("assignee_id").isEqualTo(testTask.getAssignee().getId());
        tester.node("status").isEqualTo(testTask.getTaskStatus().getSlug());
    }

    @Test
    public void testCreateTask() throws Exception {
        var data = taskMapper.map(testTask);

        var request = post("/api/tasks")
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
        // .andDo(print());

        var taskStatus = taskRepository.findByName(data.getTitle()).orElse(null);
        assertNotNull(taskStatus);
        assertThat(taskStatus.getIndex()).isEqualTo(data.getIndex());
        assertThat(taskStatus.getAssignee().getId()).isEqualTo(data.getAssigneeId().get());
       // assertThat(taskStatus.getName()).isEqualTo(data.getTitle());
        assertThat(taskStatus.getDescription()).isEqualTo(data.getContent());
        assertThat(taskStatus.getTaskStatus().getSlug()).isEqualTo(data.getStatus());
    }

    @Test
    public void testCreateNoValidNameTask() throws Exception {
        var data = taskMapper.map(testTask);
        data.setTitle("");

        var request = post("/api/tasks")
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateTask() throws Exception {
        taskRepository.save(testTask);
        testTask.setName("New title");
        testTask.setDescription("New description");

        var data = taskMapper.map(testTask);

        var request = put("/api/tasks/" + testTask.getId())
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());
        // .andDo(print());

        var taskStatus = taskRepository.findById(data.getId()).orElse(null);
        assertNotNull(taskStatus);
        assertThat(taskStatus.getIndex()).isEqualTo(data.getIndex());
        assertThat(taskStatus.getAssignee().getId()).isEqualTo(data.getAssigneeId().get());
        assertThat(taskStatus.getName()).isEqualTo(data.getTitle());
        assertThat(taskStatus.getDescription()).isEqualTo(data.getContent());
        assertThat(taskStatus.getTaskStatus().getSlug()).isEqualTo(data.getStatus());
    }

    @Test
    public void testPartUpdateTask() throws Exception {
        taskRepository.save(testTask);

        var updateData = new HashMap<String, String>();
        updateData.put("title", "New part update title");

        var data = taskMapper.map(testTask);

        var request = put("/api/tasks/" + testTask.getId())
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updateData));

        mockMvc.perform(request)
                .andExpect(status().isOk());
        // .andDo(print());

        var taskNew = taskRepository.findByName(updateData.get("title")).orElse(null);
        assertNotNull(taskNew);

        assertThat(taskNew.getIndex()).isEqualTo(data.getIndex());
        assertThat(taskNew.getAssignee().getId()).isEqualTo(data.getAssigneeId().get());
       // assertThat(taskNew.getName()).isEqualTo(updateData.get("title"));
        assertThat(taskNew.getDescription()).isEqualTo(data.getContent());
        assertThat(taskNew.getTaskStatus().getSlug()).isEqualTo(data.getStatus());


    }

    @Test
    public void testShowTask() throws Exception {

        taskRepository.save(testTask);

        var result = mockMvc.perform(get("/api/tasks/" + testTask.getId()))
                .andExpect(status().isOk())
                .andReturn();
        //.getResponse(); //.with(jwt());

        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()));
    }

    @Test
    public void testDeleteTask() throws Exception {
        taskRepository.save(testTask);

        mockMvc.perform(delete("/api/tasks/" + testTask.getId()))
                .andExpect(status().isNoContent());

        var task = taskRepository.findById(testTask.getId()).orElse(null);
        assertThat(task).isNull();
    }

    @Test
    public void testIndexTaskTitleCont() throws Exception {
        String findString = "first_string";
        String otherString = "second_string";

        testTask.setName(findString);
        testTask.setId(null);
        taskRepository.save(testTask);

        testTask.setName(otherString);
        testTask.setId(null);
        taskRepository.save(testTask);

        var result = mockMvc.perform(get("/api/tasks?titleCont=" + findString.substring(1, 4)))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body)
                .isArray()
                .allSatisfy(element1 ->  assertThatJson(element1)
                        .and(v -> v.node("title").asString()
                                .contains(findString)));

        assertThatJson(body)
                .isArray()
                .allSatisfy(element2 -> assertThatJson(element2)
                        .and(v -> v.node("title").asString()
                                .doesNotContain(otherString)));
    }

    @Test
    public void testIndexTaskAssigneeId() throws Exception {
        taskRepository.save(testTask);
        var firstId = testTask.getAssignee().getId();

        testUser.setId(null);
        testUser.setEmail("test@test.com");
        userRepository.save(testUser);

        testTask.setId(null);
        testTask.setAssignee(testUser);
        taskRepository.save(testTask);
        var secondId = testTask.getAssignee().getId();


        var result = mockMvc.perform(get("/api/tasks?assigneeId=" + firstId))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body)
                .isArray()
                .allSatisfy(element1 ->  assertThatJson(element1)
                        .node("assignee_id")
                        .asNumber()
                        .isEqualTo(BigDecimal.valueOf(firstId)));

        assertThatJson(body)
                .isArray()
                .allSatisfy(element2 -> assertThatJson(element2)
                        .node("assignee_id")
                        .asNumber()
                        .isNotEqualTo(BigDecimal.valueOf(secondId)));
    }
    @Test
    public void testIndexTaskByStatus() throws Exception {
        String findString = testTask.getTaskStatus().getSlug();
        taskRepository.save(testTask);

        testTaskStatus.setId(null);
        testTaskStatus.setSlug("my_test_slug");
        taskStatusRepository.save(testTaskStatus);

        testTask.setId(null);
        testTask.setTaskStatus(testTaskStatus);
        taskRepository.save(testTask);

        String otherString = testTask.getTaskStatus().getSlug();

        var result = mockMvc.perform(get("/api/tasks?status=" + findString))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body)
                .isArray()
                .allSatisfy(element1 ->  assertThatJson(element1)
                        .and(v -> v.node("status").asString()
                                .contains(findString)));

        assertThatJson(body)
                .isArray()
                .allSatisfy(element2 -> assertThatJson(element2)
                        .and(v -> v.node("status").asString()
                                .doesNotContain(otherString)));
    }

    @Test
    public void testIndexTaskByLabel() throws Exception {
        Long firstId = testLabel.getId();
        taskRepository.save(testTask);

        testLabel.setId(null);
        testLabel.setName("my_test_label");
        labelRepository.save(testLabel);

        Long otherId = testLabel.getId();

        Set<Label> labelSet = Set.of(testLabel);
        testTask.setId(null);
        testTask.setLabels(labelSet);
        taskRepository.save(testTask);

        var result = mockMvc.perform(get("/api/tasks?labelId=" + firstId))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body)
                .isArray()
                .allSatisfy(element1 ->  assertThatJson(element1)
                        .node("taskLabelIds")
                        .isArray()
                        .contains(BigDecimal.valueOf(firstId)));

        assertThatJson(body)
                .isArray()
                .allSatisfy(element2 -> assertThatJson(element2)
                        .node("taskLabelIds")
                        .isArray()
                        .doesNotContain(BigDecimal.valueOf(otherId)));
    }
}
