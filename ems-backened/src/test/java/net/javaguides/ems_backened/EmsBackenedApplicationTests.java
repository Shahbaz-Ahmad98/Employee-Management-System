package net.javaguides.ems_backened;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import net.javaguides.ems_backened.entity.Employee;
import net.javaguides.ems_backened.repository.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.config.import=",
        "spring.datasource.url=jdbc:h2:mem:ems-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
class EmsBackenedApplicationTests {

    @Autowired WebApplicationContext context;
    @Autowired EmployeeRepository repository;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void employeeCrud() throws Exception {
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Ada","lastName":"Lovelace","email":"ada@example.com"}
                                """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNumber());
        Long id = repository.findAll().getFirst().getId();
        mvc.perform(get("/api/employees")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Ada"));
        mvc.perform(get("/api/employees/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ada@example.com"));
        mvc.perform(put("/api/employees/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Grace","lastName":"Hopper","email":"grace@example.com"}
                                """))
                .andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("Grace"));
        mvc.perform(delete("/api/employees/{id}", id)).andExpect(status().isOk());
        assertEquals(0, repository.count());
        mvc.perform(get("/api/employees/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void creationIgnoresClientId() throws Exception {
        Employee existing = repository.save(new Employee(null, "Ada", "Lovelace", "ada@example.com"));
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":%d,"firstName":"Grace","lastName":"Hopper","email":"grace@example.com"}
                                """.formatted(existing.getId())))
                .andExpect(status().isCreated());
        assertEquals(2, repository.count());
        assertEquals("Ada", repository.findById(existing.getId()).orElseThrow().getFirstName());
    }

    @Test
    void rejectsInvalidCreateAndUpdate() throws Exception {
        String invalid = """
                {"firstName":" ","lastName":"","email":"invalid"}
                """;
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.errors.firstName").exists())
                .andExpect(jsonPath("$.errors.lastName").exists()).andExpect(jsonPath("$.errors.email").exists());
        mvc.perform(put("/api/employees/1").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest());
        assertEquals(0, repository.count());
    }

    @Test
    void duplicateEmailReturnsConflict() throws Exception {
        repository.save(new Employee(null, "Ada", "Lovelace", "ada@example.com"));
        Employee other = repository.save(new Employee(null, "Grace", "Hopper", "grace@example.com"));
        String duplicate = """
                {"firstName":"Other","lastName":"Employee","email":"ada@example.com"}
                """;
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(duplicate))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.message").exists());
        mvc.perform(put("/api/employees/{id}", other.getId()).contentType(MediaType.APPLICATION_JSON).content(duplicate))
                .andExpect(status().isConflict());
        assertEquals("grace@example.com", repository.findById(other.getId()).orElseThrow().getEmail());
    }

    @Test
    void missingUpdateAndDeleteReturnNotFound() throws Exception {
        mvc.perform(put("/api/employees/999999").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Ada","lastName":"Lovelace","email":"ada@example.com"}
                                """))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/employees/999999")).andExpect(status().isNotFound());
    }

    @Test
    void frontendCanCallApi() throws Exception {
        mvc.perform(options("/api/employees/1").header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "PUT")
                        .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isOk()).andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

}
