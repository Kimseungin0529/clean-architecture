plugins {
	java
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.project"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("commons-io:commons-io:2.11.0") // 현재 버전 확인하여 적절히 수정

	implementation ("com.google.cloud:google-cloud-texttospeech:2.10.0") // 구글 TTS 라이브러리
	implementation ("com.google.auth:google-auth-library-oauth2-http:2.0.0")

	//jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.1")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.1")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.1")

	//implementation ("org.springdoc:springdoc-openapi-ui:2.0.2") // swagger -> spring boot 3 이상부터는 해당 의존성 형식임, ui는 보여주기 위한 라이브러리
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2") // webmvc-ui는 json 형식을 보여주기 위함.

	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE") // amazon cloud 사용
	implementation ("org.springframework.boot:spring-boot-starter-webflux") // webflux, 모바일 사용


	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
