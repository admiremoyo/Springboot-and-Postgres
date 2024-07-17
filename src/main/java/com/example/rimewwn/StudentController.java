package com.example.rimewwn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/students")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestParam("imageFile") MultipartFile imageFile,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("age") Integer age,
                                                 @RequestParam("dob") String dob,
                                                 @RequestParam("email") String email) throws IOException {
        LocalDate dobDate = LocalDate.parse(dob.trim());

        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        String uploadPath = UPLOAD_DIR + fileName;

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        Files.copy(imageFile.getInputStream(), Paths.get(uploadPath), StandardCopyOption.REPLACE_EXISTING);

        Student student = new Student(null, name, age, dobDate, email, uploadPath);
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(id, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Student> searchStudentsByName(@RequestParam("name") String name) {
        return studentService.searchStudentsByName(name);
    }
}
