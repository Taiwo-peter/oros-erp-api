package ng.oros.erp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DepartmentController {

    private final DepartmentRepository repository;

    @Value("${spring.application.name:erp-api}")
    private String serviceName;

    public DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", serviceName);
        response.put("status", "ok");
        response.put("timestamp", LocalDateTime.now().toString());

        try {
            repository.count();
            response.put("database", "ok");
        } catch (Exception e) {
            response.put("database", "error: " + e.getMessage());
            response.put("status", "degraded");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public Map<String, Object> index() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", serviceName);
        info.put("version", "1.0.0");
        info.put("endpoints", List.of("/health", "/api/v1/departments"));
        return info;
    }

    @GetMapping("/api/v1/departments")
    public List<Department> listDepartments() {
        return repository.findAll();
    }

    @PostMapping("/api/v1/departments")
    public ResponseEntity<?> createDepartment(@RequestBody Department department) {
        if (department.getName() == null || department.getCode() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "name and code are required"));
        }
        if (repository.existsByCode(department.getCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Department code already exists"));
        }
        Department saved = repository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/api/v1/departments/{id}")
    public ResponseEntity<?> getDepartment(@PathVariable Long id) {
        return repository.findById(id)
                .map(d -> ResponseEntity.ok((Object) d))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Department not found")));
    }

    @DeleteMapping("/api/v1/departments/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Department not found"));
        }
        repository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Department deleted"));
    }
}
