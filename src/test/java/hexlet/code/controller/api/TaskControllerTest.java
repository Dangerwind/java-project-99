package hexlet.code.controller.api;
/*
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskStatusDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.instancio.Select;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private Task testTask;

//    @Autowired
 //   private TaskRepository taskRepository;


    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                // .apply(springSecurity())
                .build();

      //  testTask = Instancio.of(modelGenerator.getTaskModel()).create();

      //  taskRepository.save(testTask);
        // token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @AfterEach
    public void garbageDbDelete() {
        taskRepository.deleteAll();
    }


    */

    /*
    @Test
    public void testIndexTask() throws Exception {
        var response = mockMvc.perform(get("/api/task")) //.with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<TaskDTO> taskDTOS = om.readValue(body, new TypeReference<>() { });

        var actual = taskDTOS.stream()
                .map((m) -> taskMapper.map(m))
                .toList();
        var expected = taskRepository.findAll();
        Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
*/
/*
    @Test
    public void testCreateTask() throws Exception {
        var data = taskMapper.map(Instancio.of(modelGenerator.getTaskModel()).create());

        var request = post("/api/task")
                //  .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());
        // .andDo(print());

        var tsakStatus = taskRepository.findByName(data.getTitle()).orElse(null);
        assertNotNull(tsakStatus);
        assertThat(tsakStatus.getIndex()).isEqualTo(data.getIndex());
        assertThat(tsakStatus.getAssignee()).isEqualTo(data.getAssigneeId());
        assertThat(tsakStatus.getName()).isEqualTo(data.getTitle());
        assertThat(tsakStatus.getDescription()).isEqualTo(data.getContent());
        assertThat(tsakStatus.getTaskStatus().getSlug()).isEqualTo(data.getStatus());
    }
*/
}
