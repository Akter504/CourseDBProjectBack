package ru.maryan.webproject.coursedbprojectback.controllers;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BackupController {

    @GetMapping("/backup")
    public ResponseEntity<InputStreamResource> createBackup() {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "exec", "DB", "pg_dump", "-U", "admin", "-F", "c", "-f", "/db_backup.dump", "postgres");
            pb.environment().put("PGPASSWORD", "12345"); // Specify the password
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            ProcessBuilder copyPb = new ProcessBuilder("docker", "cp", "DB:/db_backup.dump", "db_backup.dump");
            copyPb.start().waitFor();

            File file = new File("db_backup.dump");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/restore")
    public ResponseEntity<String> restoreBackup(@RequestParam("file") MultipartFile file) {
        try {
            File backupFile = new File("db_backup.dump");
            file.transferTo(backupFile);

            ProcessBuilder dropTablesPb = new ProcessBuilder("docker", "exec", "DB", "psql", "-U", "admin", "-d", "postgres", "-c", "DROP SCHEMA public CASCADE; CREATE SCHEMA public;");
            dropTablesPb.environment().put("PGPASSWORD", "12345");
            Process dropTablesProcess = dropTablesPb.start();
            int dropTablesExitCode = dropTablesProcess.waitFor();

            if (dropTablesExitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка удаления таблиц.");
            }

            ProcessBuilder restorePb = new ProcessBuilder("docker", "exec", "DB", "pg_restore", "-U", "admin", "-d", "postgres", "/db_backup.dump");
            restorePb.environment().put("PGPASSWORD", "12345");
            Process restoreProcess = restorePb.start();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(restoreProcess.getErrorStream()));
            String errors = errorReader.lines().collect(Collectors.joining("\n"));

            BufferedReader outputReader = new BufferedReader(new InputStreamReader(restoreProcess.getInputStream()));
            String output = outputReader.lines().collect(Collectors.joining("\n"));

            System.out.println("Standard Output: " + output);
            System.out.println("Error Output: " + errors);
            int restoreExitCode = restoreProcess.waitFor();

            if (restoreExitCode == 0) {
                return ResponseEntity.ok("База данных успешно восстановлена.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка восстановления базы данных.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка восстановления базы данных.");
        }
    }
}

