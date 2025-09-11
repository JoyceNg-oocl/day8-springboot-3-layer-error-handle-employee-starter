package com.example.demo;

import com.example.demo.entity.Employee;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private void createJohnSmith() throws Exception {
        Gson gson = new Gson();
        String johnSmithJson = gson.toJson(new Employee(null, "John Smith", 28, "MALE", 60000.0));
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(johnSmithJson));
    }

    private void createJaneDoe() throws Exception {
        Gson gson = new Gson();
        String janeDoeJson = gson.toJson(new Employee(null, "Jane Doe", 22, "FEMALE", 60000.0));
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(janeDoeJson));
    }

    @BeforeEach
    void cleanEmployees() throws Exception {
//        jdbcTemplate.execute("TRUNCATE TABLE employee");
        jdbcTemplate.execute("DELETE FROM employee");
        jdbcTemplate.execute("ALTER TABLE employee AUTO_INCREMENT = 1");
    }

    @Test
    void should_return_404_when_employee_not_found() throws Exception {
        mockMvc.perform(get("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_employee() throws Exception {
        createJaneDoe();
        createJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_employee_when_employee_found() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(60000.0));
    }

    @Test
    void should_return_male_employee_when_employee_found() throws Exception {
        createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(28))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(60000.0));
    }

    @Test
    void should_create_employee() throws Exception {
        String requestBody = new Gson().toJson(new Employee(null, "John Smith", 28, "MALE", 60000.0));

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    void should_return_200_with_empty_body_when_no_employee() throws Exception {
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_return_200_with_employee_list() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(28))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(60000));
    }

    @Test
    void should_status_204_when_delete_employee() throws Exception {
        createJohnSmith();

        mockMvc.perform(delete("/employees/" + 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_status_200_when_update_employee() throws Exception {
        createJohnSmith();

        String requestBody = new Gson().toJson(new Employee(null, "John Smith", 29, "MALE", 65000.0));

        mockMvc.perform(put("/employees/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.age").value(29))
                .andExpect(jsonPath("$.salary").value(65000.0));
    }

    @Test
    void should_status_200_and_return_paged_employee_list() throws Exception {
        createJohnSmith();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_throw_exception_when_employee_of_greater_than_65_or_less_than_18() throws Exception {
        String requestBody = new Gson().toJson(new Employee(null, "John Smith", 17, "MALE", 65000.0));

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Employee age less than 18 or greater than 65!"));

    }

    @Test
    void should_throw_exception_when_employee_of_greater_than_or_equal_to_30_salary_below_20000() throws Exception {
        String requestBody = new Gson().toJson(new Employee(null, "John Smith", 30, "MALE", 19999.0));
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Employee age greater than or equal to 30 but salary below 20000!"));
    }

    @Test
    void should_return_active_status_true_when_employee_created() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeStatus").value(true));
    }

    @Test
    void should_return_active_false_when_employee_deleted() throws Exception {
        createJohnSmith();

        mockMvc.perform(delete("/employees/" + 1))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeStatus").value(false));
    }

    @Test
    void should_throw_exception_when_update_a_left_employee() throws Exception {
        Gson gson = new Gson();
        String johnSmithJson = gson.toJson(new Employee(null, "John Smith", 28, "MALE", 60000.0));
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(johnSmithJson));

        mockMvc.perform(delete("/employees/" + 1))
                .andExpect(status().isNoContent());

        mockMvc.perform(put("/employees/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(johnSmithJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Employee has been left with id: 1"));
    }
}
