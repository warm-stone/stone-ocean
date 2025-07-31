package com.example.stoneocean;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.example.stoneocean.mapper")
public class StoneOceanApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoneOceanApplication.class, args);
	}


}
