package com.example.Student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.Student.Entity.StudentRequest;
import com.example.Student.Repository.StudentRequestRepository;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StudentApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(StudentRequestRepository repo) {
		return args -> {

			StudentRequest s1 = new StudentRequest();
			s1.setStudentName("Ali");
			s1.setEmail("ali@gmail.com");
			s1.setUniversity("ESPRIT");
			s1.setCompanyName("Google");
			s1.setStatus("PENDING");

			StudentRequest s2 = new StudentRequest();
			s2.setStudentName("Sara");
			s2.setEmail("sara@gmail.com");
			s2.setUniversity("INSAT");
			s2.setCompanyName("Microsoft");
			s2.setStatus("APPROVED");

			StudentRequest s3 = new StudentRequest();
			s3.setStudentName("Omar");
			s3.setEmail("omar@gmail.com");
			s3.setUniversity("FST");
			s3.setCompanyName("Amazon");
			s3.setStatus("REJECTED");

			repo.save(s1);
			repo.save(s2);
			repo.save(s3);

			System.out.println("Students inserted successfully 🚀");
		};
	}
}