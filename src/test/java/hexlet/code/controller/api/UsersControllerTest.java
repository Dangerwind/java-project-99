package hexlet.code.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import hexlet.code.dto.user.UserDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;

import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hexlet.code.util.ModelGenerator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    private User testUser;
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);

        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));
    }

    @AfterEach
    public void garbageDbDelete() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<UserDTO> userDTOS = om.readValue(body, new TypeReference<>() { });

        var actual = userDTOS.stream()
                .map((m) -> userMapper.map(m))
                .toList();
        var expected = userRepository.findAll();
        Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testCreateUser() throws Exception {
        var data = userMapper.mapCreate(Instancio.of(modelGenerator.getUserModel()).create());

        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(data.getEmail()).orElse(null);
        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(data.getFirstName().get());
        assertThat(user.getLastName()).isEqualTo(data.getLastName().get());
    }

// тест на создание пользователя с невалидным коротким именем -----------------------
    @Test
    public void testNoValidPasswordCreateUser() throws Exception {
        var data = new HashMap<>();
        data.put("email", "valid@valid.com");
        data.put("password", "va");

        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

// тест на создание пользователя с невалидным email -----------------------
    @Test
    public void testNoValidEmailCreateUser() throws Exception {
        var data = new HashMap<>();
        data.put("email", "validvalid.com");
        data.put("password", "valid");

        var request = post("/api/users")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUser() throws Exception {

        var data = new HashMap<>();
        data.put("firstName", faker.name().firstName());
        data.put("lastName", faker.name().lastName());
        data.put("email", faker.internet().emailAddress());
        var request = put("/api/users/" + testUser.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var user = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(data.get("firstName"));
        assertThat(user.getLastName()).isEqualTo(data.get("lastName"));
        assertThat(user.getEmail()).isEqualTo(data.get("email"));
    }

// частичное обновление только email -------------------
    @Test
    public void testPartUpdateUser() throws Exception {
        var data = new HashMap<>();
      //  data.put("firstName", faker.name().firstName());
      //  data.put("lastName", faker.name().lastName());
        data.put("email", faker.internet().emailAddress());
        var request = put("/api/users/" + testUser.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var user = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(user.getEmail()).isEqualTo(data.get("email"));
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(get("/api/users/" + testUser.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(testUser.getEmail()),
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()));
    }

    @Test
    public void testDeleteUser() throws Exception {

        mockMvc.perform(delete("/api/users/" + testUser.getId())
                .with(token))
                .andExpect(status().isNoContent());

        var user = userRepository.findById(testUser.getId()).orElse(null);
        assertThat(user).isNull();
    }

// тест на удаление юзера который связан с задачей -----------------
    @Test
    public void testDeleteJoinUser() throws Exception {

        var testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
        labelRepository.save(testLabel);

        Set<Label> labelSet = Set.of(testLabel);

        var testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testTaskStatus);

        var testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTask.setAssignee(testUser);
        testTask.setTaskStatus(testTaskStatus);
        testTask.setLabels(labelSet);
        taskRepository.save(testTask);

// должна быть ошибка 400 -------------
        mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token))
                .andExpect(status().isBadRequest());
    }

// удаление другого одного юзера другим юзером ---------------
    @Test
    public void testDeleteUnAuthenticUser() throws Exception {
        var oldTestUserId = testUser.getId();

        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(testUser.getEmail()));

// должна быть ошибка 403
        mockMvc.perform(delete("/api/users/" + oldTestUserId)
                        .with(token))
                .andExpect(status().isForbidden());
    }
}
