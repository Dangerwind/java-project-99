package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.LabelMapper;
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

import java.nio.charset.StandardCharsets;
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
public class LabelControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    private Label testLabel;

    private Task testTask;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    public void setUp() {
        labelRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                // .apply(springSecurity())
                .build();
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();

        // token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @AfterEach
    public void garbageDbDelete() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void testIndexLabel() throws Exception {
        labelRepository.save(testLabel);
        var response = mockMvc.perform(get("/api/labels")) //.with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var tester = assertThatJson(response.getContentAsString()).isArray().first();
        tester.node("name").isEqualTo(testLabel.getName());
    }

    @Test
    public void testCreateLabel() throws Exception {
        var data = labelMapper.map(testLabel);

        var request = post("/api/labels")
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
        // .andDo(print());

        var label = labelRepository.findByName(data.getName()).orElse(null);
        assertNotNull(label);
        assertThat(label.getName()).isEqualTo(data.getName());
    }
    @Test

    public void testCreateNoValidShortLabel() throws Exception {
        var data = labelMapper.map(testLabel);
        data.setName("N");

        var request = post("/api/labels")
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateLabel() throws Exception {
        labelRepository.save(testLabel);
        testLabel.setName("New label");
        var data = labelMapper.map(testLabel);

        var request = put("/api/labels/" + testLabel.getId())
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());
        // .andDo(print());

        var label = labelRepository.findById(data.getId()).orElse(null);
        assertNotNull(label);
        assertThat(label.getName()).isEqualTo(data.getName());
    }

    @Test
    public void testDeleteLabel() throws Exception {
        labelRepository.save(testLabel);

        mockMvc.perform(delete("/api/labels/" + testLabel.getId()))
                .andExpect(status().isNoContent());

        var label = labelRepository.findById(testLabel.getId()).orElse(null);
        assertThat(label).isNull();
    }

    @Test
    public void testDeleteJoinLabel() throws Exception {
        labelRepository.save(testLabel);
        Set<Label> labelSet = Set.of(testLabel);

        var testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);

        var testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setAssignee(testUser);
        testTask.setTaskStatus(testTaskStatus);
        testTask.setLabels(labelSet);

        taskRepository.save(testTask);

        mockMvc.perform(delete("/api/labels/" + testLabel.getId()))
                .andExpect(status().isBadRequest());
    }

}
