import groovy.lang.Closure

plugins {
	java
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.canyoncompanion"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val mapstructVersion = "1.6.3"

dependencies {

	// =========================
	// SPRING
	// =========================
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// =========================
	// SECURITY
	// =========================
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// =========================
	// POSTGRESQL
	// =========================
	runtimeOnly("org.postgresql:postgresql")

	// =========================
	// LOMBOK
	// =========================
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	// =========================
	// MAPSTRUCT
	// =========================
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
	annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

	// Lombok + MapStruct compatibility
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	// =========================
	// DEVTOOLS
	// =========================
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	// ========================
	// SWAGGER
	// =========================
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")
	// =========================
	// TESTS
	// =========================
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}